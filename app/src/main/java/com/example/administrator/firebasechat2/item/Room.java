package com.example.administrator.firebasechat2.item;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */
@IgnoreExtraProperties
public class Room {

    public String id;
    public String title;
    public Message last_msg;
    public int member_count;
    public long created_time;
    public long last_msg_time;
    public int msg_count;


    // 메시지 리스트
    public List<Message> message_list;
    // 방에 참여하는 멤머
    public List<User> members;

    @Exclude
    public String toStringFriend(){
        String result = "";
        for (int i = 0; i < members.size(); i++) {
            result += members.get(i).name;
            if(i==members.size()-1){
                return result;
            }
            result += ",";

        }
        return result;
    }




}
