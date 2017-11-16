package com.example.administrator.firebasechat2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.administrator.firebasechat2.R;

/**
 * Created by Administrator on 2017-11-02.
 */

public class AccountView extends FrameLayout {

    private Context context;

    public AccountView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_account, this, false);
        addView(view);
    }

}
