package com.example.said.villefuteeips2017;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by idriss on 1/27/18.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message,parent, false);
        }

        MessageAdapter.MessageViewHolder viewHolder = (MessageAdapter.MessageViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MessageAdapter.MessageViewHolder();
            viewHolder.sender = (TextView) convertView.findViewById(R.id.message_sender);
            viewHolder.content = (TextView) convertView.findViewById(R.id.message_content);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<>
        Message message = getItem(position);
        viewHolder.sender.setText(message.getSender());
        viewHolder.content.setText(message.getMessage());

        return convertView;
    }
    private class MessageViewHolder{
        public TextView sender;
        public TextView content;
        public LinearLayout manageLayout;

    }
}
