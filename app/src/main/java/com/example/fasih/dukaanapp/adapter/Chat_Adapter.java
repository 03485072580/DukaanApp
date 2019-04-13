package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Chat_Model;

import java.util.List;


public class Chat_Adapter extends RecyclerView.Adapter {
    private Context context;
    private List<Chat_Model> myNoti;


    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public Chat_Adapter(Context context, List<Chat_Model> myNoti) {
        this.context = context;
        this.myNoti = myNoti;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Chat_Model message = (Chat_Model) myNoti.get(position);

        if (message.getTb_in_out().equals("user")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
//             If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage, image;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        void bind(Chat_Model message) {
            messageText.setText(message.getTb_messaga());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTb_datetime());
            nameText.setText(message.getTb_in_out());
//            Picasso.with(context).load("").placeholder(R.drawable.logo).error(R.drawable.logo).into(profileImage);

//            if (message.getTB_MEDIA_TYPE().equals("M")) {
//                messageText.setVisibility(View.GONE);
//                image.setVisibility(View.VISIBLE);
//                Picasso.with(context).load(message.getTB_IMG()).into(image);
//
//            } else if (message.getTB_MEDIA_TYPE().equals("T")) {
//                messageText.setVisibility(View.VISIBLE);
//                image.setVisibility(View.GONE);
//            }

            // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat_Model message = (Chat_Model) myNoti.get(position);

        Log.d("TYPEVIEW", String.valueOf(holder.getItemViewType()));
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }


    public int getItemCount() {
        return myNoti.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage, image;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        void bind(Chat_Model message) {
            Log.d("TYPEVIEW", String.valueOf(message.getTb_messaga()));
            messageText.setText(message.getTb_messaga());
//            Picasso.with(context).load("").placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(profileImage);
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTb_datetime());

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}


