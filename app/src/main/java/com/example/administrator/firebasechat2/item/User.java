package com.example.administrator.firebasechat2.item;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */
@IgnoreExtraProperties
public class User {

    public String id;
    public String name;
    public String email;
    public String password;
    public String token;
    public String phone_number;
    public String gender;
    public Uri profileUri;
    public Date birthday;
    @Exclude
    public boolean isSelected;

    // 친구 목록
    public List<User> friend_list;
    // 내 채팅방
   public List<Room> my_chat_room;

    public User() {

    }


}
