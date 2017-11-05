package com.example.administrator.firebasechat2.Util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017-11-03.
 */

public class PreferenceUtil {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(Const.SIGN_IN_SP, MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferenceEditor(Context context){
        return sp.edit();
    }

    public static void setValue(Context context, String key, String value){
        sp = getPreference(context);
        editor = getPreferenceEditor(context);
        editor.putString(key, value);
        editor.commit();

    }

    public static void setValue(Context context, String key, boolean value){
        sp = getPreference(context);
        editor = getPreferenceEditor(context);
        editor.putBoolean(key, value);
        editor.commit();

    }

    public static void setValue(Context context, String key, long value){
        sp = getPreference(context);
        editor = getPreferenceEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key){
        sp = getPreference(context);
        return sp.getString(key, "");
    }

    public static long getLong(Context context, String key){
        sp = getPreference(context);
        return sp.getLong(key, 0);
    }

    public static boolean getBoolean(Context context, String key){
        sp = getPreference(context);
        return sp.getBoolean(key, false);
    }
}
