package com.teachandroid.app.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout chatUsersLinearLayout;
    private ListView  messageList;
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    public static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private MessageReceiver receiverMessage;
    private UserReceiver receiverUser;
    private ChatUsersReceiver receiverChatUsers;
    private Intent intent;
    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        if (intent == null) { return;  }

        message = intent.getParcelableExtra(MessageActivity.EXTRA_MESSAGE);
        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        messageList = (ListView) findViewById(R.id.list_message);
        messageList.setAdapter(messageAdapter);

        fillFieldOfActivity();
        registerAllNeededReceiver();
        fillAndStartService(message);
    }

    private void fillFieldOfActivity() {


        ImageView imageView = (ImageView)findViewById(R.id.image_message_top);
        TextView textOwnerData = (TextView) findViewById(R.id.text_message_owner_data);
        TextView textTitleData = (TextView) findViewById(R.id.text_message_title_data);
        chatUsersLinearLayout = (LinearLayout)findViewById(R.id.horizontal_linear_scroll);

        User user = KnownUsers.getInstance().getUserFromId(message.getUserId());
        ImageLoader.getInstance().displayImage(user.getPhoto100(),imageView);

        textTitleData.setText(message.getTitle());
        textOwnerData.setText(user.getFirstName() + " - " +user.getLastName());

        EditText editText = (EditText)findViewById(R.id.edit_send_message);
        editText.clearFocus();
        Button buttonSend = (Button)findViewById(R.id.button_send_message);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.edit_send_message);
                if (editText.getText().toString().equals("")) {return;}

                Intent intentForGetHistory = new Intent(v.getContext(), ApiFacadeService.class);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.send");
                HashMap<String, String> parameters = new HashMap<String, String>();

                if (message.getChatId() == 0) {
                    parameters.put("user_id", message.getUserIdString());
                } else {
                    parameters.put("chat_id", "" + message.getChatId());
                }
                parameters.put("message", editText.getText().toString());

                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parameters);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.RETURNED_TYPE_NO_RETURN);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, ApiFacadeService.RETURNED_TYPE_NO_RETURN);

                startService(intentForGetHistory);
                editText.setText("");
//                Notification.Builder builder =
//                        new Notification.Builder(v.getContext())
//                                .setContentTitle(getString(R.string.text_send_message_to)+KnownUsers.getUserFromId(message.getUserId()).getFirstName()+" "+KnownUsers.getUserFromId(message.getUserId()).getLastName())
//                                .setContentText(editText.getText().toString())
//                                .setAutoCancel(true);
//                NotificationManager mNotificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.notify(0, builder.getNotification());
            }
        });
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

        Intent intentForGetHistory = new Intent(this, ApiFacadeService.class);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.getHistory");
        HashMap<String, String> parametersForGetHistory = new HashMap<String, String>();
        if (message.getChatId() == 0) {
            parametersForGetHistory.put("user_id", message.getUserIdString());
        } else {
            parametersForGetHistory.put("chat_id", "" + message.getChatId());
        }
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parametersForGetHistory);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, Message.BROADCAST_MESSAGE);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, Message.RETURNED_TYPE_MESSAGE);

        startService(intentForGetHistory);

        if (message.getChatId()!=0) {

            Intent intentForChatUsers = new Intent(this, ApiFacadeService.class);
            intentForChatUsers.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.getChatUsers");
            HashMap<String, String> parametersForChatUsers = new HashMap<String, String>();
            parametersForChatUsers.put("chat_id", ""+message.getChatId());
            parametersForChatUsers.put("fields", "photo_50,photo_100,photo_200");
            intentForChatUsers.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parametersForChatUsers);
            intentForChatUsers.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, Message.BROADCAST_CHAT_USERS);
            intentForChatUsers.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, User.RETURNED_TYPE_USER);

            startService(intentForChatUsers);
        }
    }

    private void registerAllNeededReceiver() {
        receiverMessage = new MessageReceiver();
        registerReceiver(receiverMessage,new IntentFilter(Message.BROADCAST_MESSAGE));
        receiverUser = new UserReceiver();
        registerReceiver(receiverUser,new IntentFilter(KnownUsers.BROADCAST_USER));
        receiverChatUsers = new ChatUsersReceiver();
        registerReceiver(receiverChatUsers,new IntentFilter(Message.BROADCAST_CHAT_USERS));
    }

    private void unRegisterAllNeededReceiver() {
        unregisterReceiver(receiverMessage);
        unregisterReceiver(receiverUser);
        unregisterReceiver(receiverChatUsers);
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            ArrayList<Message> result;
            result = intent.getParcelableArrayListExtra(Message.BROADCAST_MESSAGE);
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

    private class ChatUsersReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            List<Long> listChatUsers = message.getChatActive();
            for (final Long tempId :listChatUsers){
                ImageView tempImageView = new ImageView(chatUsersLinearLayout.getContext());
                chatUsersLinearLayout.addView(tempImageView);
                tempImageView.setPadding(3, 1, 3, 1);
                imageViewList.add(tempImageView);
                tempImageView.setOnClickListener(new View.OnClickListener() {
                    private Long id = tempId;
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),UserActivity.class);
                        intent.putExtra(UserActivity.EXTRA_USER_ID,id);
                        startActivity(intent);
                    }
                });
                ImageLoader.getInstance().displayImage(KnownUsers.getInstance().getUserFromId(tempId).getPhoto50(), imageViewList.get(imageViewList.size()-1));
            }
        }
    }

}