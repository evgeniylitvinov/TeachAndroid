package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.User;

public class UserActivity extends Activity {

    public static String EXTRA_USER_ID = "EXTRA_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        Long userId = intent.getLongExtra(EXTRA_USER_ID,1);
        User user = KnownUsers.getUserFromId(userId);
        ImageView imageView = (ImageView)findViewById(R.id.image_user);
        ImageLoader.getInstance().displayImage(user.getPhoto200(),imageView);

    }


}
