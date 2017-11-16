# FirbaseChat
- 파이어베이스를 통한 채팅 구현
  - 회원가입
  - 로그인
  - 친구추가
  - 방 추가, 친구 초개
  - 채팅하기

![](https://github.com/qskeksq/FirebaseChat/blob/master/pic/20171106_141007_360x640.jpg)

## 회원가입
- Google 이메일 인증
- 1. 인증 서버에 사용자 생성
- 2. (사용자 생성 성공시) 이메일 발송
- 3. 인증 서버에 사용자 정보 등록
- 4. Realtime Database에 사용자 정보 등록
- 5. 이메일 인증 전송
- 5.1 발송 성공시 로그인 페이지로 넘어감

```java
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
                }
            }
        })
```

## 로그인
- 1. 서버에 로그인 정보 전달
- 2. 전달된 정보로 가져온 유저 정보가 이메일 인증 처리가 완료됬는지 확인
- 3. 로그인 하면서 유저 정보 저장
- 3.1 유저 객체를 따로 전역으로 저장
- 3.2 유저 정보 공유프레퍼런스에 저장(선택)
- 4. 메인 페이지로 넘어감

```java
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
}
```

## 친구 초대하기
- 유저데이터베이스에 이벤트 발생
- 존재할 경우 내 데이터베이스에 추가
- 친구가 추가되었으면 액티비티 종료

```java
// 유저데이터베이스에 이벤트 발생
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
```

## 방 만들기

#### 친구리스트 클릭시 인터페이스를 통해 makeRoomActivity에 선택된 친구 전달

```java
@Override
  public void sendSelectedUsers(User user, boolean isChecked) {
      this.user = user;
      if (isChecked) {
          memberList.add(user);
      } else {
          memberList.remove(user);
      }
  }
```
#### 데이터베이스에 방 만들기

```java
// 방 만들기
roomRef.child(room.id).setValue(room);
// 현재 아이디에 방 아이디 추가
userRef.child(StringUtil.replaceEmailComma(myId)).child(Const.MY_CHAT_ROOM).child(room.id).child("id").setValue(room.id);
// 현재 아이디에 방 이름 추가
userRef.child(StringUtil.replaceEmailComma(myId)).child(Const.MY_CHAT_ROOM).child(room.id).child("title").setValue(room.title);
// 초대된 아이디에 방 이름 추가
for(User user : memberList){
    userRef.child(StringUtil.replaceEmailComma(user.email)).child(Const.MY_CHAT_ROOM).child(room.id).child("id").setValue(room.id);
    userRef.child(StringUtil.replaceEmailComma(user.email)).child(Const.MY_CHAT_ROOM).child(room.id).child("title").setValue(room.title);
}
```

## 채팅하기

#### 메시지 보내기
- 채팅방 데이터베이스에 생성한 메시지 객체 추가

```java
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
```

#### 메시지 리스트 갱신하기

```java
roomRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // 현재 화면에 보여줄 메시지 리스트
        List<Message> messageList = new ArrayList<>();
        // 내 이메일
        String myEmail = PreferenceUtil.getString(RoomActivity.this, Const.SP_EMAIL);
        for(DataSnapshot snapshot : dataSnapshot.child(Const.MESSAGE_LIST).getChildren()){
            // 메시지를 받은 사람에 내 이메일 추가
            roomRef.child(Const.MESSAGE_LIST).child(snapshot.getKey()).child(Const.ROOM_RECEIVED_USERS).child(StringUtil.replaceEmailComma(myEmail)).setValue(myEmail);
            // 상대가 메시지를 읽었는지 확인
            List<String> received_users = new ArrayList<>();
            for(DataSnapshot userSnapshot : snapshot.child("received_users").getChildren()){
                if(userSnapshot.getValue(String.class)!=null)
                received_users.add(userSnapshot.getValue(String.class));
            }
            // 현재 미시지 리스트에 추가하고 갱신
            String id = snapshot.child("id").getValue(String.class);
            String content = snapshot.child("content").getValue(String.class);
            long memberCount = (long) snapshot.child("memberCount").getValue();
            String name = snapshot.child("name").getValue(String.class);
            Message message = new Message();
            message.id = id;
            message.content = content;
            message.memberCount = (int) memberCount;
            message.name = name;
            message.received_users = received_users;
            message.read_count = message.memberCount - (message.received_users.size()-1);
            messageList.add(message);
        }
        adapter.setDataAndRefresh(messageList);
    }
});
```