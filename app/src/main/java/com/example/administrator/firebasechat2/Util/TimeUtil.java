package com.example.administrator.firebasechat2.Util;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017-11-05.
 */

public class TimeUtil {

    public static String sdf(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String temp = sdf.format(time);
        String hour = temp.split(":")[1];
        if(Integer.parseInt(hour) < 12){
            temp = "오전"+temp;
        } else {
            temp = "오후"+temp;
        }
        return temp;
    }

}
