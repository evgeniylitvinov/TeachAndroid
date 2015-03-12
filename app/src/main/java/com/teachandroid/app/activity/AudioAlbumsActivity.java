package com.teachandroid.app.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.AudioAlbum;

import java.util.ArrayList;
import java.util.List;

public class AudioAlbumsActivity extends Activity{

    private GridView albumsListView;

    private AudioAlbumAdapter adapter;

    private boolean isMultyPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_albums);
        albumsListView = (GridView) findViewById(R.id.grid_view);

        adapter = new AudioAlbumAdapter(this, new ArrayList<AudioAlbum>());

        albumsListView.setAdapter(adapter);
        ApiFacade facade = new ApiFacade(this);
        facade.getAudioAlbums(new SimpleResponseListener<List<AudioAlbum>>(){
            @Override
            public void onResponse(final List<AudioAlbum> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(response);
                    }
                });
            }
        });
        albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioAlbum album = (AudioAlbum) parent.getItemAtPosition(position);
                if(!isMultyPane) {
                    Intent intent = new Intent(AudioAlbumsActivity.this, AudioActivity.class);
                    intent.putExtra(AudioActivity.EXTRA_ALBUM_ID, album.getId());
                    startActivity(intent);
                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.audio_content, AudioActivity.AudioListFragment.create(album.getId()))
                            .commit();
                }
            }
        });

        isMultyPane = getResources().getBoolean(R.bool.is_multy_pane);
    }

    private static final class AudioAlbumAdapter extends ArrayAdapter<AudioAlbum> {

        public AudioAlbumAdapter(Context context, List<AudioAlbum> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_audio, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_title);

            AudioAlbum item = getItem(position);

            titleView.setText(item.getTitle());
            return convertView;
        }
    }

}
