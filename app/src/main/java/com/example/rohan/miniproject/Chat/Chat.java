package com.example.rohan.miniproject.Chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohan.miniproject.MainActivity;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohan on 01/04/17.
 */
public class Chat extends Fragment {


    RecyclerView chatsectionrv;
    DatabaseReference databaseReference;
    TextView username;
    TextView onlinestatus;
    String name;
    FloatingActionButton fab;
    EditText input;
    DatabaseReference yourreference;
    DatabaseReference friendsreference;
    ImageView imgonline;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat, null);
        username = (TextView) view.findViewById(R.id.name);

        imgonline = (ImageView)view.findViewById(R.id.imgonline);
        onlinestatus = (TextView)view.findViewById(R.id.onlinestatus);
        input = (EditText)view.findViewById(R.id.input);
        yourreference =FirebaseDatabase.getInstance().getReference().child("UsersChats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getArguments().getString("uid"));
        friendsreference =FirebaseDatabase.getInstance().getReference().child("UsersChats").child(getArguments().getString("uid")).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fab = (FloatingActionButton)view.findViewById(R.id.fab);

                     fab.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             ChatMessage chatMessage = new ChatMessage();
                             chatMessage.setMessageText(input.getText().toString());
                             chatMessage.setMessageUser("You");
                             chatMessage.setSeen("Unseen");

                             ChatMessage chatMessage2 = new ChatMessage();
                             chatMessage2.setMessageText(input.getText().toString());
                             chatMessage2.setMessageUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                             // Read the input field and push a new instance
                             // of ChatMessage to the Firebase database

                             String key = databaseReference.push().getKey();
                             databaseReference
                                     .child(key)
                                     .setValue(chatMessage);

                             FirebaseDatabase.getInstance().getReference().child("UserConversations").child(getArguments().getString("uid"))
                                     .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(chatMessage2);


                             chatMessage2.setSeen("Unseen");
                             yourreference.setValue(chatMessage2);
                             friendsreference.setValue(chatMessage2);


                             // Clear the input
                             input.setText("");

                         }
                     });


        FirebaseDatabase.getInstance().getReference().child("Users").child(getArguments().getString("uid")).child("disconnect").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("Connected")) {
                    onlinestatus.setText("Online");
                    imgonline.setImageResource(R.drawable.green_circle);
                }
                else {
                    onlinestatus.setText("Offline");
                    imgonline.setImageResource(R.drawable.red_circle);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserConversations").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getArguments().getString("uid"));
        chatsectionrv = (RecyclerView)view.findViewById(R.id.list_of_messages);
        chatsectionrv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        chatsectionrv.setAdapter(new ChatAdapter(databaseReference));



        FirebaseDatabase.getInstance().getReference().child("Users").child(getArguments().getString("uid")).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    username.setText(dataSnapshot.getValue().toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }



    public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<ChatMessage> chatpojos;
        DatabaseReference databaseReference;
        ArrayList<String> keyslist;

        public ChatAdapter(DatabaseReference databaseReference) {
            this.databaseReference = databaseReference;
            chatpojos = new ArrayList<>();
            keyslist = new ArrayList<>();

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatpojos.add(chatMessage);
                    notifyItemInserted(chatpojos.size() - 1);
                    chatsectionrv.scrollToPosition(chatpojos.size() -1);
                    keyslist.add(dataSnapshot.getKey());




                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue(ChatMessage.class).getMessageUser().equals("You"))
                    {
                        int key = keyslist.indexOf(dataSnapshot.getKey());
                        chatpojos.set(key,dataSnapshot.getValue(ChatMessage.class));
                        notifyItemChanged(key);
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
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
                    return new IncomingHolder(view);
                }
                case 1:
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_outgoing,parent,false);
                    return new OutgoingHolder(view);
                }
                default:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
                    return new OutgoingHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {
                case 0: {

                    IncomingHolder incomingHolder = (IncomingHolder)holder;
                    incomingHolder.name.setText(chatpojos.get(position).getMessageUser());
                    Date date = new Date((chatpojos.get(position).getMessageTime()));
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                    String dateText = df2.format(date);
                    incomingHolder.time.setText(dateText.toString());
                    incomingHolder.content.setText(chatpojos.get(position).getMessageText());
                    FirebaseDatabase.getInstance().getReference().child("UserConversations").child(getArguments().getString("uid")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(keyslist.get(position))
                            .child("seen").setValue("Seen");
                    break;
                }

                case 1:{

                    OutgoingHolder outgoingHolder = (OutgoingHolder) holder;
                    outgoingHolder.name.setText("You");
                    Date date = new Date((chatpojos.get(position).getMessageTime()));
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                    String dateText = df2.format(date);
                    outgoingHolder.time.setText(dateText.toString());
                    outgoingHolder.content.setText(chatpojos.get(position).getMessageText());

                    if(chatpojos.get(position).getSeen() != null && chatpojos.get(position).getSeen().equals("Seen"))
                        outgoingHolder.imgseen.setVisibility(View.VISIBLE);
                    break;

                }
            }
        }

        @Override
        public int getItemCount(){
            return chatpojos.size();
        }

        public class IncomingHolder extends RecyclerView.ViewHolder {
            public IncomingHolder(View itemView) {
                super(itemView);
            }

            TextView name = (TextView)itemView.findViewById(R.id.message_user);
            TextView time = (TextView)itemView.findViewById(R.id.message_time);
            TextView content =(TextView)itemView.findViewById(R.id.message_text);
        }

        public class OutgoingHolder extends RecyclerView.ViewHolder {
            public OutgoingHolder(View itemView) {
                super(itemView);
            }

            TextView name = (TextView)itemView.findViewById(R.id.message_user);
            TextView time = (TextView)itemView.findViewById(R.id.message_time);
            TextView content =(TextView)itemView.findViewById(R.id.message_text);
            ImageView imgseen = (ImageView) itemView.findViewById(R.id.imgseen);
        }



        @Override
        public int getItemViewType(int position) {
            if(chatpojos.get(position).getMessageUser().toString().equals("You"))
                return 1;
            else {
                return 0;
            }
        }


    }
}

