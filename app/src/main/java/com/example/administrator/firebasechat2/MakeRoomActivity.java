package com.example.administrator.firebasechat2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.StringUtil;
import com.example.administrator.firebasechat2.adapter.SelectFriendListAdapter;
import com.example.administrator.firebasechat2.item.Room;
import com.example.administrator.firebasechat2.item.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakeRoomActivity extends AppCompatActivity implements SelectFriendListAdapter.OnSelectFriendListInterface {

    private Toolbar makeRoomToolbar;
    private SearchView searchView;
    private TextView textView;
    private RecyclerView makeRoomRecycler;
    private SelectFriendListAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference roomRef;
    private DatabaseReference userRef;

    private User user;
    private List<User> memberList;
    private String myId;
    private TextView txtFriendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);
        init();
        setRecycler();
        setListener();
        setToolbar();
    }

    private void init() {
        txtFriendCount = (TextView) findViewById(R.id.txtFriendCount);
        makeRoomToolbar = (Toolbar) findViewById(R.id.makeRoomToolbar);
        searchView = (SearchView) findViewById(R.id.searchView);
        textView = (TextView) findViewById(R.id.txtFriendCount);
        makeRoomRecycler = (RecyclerView) findViewById(R.id.makeRoomRecycler);

        memberList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference(Const.CHAT_ROOM);

        myId = StringUtil.replaceEmailComma(PreferenceUtil.getString(this, Const.SP_EMAIL));
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
    }

    private void setToolbar() {
        setSupportActionBar(makeRoomToolbar);
        makeRoomToolbar.setTitleTextColor(getResources().getColor(R.color.color_selected_tab));
        getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_select_friend));
    }

    private void setRecycler() {
        adapter = new SelectFriendListAdapter(this);
        makeRoomRecycler.setAdapter(adapter);
        makeRoomRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListener() {
        userRef.child(myId).child(Const.FRIEND_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                txtFriendCount.setText("친구"+userList.size()+"명");
                adapter.setDataAndRefresh(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void makeRoom(View view) {

        if (memberList.size() == 0) {
            Toast.makeText(this, "선택된 친구가 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Room room = new Room();
        room.id = roomRef.push().getKey();
        room.members = memberList;
        room.title = room.toStringFriend();

        roomRef.child(room.id).setValue(room);


        userRef.child(StringUtil.replaceEmailComma(myId)).child(Const.MY_CHAT_ROOM).child(room.id).child("id").setValue(room.id);
        userRef.child(StringUtil.replaceEmailComma(myId)).child(Const.MY_CHAT_ROOM).child(room.id).child("title").setValue(room.title);

        for(User user : memberList){
            userRef.child(StringUtil.replaceEmailComma(user.email)).child(Const.MY_CHAT_ROOM).child(room.id).child("id").setValue(room.id);
            userRef.child(StringUtil.replaceEmailComma(user.email)).child(Const.MY_CHAT_ROOM).child(room.id).child("title").setValue(room.title);
        }
        finish();
    }

    @Override
    public void sendSelectedUsers(User user, boolean isChecked) {
        this.user = user;
        if (isChecked) {
            memberList.add(user);
        } else {
            memberList.remove(user);
        }
    }

}
