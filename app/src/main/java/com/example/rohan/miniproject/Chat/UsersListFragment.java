package com.example.rohan.miniproject.Chat;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.rohan.miniproject.Feed.FeedPOJO;
import com.example.rohan.miniproject.FragmentContainers.Tab3ContainerFragment;
import com.example.rohan.miniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Rohan on 01/04/17.
 */
public class UsersListFragment extends Fragment {


    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.users_list, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new UsersAdapter(getActivity(),this));


        return v;
    }

    public void done(String arg)
    {

        Chat chat = new Chat();
        Bundle bundle = new Bundle();
        bundle.putString("uid",arg);
        chat.setArguments(bundle);
        ((Tab3ContainerFragment)getParentFragment()).replaceFragment(chat,true);
    }

    class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

        Context context;
        ArrayList<FeedPOJO> feedPOJOs;
        ArrayList<String> keys;
        Fragment fragment;
        public UsersAdapter(Context context,Fragment fragment) {
            this.context = context;
            this.fragment = fragment;
            keys = new ArrayList<>();
            feedPOJOs = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        feedPOJOs.add(dataSnapshot.getValue(FeedPOJO.class));
                        keys.add(dataSnapshot.getKey());
                        notifyItemInserted(feedPOJOs.size()-1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_template, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final UsersAdapter.ViewHolder holder, final int position) {
            holder.name.setText(feedPOJOs.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((UsersListFragment)fragment).done(keys.get(position));
                }
            });
            Glide.with(context)
                    .load(feedPOJOs.get(position).getPhotoURL())
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
        }

        @Override
        public int getItemCount() {
            return feedPOJOs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            TextView name = (TextView)itemView.findViewById(R.id.name);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imgdp);
        }
    }
}
