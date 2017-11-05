package com.example.administrator.firebasechat2.Util;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017-11-05.
 */

public class TimeUtil {

    public static String sdf(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time);
    }

}
