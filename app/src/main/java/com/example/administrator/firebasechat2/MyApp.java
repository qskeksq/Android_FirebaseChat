package com.example.administrator.firebasechat2;

import android.app.Application;
import android.content.Context;

import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.item.User;

/**
 * Created by Administrator on 2017-11-06.
 */

public class MyApp extends Application {

    private static User mUser;

    public static User getUser(Context context) {
        if(mUser == null){
            mUser = new User();
            mUser.id = PreferenceUtil.getString(context, Const.SP_ID);
            mUser.name = PreferenceUtil.getString(context, Const.SP_NAME);
            mUser.email = PreferenceUtil.getString(context, Const.SP_EMAIL);
            mUser.password = PreferenceUtil.getString(context, Const.SP_PWD);
        }
        return mUser;
    }

    public static void setUser(User user) {
        mUser = user;
    }
}
