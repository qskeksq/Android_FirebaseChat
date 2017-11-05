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

        if(PreferenceUtil.getBoolean(this, Const.SP_AUTO_LOGIN)){
            String email = PreferenceUtil.getString(this, Const.SP_EMAIL);
            String password = PreferenceUtil.getString(this, Const.SP_PWD);
            editSignInEmail.setText(email);
            editSignInPassword.setText(password);
//            preSignin(email, password);
        } else {

        }
    }

    private void init() {
        editSignInEmail = (EditText) findViewById(R.id.editSignInEmail);
        editSignInPassword = (EditText) findViewById(R.id.editSignInPassword);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(SignInActivity.this);
    }

    public void preSignin(final String email, final String password){
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        fUser = mAuth.getCurrentUser();
                        if (fUser.isEmailVerified()) {
                            dialog.dismiss();
                            // 로그인 하면서 유저 정보까지 불러온다
                            userRef.child(StringUtil.replaceEmailComma(email)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = new User();
                                    user.id = (String)dataSnapshot.child("id").getValue();
                                    user.email = (String) dataSnapshot.child("email").getValue();
                                    user.name = (String) dataSnapshot.child("name").getValue();

                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_NAME, user.name);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_EMAIL, email);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_PWD, password);
                                    PreferenceUtil.setValue(getBaseContext(), Const.SP_AUTO_LOGIN, true);
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

//                            PreferenceUtil.setValue(getBaseContext(), Const.SP_EMAIL, email);
//                            PreferenceUtil.setValue(getBaseContext(), Const.SP_PWD, password);
//                            PreferenceUtil.setValue(getBaseContext(), Const.SP_AUTO_LOGIN, true);
//                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                        } else {
                            DialogUtil.showDialog("인증이 처리되지 않았습니다", SignInActivity.this, false);
                            dialog.dismiss();
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

    public void signin(View view) {
        final String email = editSignInEmail.getText().toString();
        final String password = editSignInPassword.getText().toString();
        preSignin(email, password);
    }


}
