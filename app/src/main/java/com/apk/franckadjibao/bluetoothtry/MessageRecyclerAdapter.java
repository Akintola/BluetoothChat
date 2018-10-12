package com.apk.franckadjibao.bluetoothtry;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Franck ADJIBAO on 06/05/2017.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder>{
    final int ME=1,OTHER=2;
    ArrayList<Message> messageList=new ArrayList<Message>();

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.contentTextView.setText(messageList.get(position).getContent());
        holder.horaireTv.setText(messageList.get(position).getHoraire());

        if(messageList.get(position).getIdenvoye()== ME){
            holder.messageParent.setGravity(Gravity.START);
        }else{
            holder.messageParent.setGravity(Gravity.END);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message m){
        messageList.add(m);
        this.notifyDataSetChanged();
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView contentTextView;
        RelativeLayout messageParent;
        TextView horaireTv;

        public MessageViewHolder(View itemView){
            super(itemView);
            contentTextView=(TextView)itemView.findViewById(R.id.messageTv);
            horaireTv=(TextView)itemView.findViewById(R.id.timeTv);
            messageParent=(RelativeLayout)itemView.findViewById(R.id.msgParent);
        }

    }
}
