package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.User;
import com.teachandroid.app.util.SendMessageAndNotification;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends Activity {

    public static String EXTRA_USER_ID = "EXTRA_USER_ID";
    private User user;

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

                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", "" + user.getId());
                SendMessageAndNotification.sendMessageWithService(v, editText.getText().toString(), parameters);

                editText.setText("");

                SendMessageAndNotification.sendNotification(
                        getString(R.string.text_send_message_to) + user.getFirstName() + " " + user.getLastName(),
                        editText.getText().toString());
            }



        });

    }


}
