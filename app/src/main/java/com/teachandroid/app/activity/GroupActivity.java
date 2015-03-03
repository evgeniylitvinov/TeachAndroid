package com.teachandroid.app.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends ActionBarActivity {

    private GroupAdapter groupAdapter;

    private ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupAdapter = new GroupAdapter(this, new ArrayList<Group>());

        groupList = (ListView) findViewById(R.id.list_group);

        groupList.setAdapter(groupAdapter);

        ApiFacade facade = new ApiFacade(this);
        facade.getGroup(new SimpleResponseListener<List<Group>>() {
            @Override
            public void onResponse(final List<Group> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        groupAdapter.addAll(response);
                    }
                });
            }
        });




    }

    private static final class GroupAdapter extends ArrayAdapter<Group> {

        public GroupAdapter(Context context, List<Group> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.list_item_group, null);
            }
            TextView groupNameView = (TextView) convertView.findViewById(R.id.text_group_name);
            TextView groupScreenNameView = (TextView) convertView.findViewById(R.id.text_group_screen_name);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_group);

            Group item = getItem(position);

            groupNameView.setText(""+item.getName());
            groupScreenNameView.setText(""+item.getScreenName());

            ImageLoader.getInstance().displayImage(item.getPhoto_50(), imageView); // Запустили асинхронный показ картинки
            return convertView;
        }
    }
}
