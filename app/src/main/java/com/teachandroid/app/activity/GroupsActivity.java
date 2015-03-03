package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.teachandroid.app.R;
import com.teachandroid.app.data.Audio;
import com.teachandroid.app.data.Group;

import java.io.File;
import java.util.List;

public class GroupsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
    }
    private final class GroupAdapter extends ArrayAdapter<Group> {

        public GroupAdapter(Context context, List<Group> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.groups_list, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_group);
            ImageView artistView = (ImageView) convertView.findViewById(R.id.photo_group);

            Group item = getItem(position);

            titleView.setText(item.getName());

           // ImageSize targetSize = new ImageSize(80, 50);

            ImageLoader.getInstance().displayImage(item.getPhoto(),artistView);
            //artistView.setText(item.getArtist());
            return convertView;
        }
    }


}
