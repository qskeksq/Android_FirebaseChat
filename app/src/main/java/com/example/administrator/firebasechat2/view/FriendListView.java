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
import com.example.administrator.firebasechat2.adapter.FriendListAdapter;
import com.example.administrator.firebasechat2.item.User;
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

public class FriendListView extends FrameLayout {

    private View view;
    private RecyclerView friendRecycler;
    private FriendListAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String myId;
    private Context context;

    /**
     * 초기화, 인플레이션
     */
    public FriendListView(@NonNull Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_friend_list, this, false);
        // 외부에서 인플레이션 할 경우 반드시 부모에 붙여줘야 한다
        addView(view);
        init();
        setFriendRecycler();
        setListener();
    }

    private void init() {
        friendRecycler = (RecyclerView) view.findViewById(R.id.friendRecycler);

        // 아이디, 데이터베이스 초기화
        myId = StringUtil.replaceEmailComma(PreferenceUtil.getString(context, Const.SP_EMAIL));
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user").child(myId);
    }

    private void setListener(){
        // 내 데이터베이스에서 친구 목록 불러오기
        userRef.child(Const.FRIEND_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter.setDataAndRefresh(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFriendRecycler(){
        adapter = new FriendListAdapter();
        friendRecycler.setAdapter(adapter);
        friendRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
