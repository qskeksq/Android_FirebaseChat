package com.example.administrator.firebasechat2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFriendActivity extends AppCompatActivity {

    private Toolbar addFriendToolbar;
    private EditText editFriendEmail;

    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private boolean ifExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();
        setToolbar();
    }

    private void init() {
        addFriendToolbar = (Toolbar) findViewById(R.id.addFriendToolbar);
        editFriendEmail = (EditText) findViewById(R.id.editFriendEmail);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
    }

    private void setToolbar(){
        setSupportActionBar(addFriendToolbar);
        addFriendToolbar.setTitleTextColor(getResources().getColor(R.color.color_text_next));
        getSupportActionBar().setTitle("친구 찾기");
    }

    /**
     * 친구 추가
     */
    public void addFriend(View view){

        final String email = editFriendEmail.getText().toString();
        final String myEmail = PreferenceUtil.getString(this, Const.SP_EMAIL);

        // 현재 내 아이디일 경우
        if(email.equals(myEmail)){
            Toast.makeText(AddFriendActivity.this, "내 아이디입니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 데이터베이스에서 전체 친구 목록 가져와서 입력한 아이디 존재하는지 검사
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String friendEmail = StringUtil.recoverEmailComma(snapshot.getKey());
                    // 존재할 경우 내 데이터베이스에 추가
                    if(email.equals(friendEmail)){
                        userRef.child(StringUtil.replaceEmailComma(myEmail)).child("friend_list").child(snapshot.getKey()).setValue(snapshot.getValue());
                        Toast.makeText(AddFriendActivity.this, friendEmail+"님이 추가되었습니다", Toast.LENGTH_SHORT).show();
                        ifExists = true;
                    }
                }
                // 친구가 추가되었으면 액티비티 종료
                if(ifExists){
                    finish();
                } else {
                    Toast.makeText(AddFriendActivity.this, "일치하는 아이디가 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
