package com.example.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private List<Message> mMessages;
    private Activity mActivity;

    public MessageAdapter(Activity context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.mMessages = messages;
        this.mActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message message = getItem(position);
        int layoutResource = 0;
        int viewType = getItemViewType(position);

        if (viewType == 0){
            layoutResource = R.layout.my_message_item;
        } else {
            layoutResource = R.layout.your_message_item;
        }

        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        boolean isText = message.getImageUrl() == null;

        if (isText){
            viewHolder.messageTV.setVisibility(View.VISIBLE);
            viewHolder.photoImg.setVisibility(View.GONE);
            viewHolder.messageTV.setText(message.getText());
        }else {
            viewHolder.messageTV.setVisibility(View.GONE);
            viewHolder.photoImg.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.photoImg.getContext())
                    .load(message.getImageUrl())
                    .into(viewHolder.photoImg);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        int flag;
        Message message = mMessages.get(position);
        if (message.isMine){
            flag = 0;
        } else  {
            flag = 1;
        }
        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolder {

        private TextView messageTV;
        private ImageView photoImg;

        public ViewHolder(View view) {
            photoImg = view.findViewById(R.id.photoImageView);
            messageTV = view.findViewById(R.id.bubbleTV);
        }
    }
}
