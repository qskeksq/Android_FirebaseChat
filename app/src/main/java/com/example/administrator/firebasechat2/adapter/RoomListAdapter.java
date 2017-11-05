package com.example.administrator.firebasechat2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.firebasechat2.R;
import com.example.administrator.firebasechat2.RoomActivity;
import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.item.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-03.
 */

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.Holder> {

    List<Room> roomList = new ArrayList<>();
    Context context;

    /**
     * 모든 데이터 함께 변경
     */
    public void setDataAndRefresh(List<Room> roomList) {
        this.roomList = roomList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.roomName.setText(roomList.get(position).title);
        holder.roomId = roomList.get(position).id;
//        Glide.with(context)
//                .load(roomList.get(position).members.get(position).profileUri)
//                .placeholder(R.mipmap.ic_launcher_round)
//                .into(holder.roomImage);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView roomImage;
        private TextView roomName;
        private String roomId;

        public Holder(View itemView) {
            super(itemView);
            roomImage = (ImageView) itemView.findViewById(R.id.roomImage);
            roomName = (TextView) itemView.findViewById(R.id.roomName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RoomActivity.class);
                    intent.putExtra(Const.ROOM_ID, roomId);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
