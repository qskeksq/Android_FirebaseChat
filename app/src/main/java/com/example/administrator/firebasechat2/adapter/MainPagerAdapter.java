package com.example.administrator.firebasechat2.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.firebasechat2.view.AccountView;
import com.example.administrator.firebasechat2.view.FriendListView;
import com.example.administrator.firebasechat2.view.RoomListView;
import com.example.administrator.firebasechat2.view.TimeLineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class MainPagerAdapter extends PagerAdapter {

    List<View> pages = new ArrayList<>();
    Context context;

    public MainPagerAdapter(Context context) {
        this.context = context;
        setPages();
    }

    private void setPages(){
        FriendListView friendListView = new FriendListView(context);
        RoomListView roomListView = new RoomListView(context);
        TimeLineView timeLineView = new TimeLineView(context);
        AccountView accountView = new AccountView(context);

        pages.add(friendListView);
        pages.add(roomListView);
        pages.add(timeLineView);
        pages.add(accountView);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = pages.get(position);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
