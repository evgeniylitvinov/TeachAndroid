package com.teachandroid.app.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends ActionBarActivity {

    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //imageLoaderInit(getApplication());


        photoAdapter = new PhotoAdapter(this, new ArrayList<Photo>());
        GridView photoGrid = (GridView) findViewById(R.id.photo_view);
        photoGrid.setAdapter(photoAdapter);

        ApiFacade facade = new ApiFacade(this);

        facade.getPhoto(new SimpleResponseListener<List<Photo>>() {
            @Override
            public void onResponse(final List<Photo> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photoAdapter.addAll(response);
                    }
                });
            }
        });
    }


    final class PhotoAdapter extends ArrayAdapter<Photo> {

        public PhotoAdapter(Context context, List<Photo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.grid_item_photo, null);
            }

            ImageView image100 = (ImageView) convertView.findViewById(R.id.imageviewPhoto);


            Photo photo = getItem(position);

            ImageLoader.getInstance().displayImage(photo.getPhoto75(), image100);

            return convertView;
        }
    }
}
