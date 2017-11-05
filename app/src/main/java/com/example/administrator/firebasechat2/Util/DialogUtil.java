package com.example.administrator.firebasechat2.Util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017-11-03.
 */

public class DialogUtil {


    public static void showDialog(String message, final Activity activity, final boolean finish){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Notice");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(finish){
                    activity.finish();
                }
            }
        });
        builder.show();
    }

}
