package com.example.administrator.firebasechat2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firebasechat2.Util.StringUtil;
import com.example.administrator.firebasechat2.item.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private AppCompatEditText editEmail;
    private Button btnNext;
    private Button btnFacebook;
    private Button btnGoogle;
    private AppCompatEditText editName;
    private EditText editPassword;
    private EditText editSecondPwd;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private boolean isValidEmail, isValidPassword, isValidSecondPassword, isValidName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        setListener();
    }

    /**
     * 뷰, 파이어베이스 초기화
     */
    private void init() {
        // 뷰 초기화
        editEmail = (AppCompatEditText) findViewById(R.id.editEmail);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        editName = (AppCompatEditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editSecondPwd = (EditText) findViewById(R.id.editSecondPwd);
        // 파이어베이스 데이터베이스, 인증정보 초기화
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        
    }


    /**
     * 유효값 확인
     */
    private void setListener(){
        // 이름
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidName =  StringUtil.isValidName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidName){
                    editName.setTextColor(getResources().getColor(R.color.color_friend_text));
                } else {
                    editName.setTextColor(getResources().getColor(R.color.color_button_next));
                }
            }
        });
        // 이메일
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidEmail = StringUtil.isValidEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidEmail){
                    editEmail.setTextColor(getResources().getColor(R.color.color_friend_text));
                } else {
                    editEmail.setTextColor(getResources().getColor(R.color.color_button_next));
                }
            }
        });
        // 1차 비밀번호
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidPassword = StringUtil.isValidPassword(s.toString());
                isValidSecondPassword = StringUtil.isValidSecondPassword(editSecondPwd.getText().toString(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidPassword){
                    editPassword.setTextColor(getResources().getColor(R.color.color_friend_text));
                } else {
                    editPassword.setTextColor(getResources().getColor(R.color.color_button_next));
                }

                if(!isValidSecondPassword){
                    editSecondPwd.setTextColor(getResources().getColor(R.color.color_friend_text));
                } else {
                    editSecondPwd.setTextColor(getResources().getColor(R.color.color_button_next));
                }
            }
        });
        // 2차 비밀번호
        editSecondPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidSecondPassword = StringUtil.isValidSecondPassword(editPassword.getText().toString(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidSecondPassword){
                    editSecondPwd.setTextColor(getResources().getColor(R.color.color_friend_text));
                } else {
                    editSecondPwd.setTextColor(getResources().getColor(R.color.color_button_next));
                }
            }
        });
    }

    /**
     * 유효값 확인하고 메인으로 넘어감
     */
    public void signup(View view){

        if(!isValidName){
            Toast.makeText(this, "이름을 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmail){
            Toast.makeText(this, "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPassword){
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidSecondPassword){
            Toast.makeText(this, "2차 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. 인증 서버에 사용자 생성
        mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mUser = mAuth.getCurrentUser();
                        // 2.1 (사용자 생성 성공시) 이메일 발송
                        if(task.isSuccessful()){
                            // 3. 인증 서버에 사용자 정보 등록
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            UserProfileChangeRequest.Builder profile = new UserProfileChangeRequest.Builder();
                            profile.setDisplayName(editName.getText().toString());
                            fUser.updateProfile(profile.build());
                            // 4. Realtime Database에 사용자 정보 등록
                            User user = new User();
                            user.id = mUser.getUid();
                            user.name = editName.getText().toString();
                            user.email = editEmail.getText().toString();
                            user.password = editPassword.getText().toString();
                            // 키 값(qskeksq@hanmail_comma_net)으로 데이터베이스에 저장
                            userRef.child(StringUtil.replaceEmailComma(user.email)).setValue(user);

                            // 5. 이메일 인증 전송
                            mUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // 5.1 발송 성공시
                                            if(task.isSuccessful()){
                                                // getBaseContext()와 this 의 중요한 차이!!
                                                // Context                      <- getBaseContext() 리턴값
                                                //   - ContextWrapper           <- 테마 값들이 필요한 경우가 있다!
                                                //      - ThemeContext
                                                //         - AppCompatActivty
                                                //            - ..
                                                // 가입하고 바로 로그인 페이지로 넘어간다
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                                builder.setTitle("Notice");
                                                builder.setMessage("이메일을 발송했습니다");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                        startActivity(intent);
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        }
                                    })
                                    // 5.2 이메일 전송 실패
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "이메일 발송 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                // 2.2 사용자 정보 실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "사용자 생성 실패 : "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
