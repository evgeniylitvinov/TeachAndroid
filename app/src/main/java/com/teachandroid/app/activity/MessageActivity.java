package com.teachandroid.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;
import com.teachandroid.app.api.ApiFacadeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends ActionBarActivity {

    private MessageAdapter messageAdapter;
    private ListView  messageList;

    private static String BROADCAST_MESSAGE = "BROADCAST_MESSAGE";
    private MessageReceiver receiverMessage;
    private UserReceiver receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        if (intent == null) { return;  }
        Message message = intent.getParcelableExtra(Message.EXTRA_MESSAGE);
        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        messageList = (ListView) findViewById(R.id.list_message);
        messageList.setAdapter(messageAdapter);

        registerAllNeededReceiver();
        fillAndStartService(message);

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

    @Override
    protected void onDestroy() {
        unRegisterAllNeededReceiver();
        super.onDestroy();
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

    private void fillAndStartService(Message message) {

        Intent intent = new Intent(this,ApiFacadeService.class);
        intent.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND,"messages.getHistory");
        HashMap<String,String> parameters = new HashMap<String, String>();
        parameters.put("user_id", message.getUserIdString());
        parameters.put("chat_id", message.getChatId());
        intent.putExtra(ApiFacadeService.EXTRA_PARAMETERS,parameters);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE,BROADCAST_MESSAGE);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,Message.class.getName());

        startService(intent);
    }

    private void registerAllNeededReceiver() {
        receiverMessage = new MessageReceiver();
        registerReceiver(receiverMessage,new IntentFilter(BROADCAST_MESSAGE));
        receiverUser = new UserReceiver();
        registerReceiver(receiverUser,new IntentFilter(KnownUsers.BROADCAST_USER));
    }

    private void unRegisterAllNeededReceiver() {
        unregisterReceiver(receiverMessage);
        unregisterReceiver(receiverUser);
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            ArrayList<Message> result;
            result = intent.getParcelableArrayListExtra(BROADCAST_MESSAGE);
            messageAdapter.addAll(result);
            messageAdapter.notifyDataSetChanged();
        }
    }
    private class UserReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageAdapter.notifyDataSetChanged();
        }
    }

}