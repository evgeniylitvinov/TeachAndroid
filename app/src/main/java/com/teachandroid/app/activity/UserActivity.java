package com.teachandroid.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;
import com.teachandroid.app.util.SendMessageAndNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends ActionBarActivity {

    public static String EXTRA_USER_ID = "EXTRA_USER_ID";
    private static User user;
    private List<Long> knownUsersList = new ArrayList<Long>();

    private ListView drawerList;
    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        Intent intent = getIntent();
        Long userId = intent.getLongExtra(EXTRA_USER_ID,1);
        Bundle sendBundle = new Bundle();
        sendBundle.putString(EXTRA_USER_ID,""+userId);
        Fragment openFragment = new UserFragment();
        openFragment.setArguments(sendBundle);

        knownUsersList.addAll(KnownUsers.getAllKnownUsers());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, openFragment)
                    .commit();
        }

        UserAdapter tempAdapter =  new UserAdapter(this, knownUsersList);
        drawerList.setAdapter(tempAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long sendId = (Long) parent.getItemAtPosition(position);
                Bundle sendBundle = new Bundle();
                sendBundle.putString(EXTRA_USER_ID,""+sendId);
                Fragment openFragment = new UserFragment();
                openFragment.setArguments(sendBundle);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, openFragment)
                        .commit();

                drawerLayout.closeDrawer(drawerList);
            }
        });

    }

    public static class UserFragment extends Fragment{
        public  UserFragment ( ) {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            final View userView = inflater.inflate(R.layout.activity_user, container, false);
            final Long userId = Long.parseLong(getArguments().getString(EXTRA_USER_ID));
            user = KnownUsers.getUserFromId(userId);
            ImageView imageView = (ImageView)userView.findViewById(R.id.image_user);
            ImageLoader.getInstance().displayImage(user.getPhoto200(),imageView);

            Button buttonSend = (Button)userView.findViewById(R.id.button_send_message);
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText)userView.findViewById(R.id.edit_send_message);
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
            return userView;
        }
    }

    private static final class UserAdapter extends ArrayAdapter<Long> {
        public UserAdapter(Context context, List<Long> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_friend, null);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_image100);

            ImageLoader.getInstance().displayImage(KnownUsers.getInstance().getUserFromId(getItem(position)).getPhoto100(), imageView);
            return convertView;
        }
    }




}
