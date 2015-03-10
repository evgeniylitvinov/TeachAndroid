package com.teachandroid.app.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;

import java.util.HashMap;

public class UserActivity extends Activity {

    public static String EXTRA_USER_ID = "EXTRA_USER_ID";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        Long userId = intent.getLongExtra(EXTRA_USER_ID,1);
        user = KnownUsers.getUserFromId(userId);
        ImageView imageView = (ImageView)findViewById(R.id.image_user);
        ImageLoader.getInstance().displayImage(user.getPhoto200(),imageView);



        Button buttonSend = (Button)findViewById(R.id.button_send_message);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.edit_send_message);
                if (editText.getText().toString().equals("")) {return;}

                Intent intentForGetHistory = new Intent(v.getContext(), ApiFacadeService.class);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.send");
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", "" + user.getId());
                parameters.put("message", editText.getText().toString());

                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parameters);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.RETURNED_TYPE_NO_RETURN);
                intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, ApiFacadeService.RETURNED_TYPE_NO_RETURN);

                startService(intentForGetHistory);
                editText.setText("");
//                Notification.Builder builder =
//                        new Notification.Builder(v.getContext())
//                                .setContentTitle(getString(R.string.text_send_message_to)+user.getFirstName()+" "+user.getLastName())
//                                .setContentText(editText.getText().toString())
//                                .setAutoCancel(true);
//
//                Intent resultIntent = new Intent(MyApplication.getContext(), UserActivity.class);
//                resultIntent.putExtra(UserActivity.EXTRA_USER_ID,user.getId());
//
//                PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                        MyApplication.getContext(),
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(resultPendingIntent);
//                builder.setAutoCancel(true);
//                NotificationManager mNotificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.notify(0, builder.getNotification());
            }
        });

    }


}
