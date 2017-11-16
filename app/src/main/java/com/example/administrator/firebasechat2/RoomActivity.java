package com.example.administrator.firebasechat2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.StringUtil;
import com.example.administrator.firebasechat2.adapter.MessageListAdapter;
import com.example.administrator.firebasechat2.item.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private ImageView messageIcon;
    private Toolbar roomToolbar;
    private ImageView sendMessage;
    private EditText editMessage;
    private RecyclerView roomRecycler;
    private MessageListAdapter adapter;
    private int memberCount = 0;

    private FirebaseDatabase database;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        setToolbar();
        setRoomRecycler();
        setListener();
    }

    private void init() {
        String roomId = getIntent().getStringExtra(Const.ROOM_ID);

        messageIcon = (ImageView) findViewById(R.id.messageIcon);
        roomToolbar = (Toolbar) findViewById(R.id.roomToolbar);
        sendMessage = (ImageView) findViewById(R.id.sendMessage);
        editMessage = (EditText) findViewById(R.id.editMessage);
        roomRecycler = (RecyclerView) findViewById(R.id.roomRecycler);

        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference(Const.CHAT_ROOM).child(roomId);
    }

    private void setToolbar(){
        setSupportActionBar(roomToolbar);
        roomToolbar.setTitleTextColor(getResources().getColor(R.color.color_text_next));
        getSupportActionBar().setTitle("채팅방");
    }

    private void setRoomRecycler(){
        adapter = new MessageListAdapter(this);
        roomRecycler.setAdapter(adapter);
        roomRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListener(){
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message newMsg = new Message();
                newMsg.id = PreferenceUtil.getString(RoomActivity.this, Const.SP_EMAIL);
                newMsg.name = PreferenceUtil.getString(RoomActivity.this, Const.SP_NAME);
                newMsg.content = editMessage.getText().toString();
                newMsg.time = System.currentTimeMillis();
                newMsg.length = newMsg.content.length();
                newMsg.memberCount = memberCount+1;

                roomRef.child(Const.MESSAGE_LIST).child(newMsg.time+"").setValue(newMsg);
                editMessage.setText("");
            }
        });

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messageList = new ArrayList<>();
                String myEmail = PreferenceUtil.getString(RoomActivity.this, Const.SP_EMAIL);
                for(DataSnapshot snapshot : dataSnapshot.child(Const.MESSAGE_LIST).getChildren()){
//                    Message message = snapshot.getValue(Message.class);
                    roomRef.child(Const.MESSAGE_LIST).child(snapshot.getKey()).child(Const.ROOM_RECEIVED_USERS).child(StringUtil.replaceEmailComma(myEmail)).setValue(myEmail);
                    String id = snapshot.child("id").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);
                    long memberCount = (long) snapshot.child("memberCount").getValue();
                    String name = snapshot.child("name").getValue(String.class);
                    List<String> received_users = new ArrayList<>();
                    for(DataSnapshot userSnapshot : snapshot.child("received_users").getChildren()){
                        if(userSnapshot.getValue(String.class)!=null)
                        received_users.add(userSnapshot.getValue(String.class));
                    }
                    Message message = new Message();
                    message.id = id;
                    message.content = content;
                    message.memberCount = (int) memberCount;
                    message.name = name;
                    message.received_users = received_users;
                    message.read_count = message.memberCount - (message.received_users.size()-1);
                    messageList.add(message);
                    Log.e("호출됨", "호출됨");
                }
                adapter.setDataAndRefresh(messageList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
