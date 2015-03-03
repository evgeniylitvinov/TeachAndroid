package com.teachandroid.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;

import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends ActionBarActivity {

    private DialogAdapter dialogAdapter;

    private ListView dialogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        dialogAdapter = new DialogAdapter(this, new ArrayList<Dialog>());

        dialogList = (ListView) findViewById(R.id.list_dialog);

        dialogList.setAdapter(dialogAdapter);

        ApiFacade facade = new ApiFacade(this);
        facade.getDialog(new SimpleResponseListener<List<Dialog>>() {
            @Override
            public void onResponse(final List<Dialog> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogAdapter.addAll(response);
                    }
                });
            }
        });

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DialogActivity.this,MessageActivity.class);
                Message message = ((Dialog)parent.getAdapter().getItem(position)).getMessage();
                intent.putExtra(Message.EXTRA_MESSAGE,message);
                startActivity(intent);
            }
        });
    }

    private static final class DialogAdapter extends ArrayAdapter<Dialog> {

        public DialogAdapter(Context context, List<Dialog> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.list_item_dialog, null);
            }
            TextView dialogOwnerData = (TextView) convertView.findViewById(R.id.text_dialog_owner_data);
            TextView dialogTitleData = (TextView) convertView.findViewById(R.id.text_dialog_title_data);
            TextView dialogUnreadMessageData = (TextView) convertView.findViewById(R.id.text_dialog_unread_message_data);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_dialog);

            Dialog item = getItem(position);
            Message message = item.getMessage();
            if (message==null) {return convertView;}
            User user = KnownUsers.getInstance().getUserFromId(message.getUserId());
            if (user==null) {return convertView;}
            dialogOwnerData.setText(user.getFirstName()+" - "+user.getLastName());
            dialogTitleData.setText(item.getMessage().getTitle());
            dialogUnreadMessageData.setText(""+item.getUnread());
            ImageLoader.getInstance().displayImage(user.getPhoto50(),imageView);

            return convertView;
        }
    }
}
