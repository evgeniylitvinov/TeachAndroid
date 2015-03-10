package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.data.User;
import com.teachandroid.app.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogActivity extends Activity {

    private DialogAdapter dialogAdapter;
    private ListView dialogList;

    private DialogReceiver receiverDialog;
    private DialogSearchReceiver receiverDialogSearch;
    private UserReceiver receiverUser;
    private SearchView actionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        dialogAdapter = new DialogAdapter(this, new ArrayList<Dialog>());
        dialogList = (ListView) findViewById(R.id.list_dialog);
        dialogList.setAdapter(dialogAdapter);
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DialogActivity.this,MessageActivity.class);
                Message message = ((Dialog)parent.getAdapter().getItem(position)).getMessage();
                intent.putExtra(MessageActivity.EXTRA_MESSAGE,message);
                startActivity(intent);
            }
        });

        registerAllNeededReceiver();
        fillAndStartServiceGetDialogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog_menu, menu);
        MenuItem item = menu.findItem(R.id.action_dialog_search);
        actionView = (SearchView) item.getActionView();
        actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fillAndStartServiceSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterAllNeededReceiver();
    }

    public static final class DialogAdapter extends ArrayAdapter<Dialog> {

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
            TextView dialogBodyData = (TextView) convertView.findViewById(R.id.text_dialog_body_data);
            TextView dialogUnreadMessageData = (TextView) convertView.findViewById(R.id.text_dialog_unread_message_data);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_dialog);

            Dialog item = getItem(position);
            Message message = item.getMessage();
            if (message==null) {return convertView;}
            User user = KnownUsers.getInstance().getUserFromId(message.getUserId());
            if (user==null) {return convertView;}
            dialogOwnerData.setText(user.getFirstName()+" - "+user.getLastName());
            dialogTitleData.setText(item.getMessage().getTitle());
            dialogBodyData.setText(item.getMessage().getBody());
            dialogUnreadMessageData.setText(""+item.getUnread());
            ImageLoader.getInstance().displayImage(user.getPhoto100(),imageView);

            return convertView;
        }
    }
    private void fillAndStartServiceGetDialogs() {
        Intent intent = new Intent(this,ApiFacadeService.class);
        intent.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.getDialogs");
        HashMap<String,String> parameters = new HashMap<String, String>();
        parameters.put("count", "20");
        intent.putExtra(ApiFacadeService.EXTRA_PARAMETERS,parameters);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, Dialog.BROADCAST_DIALOG);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,Dialog.RETURNED_TYPE_DIALOG);

        startService(intent);
    }
    private void fillAndStartServiceSearch(String query) {
        Intent intent = new Intent(this,ApiFacadeService.class);
        intent.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.search");
        HashMap<String,String> parameters = new HashMap<String, String>();
        parameters.put("q", query);
        intent.putExtra(ApiFacadeService.EXTRA_PARAMETERS,parameters);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, Message.BROADCAST_MESSAGE_SEARCH);
        intent.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,Message.RETURNED_TYPE_MESSAGE);

        startService(intent);
    }

    private void registerAllNeededReceiver() {
        receiverDialog = new DialogReceiver();
        registerReceiver(receiverDialog, new IntentFilter(Dialog.BROADCAST_DIALOG));
        receiverDialogSearch = new DialogSearchReceiver();
        registerReceiver(receiverDialogSearch, new IntentFilter(Message.BROADCAST_MESSAGE_SEARCH));
        receiverUser = new UserReceiver();
        registerReceiver(receiverUser, new IntentFilter(KnownUsers.BROADCAST_USER));
    }

    private void unRegisterAllNeededReceiver() {
        unregisterReceiver(receiverDialog);
        unregisterReceiver(receiverDialogSearch);
        unregisterReceiver(receiverUser);
    }




    private class DialogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            ArrayList<Dialog> result;
            result = intent.getParcelableArrayListExtra(Dialog.BROADCAST_DIALOG);
            dialogAdapter.addAll(result);
            dialogAdapter.notifyDataSetChanged();
        }
    }

    private class DialogSearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.log("DialogReceiver", "receive - %s");
            if (intent == null ) {return;}
            ArrayList<Message> resultMessage;
            resultMessage = intent.getParcelableArrayListExtra(Message.BROADCAST_MESSAGE_SEARCH);
            ArrayList<Dialog> resultDialogs = new ArrayList<Dialog>();
            Dialog tempDialog;
            dialogAdapter.clear();
            for (Message message : resultMessage){
                tempDialog = new Dialog(0,message);
                resultDialogs.add(tempDialog);
            }
            dialogAdapter.addAll(resultDialogs);
            dialogAdapter.notifyDataSetChanged();
            actionView.setIconified(true);

        }
    }

    private class UserReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialogAdapter.notifyDataSetChanged();
        }
    }
}
