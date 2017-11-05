package com.example.administrator.firebasechat2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class SelectFriendListAdapter extends RecyclerView.Adapter<SelectFriendListAdapter.Holder> {

    private List<User> userList = new ArrayList<>();
    private Context context;
    private OnSelectFriendListInterface selectFriendListInterface;

    public SelectFriendListAdapter(OnSelectFriendListInterface selectFriendListInterface) {
        this.selectFriendListInterface = selectFriendListInterface;
    }

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_friend, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.selectFriendText.setText(userList.get(position).name);
        Glide.with(context)
                .load(userList.get(position).profileUri)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.selectFriendImage);
        holder.user = userList.get(position);
        if(userList.get(position).isSelected){
            holder.selectFriendCheck.setChecked(true);
        } else {
            holder.selectFriendCheck.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView selectFriendImage;
        private TextView selectFriendText;
        private CheckBox selectFriendCheck;
        private User user;

        public Holder(View itemView) {
            super(itemView);
            selectFriendImage = (ImageView) itemView.findViewById(R.id.selectFriendImage);
            selectFriendText = (TextView) itemView.findViewById(R.id.selectFriendText);
            selectFriendCheck = (CheckBox) itemView.findViewById(R.id.selectFriendCheck);
            selectFriendCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectFriendListInterface.sendSelectedUsers(user, isChecked);
                }
            });
        }
    }

    public interface OnSelectFriendListInterface{
        void sendSelectedUsers(User user, boolean isChecked);
    }
}
