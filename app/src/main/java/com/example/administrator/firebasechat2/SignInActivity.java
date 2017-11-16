package com.example.administrator.firebasechat2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.DialogUtil;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.StringUtil;
import com.example.administrator.firebasechat2.item.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText editSignInEmail;
    private EditText editSignInPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        // 다른 사용자 로그인을 위해 기존 로그인 정보만 설정해준다
        if(PreferenceUtil.getBoolean(this, Const.SP_AUTO_LOGIN)){
            String email = PreferenceUtil.getString(this, Const.SP_EMAIL);
            String password = PreferenceUtil.getString(this, Const.SP_PWD);
            editSignInEmail.setText(email);
            editSignInPassword.setText(password);
        }
    }

    /**
     * 뷰, 파이어베이스 초기화
     */
    private void init() {
        // 뷰
        editSignInEmail = (EditText) findViewById(R.id.editSignInEmail);
        editSignInPassword = (EditText) findViewById(R.id.editSignInPassword);
        database = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(SignInActivity.this);
        // 파이어베이스 데이터베이스, 인증 정보
        userRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * 자동 로그인 기능을 위해 preSignin 분리(외부에서 호출 가능)
     */
    public void signin(View view) {
        final String email = editSignInEmail.getText().toString();
        final String password = editSignInPassword.getText().toString();
        preSignin(email, password);
    }


    /**
     * 로그인
     */
    public void preSignin(final String email, final String password){
        // 프로그래스 다이얼로그 시작
        dialog.show();
        // 1. 서버에 로그인 정보 전달
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 2. 전달된 정보로 가져온 유저 정보가 이메일 인증 처리가 완료됬는지 확인
                        fUser = mAuth.getCurrentUser();
                        if (fUser.isEmailVerified()) {
                            dialog.dismiss();
                            // 3. 로그인 하면서 유저 정보 저장
                            userRef.child(StringUtil.replaceEmailComma(email)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // 3.1 유저 객체를 따로 전역으로 저장
                                    User user = new User();
                                    user.id = (String)dataSnapshot.child("id").getValue();
                                    user.email = (String) dataSnapshot.child("email").getValue();
                                    user.name = (String) dataSnapshot.child("name").getValue();
                                    user.password = (String) dataSnapshot.child("password").getValue();
                                    MyApp.setUser(user);
                                    // 3.2 유저 정보 공유프레퍼런스에 저장
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_NAME, user.name);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_ID, user.id);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_EMAIL, email);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_PWD, password);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_AUTO_LOGIN, true);
                                    // 4. 메인 페이지로 넘어감
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DialogUtil.showDialog("로그인 실패 : " + e.getMessage(), SignInActivity.this, false);
                        dialog.dismiss();
                    }
                });
    }



}
