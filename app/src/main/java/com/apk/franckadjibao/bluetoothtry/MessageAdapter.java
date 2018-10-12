package com.apk.franckadjibao.bluetoothtry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<String> {
    private List<String> message;
    Context context;
    LayoutInflater inflater;

    public MessageAdapter(Context c, List<String> msgText){
        super(c,R.layout.message_layout,R.id.name,msgText);
        context=c;
        message=msgText;
    }

    class ViewHolder{
        TextView receiveText;
        TextView sendText;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.message_layout,null);
        }
        ViewHolder vh= new ViewHolder();
        vh.receiveText=(TextView)convertView.findViewById(R.id.textBoxReceived);
        vh.sendText=(TextView)convertView.findViewById(R.id.mtextBox);


        return super.getView(position, convertView, parent);
    }
}
