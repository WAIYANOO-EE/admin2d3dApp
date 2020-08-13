package com.waiyanoo.myanmar2d3dliveapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.waiyanoo.myanmar2d3dliveapp.models.ChatItem;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ImageView btnSend;
    CollectionReference chatRef;
    FirebaseAuth mAuth;
    EditText edtMessage;
    private static final String TAG = "FacebookLogin";
    private static final int FACEBOOK_LOGIN = 321;
    CallbackManager callbackManager;
    FirebaseAuth.AuthStateListener authStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);
        RecyclerView rcvChat = findViewById(R.id.rcvChat);
        rcvChat.setHasFixedSize(true);
        db = FirebaseFirestore.getInstance();


       chatRef = db.collection(getString(R.string.chat_ref));

                chatRef.orderBy("currentTime")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        ArrayList<ChatItem> chatItems = new ArrayList<>();
                        if(queryDocumentSnapshots != null)
                        {

                            for (DocumentSnapshot s : queryDocumentSnapshots)
                            {
                                chatItems.add(s.toObject(ChatItem.class));
                                chatItems.size();
                            }
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            rcvChat.setLayoutManager(manager);
                            ChatAdapter adapter = new ChatAdapter(chatItems, getApplicationContext());
                            rcvChat.setAdapter(adapter);
                            rcvChat.smoothScrollToPosition(adapter.getItemCount()-1);
                        }
                    }
                });
                mAuth=FirebaseAuth.getInstance();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(getApplicationContext());*/
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null) {
                    ChatItem item = new ChatItem();
                    item.userImage = user.getPhotoUrl().toString();
                    item.userName = user.getDisplayName();
                    item.messageTxt = edtMessage.getText().toString().trim();
                    item.currentTime = System.currentTimeMillis();
                    edtMessage.setText("");
                    chatRef.add(item);
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        Context context;

        ArrayList<ChatItem> chatItems;
        public ChatAdapter(ArrayList<ChatItem> chatItems , Context context) {

            this.chatItems=chatItems;
            this.context = context;
        }
        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

            ChatItem tmp = chatItems.get(position);
            Glide.with(context)
                    .load(tmp.userImage)
                    .into(holder.userImage);


            holder.userName.setText(tmp.userName);
            holder.messageTxt.setText(tmp.messageTxt);
        }

        @Override
        public int getItemCount() {
            return chatItems.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder{
            ImageView userImage;
            TextView userName;
            TextView messageTxt;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);

                userImage = itemView.findViewById(R.id.user_image);
                userName = itemView.findViewById(R.id.userName);
                messageTxt = itemView.findViewById(R.id.messagetxt);
            }
        }
    }
}

