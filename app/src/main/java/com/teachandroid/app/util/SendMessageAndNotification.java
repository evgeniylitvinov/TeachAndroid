package com.teachandroid.app.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.teachandroid.app.LoaderApplication;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacadeService;

import java.util.HashMap;

public class SendMessageAndNotification {
    public static void sendNotification(String title, String message){
        Notification.Builder builder =
                new Notification.Builder(LoaderApplication.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);


        builder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) LoaderApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public static void sendMessageWithService(View v , String sendMessage, HashMap<String,String> parameters){
        Intent intentForGetHistory = new Intent(v.getContext(), ApiFacadeService.class);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "messages.send");

        parameters.put("message", sendMessage);

        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parameters);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.RETURNED_TYPE_NO_RETURN);
        intentForGetHistory.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, ApiFacadeService.RETURNED_TYPE_NO_RETURN);

        LoaderApplication.getContext().startService(intentForGetHistory);
    }
}
