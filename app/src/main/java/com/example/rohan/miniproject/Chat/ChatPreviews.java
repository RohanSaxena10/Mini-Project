package com.example.rohan.miniproject.Chat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.rohan.miniproject.FragmentContainers.Tab3ContainerFragment;
import com.example.rohan.miniproject.Profile.SettingsDialogFragment;
import com.example.rohan.miniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rohan on 01/04/17.
 */
public class ChatPreviews extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_preview_page, null);

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Tab3ContainerFragment)getParentFragment()).replaceFragment(new UsersListFragment(),true);
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.rvchats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));
        recyclerView.setAdapter(new ChatPreviewAdapter(FirebaseDatabase.getInstance().getReference().child("UsersChats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()),this));
    return view;
    }


    class ChatPreviewAdapter extends RecyclerView.Adapter<ChatPreviewAdapter.ViewHolder>{


        ArrayList<ChatMessage> latestprviews;
        ArrayList<String> keyslist;
        DatabaseReference databaseReference;
        Fragment fragment;

        public ChatPreviewAdapter(final DatabaseReference databaseReference,Fragment fragment) {
            this.databaseReference = databaseReference;
            latestprviews = new ArrayList<>();
            keyslist = new ArrayList<>();
            this.fragment = fragment;

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    latestprviews.add(dataSnapshot.getValue(ChatMessage.class));
                    keyslist.add(dataSnapshot.getKey());
                    notifyItemInserted(latestprviews.size() -1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   int key = keyslist.indexOf(dataSnapshot.getKey());
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    latestprviews.set(key,chatMessage);
                    Toast.makeText(getActivity(),"Changed",Toast.LENGTH_LONG).show();
                    notifyItemChanged(key);

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
        public ChatPreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_preview_template, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChatPreviewAdapter.ViewHolder holder, final int position) {

            FirebaseDatabase.getInstance().getReference().child("Users").child(keyslist.get(position)).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.name.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chat chat = new Chat();
                    Bundle bundle = new Bundle();
                    bundle.putString("uid",keyslist.get(position));
                    chat.setArguments(bundle);
                    ((Tab3ContainerFragment)fragment.getParentFragment()).replaceFragment(chat,true);
                }
            });
            holder.imgdp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chat chat = new Chat();
                    Bundle bundle = new Bundle();
                    bundle.putString("uid",keyslist.get(position));
                    chat.setArguments(bundle);
                    ((Tab3ContainerFragment)fragment.getParentFragment()).replaceFragment(chat,true);
                }
            });
            Date date = new Date((latestprviews.get(position).getMessageTime()));
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
            String dateText = df2.format(date);
            holder.time.setText(dateText.toString());

            holder.content.setText(latestprviews.get(position).getMessageText());

            FirebaseDatabase.getInstance().getReference().child("Users").child(keyslist.get(position)).child("PhotoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Glide.with(getActivity())
                            .load(dataSnapshot.getValue().toString())
                    .asBitmap()
                            .into(new BitmapImageViewTarget(holder.imgdp) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    holder.imgdp.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return latestprviews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            TextView name = (TextView)itemView.findViewById(R.id.name);
            TextView time = (TextView)itemView.findViewById(R.id.time);
            TextView content = (TextView)itemView.findViewById(R.id.message);
            ImageView imgdp = (ImageView)itemView.findViewById(R.id.imgdp);
        }
    }


}
