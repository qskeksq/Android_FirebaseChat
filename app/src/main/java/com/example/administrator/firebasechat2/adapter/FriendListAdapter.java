package com.example.administrator.firebasechat2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.firebasechat2.R;
import com.example.administrator.firebasechat2.item.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.Holder> {

    List<User> userList = new ArrayList<>();
    Context context;

    /**
     * 모든 데이터 함께 변경
     */
    public void setDataAndRefresh(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    /**
     * 기존에 저장된 데이터와 비교해서 변경된 사항만 반영
     */
    public void setItemAndRefresh() {

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = userList.get(position);
        holder.friendName.setText(user.name+"["+user.email+"]");
        Glide.with(context)
                .load(user.profileUri)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.friendImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView friendImage;
        private TextView friendName;

        public Holder(View itemView) {
            super(itemView);
            friendImage = (ImageView) itemView.findViewById(R.id.friendImage);
            friendName = (TextView) itemView.findViewById(R.id.friendName);
        }
    }
}
