package com.example.administrator.firebasechat2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.firebasechat2.R;
import com.example.administrator.firebasechat2.Util.Const;
import com.example.administrator.firebasechat2.Util.PreferenceUtil;
import com.example.administrator.firebasechat2.Util.TimeUtil;
import com.example.administrator.firebasechat2.item.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-05.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.Holder> {

    private List<Message> messageList = new ArrayList<>();
    private Context context;
    private int viewType;

    public MessageListAdapter(Context context) {
        this.context = context;
    }

    /**
     * 초기 데이터 세팅
     */
    public void setDataAndRefresh(List<Message> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    /**
     * 메시지가 추가될 경우
     */
    public void addMessageAndRefresh(Message message){
        messageList.add(message);
        notifyItemInserted(messageList.size()-1);
    }

    @Override
    public int getItemViewType(int position) {
        // 내가 작성한 메시지
        if(messageList.get(position).id.equals(PreferenceUtil.getString(context, Const.SP_EMAIL))){
            return Const.MY_MESSAGE;
        } else {
            // 남이 작성한 메시지
            return Const.OTHER_MESSAGE;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case Const.MY_MESSAGE:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
                break;
            case Const.OTHER_MESSAGE:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
                break;
        }
        this.viewType = viewType;
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Message message = messageList.get(position);
        holder.message.setText(message.content);
        holder.time.setText(TimeUtil.sdf(message.time));
        if(message.read_count>-1){
            holder.readCount.setText(message.read_count+"");
        } else {
            holder.readCount.setText("0");
        }
        if(!messageList.get(position).id.equals(PreferenceUtil.getString(context, Const.SP_EMAIL))){
            Log.e("메시지 확인", "메시지 : "+message.name);
            holder.name.setText(message.name);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView message;
        TextView time;
        TextView readCount;
        TextView name;

        public Holder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case Const.MY_MESSAGE:
                    message = (TextView) itemView.findViewById(R.id.chatRightMessage);
                    time = (TextView) itemView.findViewById(R.id.chatRightTime);
                    readCount = (TextView) itemView.findViewById(R.id.chatRightCount);
                    break;
                case Const.OTHER_MESSAGE:
                    name = (TextView) itemView.findViewById(R.id.chatLeftName);
                    profile = (ImageView) itemView.findViewById(R.id.chatLeftImage);
                    message = (TextView) itemView.findViewById(R.id.chatLeftMessage);
                    time = (TextView) itemView.findViewById(R.id.chatLeftTime);
                    readCount = (TextView) itemView.findViewById(R.id.chatLeftCount);
                    break;
            }
        }
    }
}
