package com.teachandroid.app.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teachandroid.app.activity.MyApplication;
import com.teachandroid.app.api.reponse.*;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.Session;
import com.teachandroid.app.data.User;
import com.teachandroid.app.store.SessionStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApiFacadeService extends Service {

    public static final String EXTRA_MAIN_COMMAND = "EXTRA_MAIN_COMMAND";
    public static final String EXTRA_PARAMETERS = "EXTRA_PARAMETERS";
    public static final String EXTRA_RETURNED_BROADCAST_MESSAGE = "EXTRA_RETURNED_BROADCAST_MESSAGE";
    public static final String EXTRA_RETURNED_CLASS_NAME = "EXTRA_RETURNED_CLASS_NAME";

    private static final String TAG = ApiFacadeService.class.getSimpleName();

    private final String accessToken;
    private final String userId;
    private final HttpClient httpClient;
    private final Executor requestExecutor = Executors.newFixedThreadPool(3);

    private String mainCommandForRequest ;
    private Map<String,String> parametersForRequest;
    private String returnedBroadcastMessage;
    private Class typeOfReturnedData;

    public ApiFacadeService() {
        Session session = SessionStore.restore(MyApplication.getContext());
        this.accessToken = session.getAccessToken();
        this.userId = session.getUserId();
        this.httpClient = HttpClientFactory.getThreadSafeClient();
        parametersForRequest = new HashMap<String, String>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            obtainDataFromIntent(intent);
            final HttpGet request = makeRequest();
            requestExecutor.execute(new Runnable() {
                String threadReturnBroadcastMessage  = returnedBroadcastMessage;
                String threadTypeOfReturnedData = typeOfReturnedData.getSimpleName();

                @Override
                public void run() {
                    try {
                        HttpResponse response =  httpClient.execute(request);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        StringBuilder stringResponse = new StringBuilder();
                        String tempString = null;
                        while ((tempString=reader.readLine())!=null){
                            stringResponse.append(tempString);
                        }
                        sendResultData(stringResponse.toString(),threadReturnBroadcastMessage, threadTypeOfReturnedData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void obtainDataFromIntent(Intent intent){
        mainCommandForRequest=intent.getStringExtra(EXTRA_MAIN_COMMAND);
        parametersForRequest = (Map<String, String>) intent.getSerializableExtra(EXTRA_PARAMETERS);
        returnedBroadcastMessage = intent.getStringExtra(EXTRA_RETURNED_BROADCAST_MESSAGE);
        String className= intent.getStringExtra(EXTRA_RETURNED_CLASS_NAME);
        try {
            typeOfReturnedData =  Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private HttpGet makeRequest(){

        RequestBuilder builder = new VkRequestBuilder(mainCommandForRequest, accessToken);  //make request
        for (String hm :parametersForRequest.keySet()) {
            builder.addParam(hm,parametersForRequest.get(hm));
        }
        String query = builder.query();
        return new HttpGet(query);
    }
    private void sendResultData(String request, String localReturnedBroadcastMessage, String localTypeOfReturnedData){
        Boolean haveResult= false;
        Intent intent = new Intent(localReturnedBroadcastMessage);
        if (localTypeOfReturnedData.equals("Dialog")) {
            ApiResponse<ResponseList<Dialog>> apiResponse = new Gson().fromJson(request, new TypeToken<ApiResponse<ResponseList<Dialog>>>() {}.getType());
            if (apiResponse == null || apiResponse.getResult()==null) {return;}
            ArrayList<Dialog> result = (ArrayList<Dialog>) apiResponse.getResult().getItems();
            intent.putParcelableArrayListExtra(returnedBroadcastMessage,result);
            haveResult = true;
        }
        if (localTypeOfReturnedData.equals("Message")) {
            ApiResponse<ResponseList<Message>> apiResponse = new Gson().fromJson(request, new TypeToken<ApiResponse<ResponseList<Message>>>() { }.getType());
            if (apiResponse == null || apiResponse.getResult()==null) {return;}
            ArrayList<Message> result = (ArrayList<Message>) apiResponse.getResult().getItems();
            intent.putParcelableArrayListExtra(returnedBroadcastMessage,result);
            haveResult = true;
        }
        if (localTypeOfReturnedData.equals("User")) {

            ApiResponse<ArrayList<User>> apiResponse = new Gson().fromJson(request,new TypeToken<ApiResponse<ArrayList<User>>>(){}.getType());
            if(apiResponse != null && apiResponse.getResult()!=null){
                for (User user :apiResponse.getResult()) {
                    User tempUser = new User(user.getId());
                    tempUser.setFirstName(user.getFirstName());
                    tempUser.setLastName(user.getLastName());
                    tempUser.setPhoto50(user.getPhoto50());
                    tempUser.setPhoto100(user.getPhoto100());
                    tempUser.setPhoto200(user.getPhoto200());
                    KnownUsers.getInstance().addUser(user.getId(), tempUser);
                    ArrayList<User> result = (ArrayList<User>) apiResponse.getResult();
                    intent.putParcelableArrayListExtra(returnedBroadcastMessage,result);
                    haveResult = true;
                }
            }else {
            }
        }

        if (haveResult){  sendBroadcast(intent); }
    }
}
