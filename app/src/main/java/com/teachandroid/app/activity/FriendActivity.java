package com.teachandroid.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Friend;
import com.teachandroid.app.data.Video;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends ActionBarActivity {

    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        ListView friendList;
        friendAdapter = new FriendAdapter(this, new ArrayList<Friend>());
        friendList = (ListView) findViewById(R.id.friend_listview);
        friendList.setAdapter(friendAdapter);

        ApiFacade facade = new ApiFacade(this);

        facade.getFriends(new SimpleResponseListener<List<Friend>>() {
            @Override
            public void onResponse(final List<Friend> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        friendAdapter.addAll(response);
            }
        });
            }
        });
    }


    public static final class FriendAdapter extends ArrayAdapter<Friend> {

        public FriendAdapter(Context context, List<Friend> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_friend, null);
            }

            ImageView image100 = (ImageView) convertView.findViewById(R.id.imageview_image100);
            TextView idView = (TextView) convertView.findViewById(R.id.text_user_id);
            TextView firstNameView = (TextView) convertView.findViewById(R.id.text_user_first_name);
            TextView lastNameView = (TextView) convertView.findViewById(R.id.text_user_last_name);

            Friend friend = getItem(position);

            ImageLoader.getInstance().displayImage(friend.getPhoto100(), image100);
            idView.setText(Long.toString(friend.getUserId()));
            firstNameView.setText(friend.getFirstName());
            lastNameView.setText(friend.getLastName());

            return convertView;
        }
    }
}
