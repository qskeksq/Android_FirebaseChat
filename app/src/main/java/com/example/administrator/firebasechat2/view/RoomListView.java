package com.example.administrator.firebasechat2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.administrator.firebasechat2.R;
import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.StringUtil;
import com.example.administrator.firebasechat2.adapter.RoomListAdapter;
import com.example.administrator.firebasechat2.item.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-02.
 */

public class RoomListView extends FrameLayout {

    private View view;
    private RecyclerView roomRecycler;
    private RoomListAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String myId;
    private Context context;

    public RoomListView(@NonNull Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_room_list, this, false);
        addView(view); // 외부에서 인플레이션 할 경우 반드시 부모에 붙여줘야 한다
        init();
        setFriendRecycler();
        setListener();
    }

    private void init() {
        roomRecycler = (RecyclerView) view.findViewById(R.id.roomRecycler);

        myId = StringUtil.replaceEmailComma(PreferenceUtil.getString(context, Const.SP_EMAIL));
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user").child(myId);
    }

    private void setListener() {
        userRef.child(Const.MY_CHAT_ROOM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Room> roomList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = new Room();
                    room.id = snapshot.getKey();
                    room.title = (String) snapshot.child("title").getValue();
                    roomList.add(room);
                }
                adapter.setDataAndRefresh(roomList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFriendRecycler() {
        adapter = new RoomListAdapter();
        roomRecycler.setAdapter(adapter);
        roomRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
