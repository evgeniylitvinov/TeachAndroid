package com.teachandroid.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends ActionBarActivity {

    private MessageAdapter messageAdapter;

    private ListView  messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        if (intent == null) { return;  }
        Message message = (Message) intent.getParcelableExtra(Message.EXTRA_MESSAGE);
        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        messageList = (ListView) findViewById(R.id.list_message);
        messageList.setAdapter(messageAdapter);
        ApiFacade facade = new ApiFacade(this);
        facade.getMessage( message, new SimpleResponseListener<List<Message>>() {
            @Override
            public void onResponse(final List<Message> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.addAll(response);
                    }
                });
            }
        });

        ImageView imageView = (ImageView)findViewById(R.id.image_message_top);
        TextView textOwnerData = (TextView) findViewById(R.id.text_message_owner_data);
        TextView textTitleData = (TextView) findViewById(R.id.text_message_title_data);

        textTitleData.setText(message.getTitle());
        User user = KnownUsers.getInstance().getUserFromId(message.getUserId());
        if (user!=null) {
            ImageLoader.getInstance().displayImage(user.getPhoto200(),imageView);
            textOwnerData.setText(user.getFirstName() + " - " +user.getLastName());
        }
    }

    private static final class MessageAdapter extends ArrayAdapter<Message> {

        public MessageAdapter(Context context, List<Message> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_message, null);
            }
            TextView textOwnerData = (TextView) convertView.findViewById(R.id.text_message_owner_data);
            TextView messageBodyData = (TextView) convertView.findViewById(R.id.text_message_body_data);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_message);

            Message message = getItem(position);

            User user = KnownUsers.getInstance().getUserFromId(message.getFromId());
            if (user!=null) {
                ImageLoader.getInstance().displayImage(user.getPhoto50(),imageView);
                textOwnerData.setText(user.getFirstName() + " - " +user.getLastName());
            }
            messageBodyData.setText("" + message.getBody());
            return convertView;
        }
    }
}