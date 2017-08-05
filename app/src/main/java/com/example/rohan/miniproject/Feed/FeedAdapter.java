package com.example.rohan.miniproject.Feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.rohan.miniproject.Chat.Chat;
import com.example.rohan.miniproject.FragmentContainers.Tab1ContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab2ContainerFragment;
import com.example.rohan.miniproject.Profile.DifferentUserProfile;
import com.example.rohan.miniproject.Profile.UserProfile;
import com.example.rohan.miniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Rohan on 31/03/17.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {


    Context context;
    private final Handler mHandler = new Handler();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ArrayList<String> nameList;
    ArrayList<String> keyList;
    ArrayList<String> photoURLlist;
    ArrayList<String> onlinelist;
    ArrayList<Long> emergencylist;

    android.app.Fragment parentfragment;
    //Double mLastRandom = 2d;
    //Random mRand;
    Runnable mTimer2;

    public FeedAdapter(final Context context, android.app.Fragment fragment) {
        this.context = context;
        parentfragment = fragment;
        nameList = new ArrayList<>();
        keyList = new ArrayList<>();
        photoURLlist = new ArrayList<>();
        onlinelist = new ArrayList<>();
        emergencylist = new ArrayList<>();
       // mRand = new Random();


        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!(dataSnapshot.getKey().equals(user.getUid()))) {

                    FeedPOJO pojo = dataSnapshot.getValue(FeedPOJO.class);
                    keyList.add(dataSnapshot.getKey());
                    nameList.add(pojo.getName());
                    photoURLlist.add(pojo.getPhotoURL());
                    onlinelist.add(pojo.getDisconnect());
                    emergencylist.add(pojo.getEmergencyContact());
                   //TODO Toast.makeText(context,pojo.getDisconnect(),Toast.LENGTH_LONG).show();
                   //TODO Toast.makeText(context,pojo.getPhotoURL(),Toast.LENGTH_LONG).show();
                    notifyItemInserted(keyList.size() - 1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if(!(dataSnapshot.getKey().equals(user.getUid()))) {
                    int key = keyList.indexOf(dataSnapshot.getKey());
                    onlinelist.set(key, dataSnapshot.getValue(FeedPOJO.class).getDisconnect());
                    notifyItemChanged(key);
                    Toast.makeText(context, "Changed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtname.setText(nameList.get(position));
        holder.txtname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DifferentUserProfile differentUserProfile = new DifferentUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("uid",keyList.get(position));
                differentUserProfile.setArguments(bundle);
                ((Tab2ContainerFragment)(parentfragment.getParentFragment())).replaceFragment(differentUserProfile,true);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DifferentUserProfile differentUserProfile = new DifferentUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("uid",keyList.get(position));
                differentUserProfile.setArguments(bundle);
                ((Tab2ContainerFragment)(parentfragment.getParentFragment())).replaceFragment(differentUserProfile,true);
            }
        });
        Glide.with(context)
                .load(photoURLlist.get(position))
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
        if(onlinelist.get(position).equals("Disconnected"))
        {
            //Toast.makeText(context,"Disconnected",Toast.LENGTH_LONG).show();
            holder.imageView2.setBackgroundResource(R.drawable.red_circle);


        }
        else
        {
            holder.imageView2.setBackgroundResource(R.drawable.green_circle);
            mTimer2 = new Runnable() {
                @Override
                public void run() {

/*
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        graph2LastXValue += 1d;
                      //TODO  mLastRandom = (Long)(dataSnapshot.getValue());
                        mSeries2.appendData(new DataPoint(graph2LastXValue, mLastRandom), true, 40);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                FirebaseDatabase.getInstance().getReference().child("Heartbeat").addValueEventListener(valueEventListener);

*/

                    holder.heartrate.setText(String.valueOf(getRandom()));
                    mHandler.postDelayed(this, 800);
                }
            };
            mHandler.postDelayed(mTimer2, 100);

        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_CALL);
                FirebaseDatabase.getInstance().getReference().child("Users").child(keyList.get(position)).child("EmergencyContact").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            intent.setData(Uri.parse("tel:" + dataSnapshot.getValue().toString()));
                            parentfragment.getActivity().startActivity(intent);
                        }
                        else
                            Toast.makeText(context,"No Emergency Contact",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }



    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView txtname = (TextView)itemView.findViewById(R.id.txtname);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imgdp);
        View imageView2 = (View)itemView.findViewById(R.id.imgonline);
        TextView heartrate = (TextView)itemView.findViewById(R.id.txtheartrate);

        ImageView call = (ImageView)itemView.findViewById(R.id.imgcallemergency);
    }

    int min= 50;
    int max = 150;
    Double mRand = Math.random();
    private int getRandom() {


        int random = (int )(Math.random() * max + 40);




       /* if(maxheartbeat - Math.floor(mLastRandom*100)/100 < 0.8) {
            textView.setTextColor(Color.RED);
            textView.setText(String.valueOf(Math.floor(mLastRandom * 100) / 100));
        }
        else
        {
            textView.setTextColor(Color.BLACK);
            textView.setText(String.valueOf(Math.floor(mLastRandom*100)/100));
        }
        */
        return random;

      /*  ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLastRandom = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Heartbeat").addValueEventListener(valueEventListener);
        return mLastRandom;*/
    }


    public void removecallback()
    {
        mHandler.removeCallbacks(mTimer2);
    }



}

