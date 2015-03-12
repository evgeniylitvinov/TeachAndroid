package com.teachandroid.app.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Audio;

import java.util.ArrayList;
import java.util.List;


public class AudioActivity extends ActionBarActivity {

    private AudioAdapter audioAdapter;

    private ListView audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        audioAdapter = new AudioAdapter(this, new ArrayList<Audio>());

        audioList = (ListView) findViewById(R.id.list_audio);

        audioList.setAdapter(audioAdapter);

        ApiFacade facade = new ApiFacade(this);
        facade.getAudio(new SimpleResponseListener<List<Audio>>(){
            @Override
            public void onResponse(final List<Audio> response) {
                super.onResponse(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioAdapter.addAll(response);
                    }
                });

            }
        });
    }


    private static final class AudioAdapter extends ArrayAdapter<Audio>{

        public AudioAdapter(Context context, List<Audio> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.list_item_audio, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_title);
            TextView artistView = (TextView) convertView.findViewById(R.id.text_artist);

            Audio item = getItem(position);

            titleView.setText(item.getTitle());
            artistView.setText(item.getArtist());
            return convertView;
        }
    }
}
