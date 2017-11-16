package com.example.administrator.firebasechat2.item;

import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class Message {

    public String id;
    public String name;
    public String content;
    public long time;
    public String sender;
    public int length;
    public int received_count;
    public int read_count;

    public int memberCount;
    public List<String> received_users;

}
