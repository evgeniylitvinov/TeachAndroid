package com.teachandroid.app.api;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.teachandroid.app.api.reponse.*;
import com.teachandroid.app.api.reponse.Error;
import com.teachandroid.app.data.Audio;
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Session;
import com.teachandroid.app.data.User;
import com.teachandroid.app.store.SessionStore;
import com.teachandroid.app.util.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


//TODO: http://vk.com/dev/api_requests

public class ApiFacade {

    private static final String TAG = ApiFacade.class.getSimpleName();

    private final String accessToken;

    private final String userId;

    private final HttpClient httpClient;

    private final Executor requestExecutor = Executors.newFixedThreadPool(3);

    public ApiFacade(Context context) {
        Session session = SessionStore.restore(context);
        this.accessToken = session.getAccessToken();
        this.userId = session.getUserId();
        this.httpClient = HttpClientFactory.getThreadSafeClient();
    }

    public void getAudio(final ResponseListener<List<Audio>> listener){
        RequestBuilder builder = new VkRequestBuilder("audio.get", accessToken);
        builder.addParam("count", "100");

        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);

        final HttpGet request = new HttpGet(query);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    ApiResponse<ResponseList<Audio>> apiResponse = new Gson().fromJson(reader, new TypeToken<ApiResponse<ResponseList<Audio>>>() {
                    }.getType());
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void getGroups(final ResponseListener<List<Group>> listener){
        RequestBuilder builder = new VkRequestBuilder("groups.get", accessToken);
        builder.addParam("count", "100");
        builder.addParam("extended","1");

        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);

        final HttpGet request = new HttpGet(query);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);

                    //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String responseString = EntityUtils.toString(response.getEntity());
                    Logger.log(TAG,"json groups request " + responseString);

                    ApiResponse<ResponseList<Group>> apiResponse = new Gson().fromJson(responseString, new TypeToken<ApiResponse<ResponseList<Group>>>() {
                    }.getType());
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void getGroup(final ResponseListener<List<Group>> listener) {
        RequestBuilder builder = new VkRequestBuilder("groups.get", accessToken);
        builder.addParam("count", "100");
        builder.addParam("extended", "1");

        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);

        final HttpGet request = new HttpGet(query);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    ApiResponse<ResponseList<Group>> apiResponse = new Gson().fromJson(reader, new TypeToken<ApiResponse<ResponseList<Group>>>() {
                    }.getType());
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void getDialog(final ResponseListener<List<Dialog>> listener) {
        RequestBuilder builder = new VkRequestBuilder("messages.getDialogs", accessToken);  //make request
        builder.addParam("count", "20");
        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);
        final HttpGet request = new HttpGet(query);
        //prepare execute request in thread
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //execute request
                    //take response
                    HttpResponse response = httpClient.execute(request);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    //put responce in Dialog
                    ApiResponse<ResponseList<Dialog>> apiResponse = new Gson().fromJson(reader, new TypeToken<ApiResponse<ResponseList<Dialog>>>() {
                    }.getType());
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void getMessage(Message message, final ResponseListener<List<Message>> listener) {
        RequestBuilder builder = new VkRequestBuilder("messages.getHistory", accessToken);
        builder.addParam("user_id", message.getUserIdString());
        builder.addParam("chat_id", message.getChatId());
        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);
        final HttpGet request = new HttpGet(query);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    ApiResponse<ResponseList<Message>> apiResponse = new Gson().fromJson(reader, new TypeToken<ApiResponse<ResponseList<Message>>>() {
                    }.getType());
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void getUserDataOnlyFromId (final long id){
        RequestBuilder builder = new VkRequestBuilder("users.get", accessToken);
        builder.addParam("user_ids", ""+id);
        builder.addParam("fields", "photo_50,photo_100,photo_200");
        String query = builder.query();
        Logger.log(TAG, "api request - %s", query);
        final HttpGet request = new HttpGet(query);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = null;
                try {
                    response = httpClient.execute(request);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    ApiResponse<ArrayList<User>> apiResponse = new Gson().fromJson(reader,new TypeToken<ApiResponse<ArrayList<User>>>(){}.getType());
                    if(apiResponse != null && apiResponse.getResult()!=null && apiResponse.getResult().get(0)!=null){
                        User user = new User(id);
                        user.setFirstName(apiResponse.getResult().get(0).getFirstName());
                        user.setLastName(apiResponse.getResult().get(0).getLastName());
                        user.setPhoto50(apiResponse.getResult().get(0).getPhoto50());
                        user.setPhoto100(apiResponse.getResult().get(0).getPhoto100());
                        user.setPhoto200(apiResponse.getResult().get(0).getPhoto200());
                        KnownUsers.getInstance().addUser(id,user);
                    }else {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }); //end requestExecutor
    }

    public void searchAudio(final String audioKeyWord, final ResponseListener<List<Audio>> listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder urlBuilder = new StringBuilder("https://api.vk.com/method/");
                urlBuilder.append("audio.search");
                urlBuilder.append("?");
                try {
                    urlBuilder.append("q").append("=").append(URLEncoder.encode(audioKeyWord, "UTF-8")).append("&");
                    urlBuilder.append("count").append("=").append(URLEncoder.encode("100", "UTF-8")).append("&");
                    urlBuilder.append("v").append("=").append(URLEncoder.encode("5.28", "UTF-8")).append("&");
                    urlBuilder.append("access_token").append("=").append(URLEncoder.encode(accessToken, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                HttpURLConnection connection = null;
                try {
                    String urlString = urlBuilder.toString();

                    Logger.log(TAG, "url request " + urlString);

                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String response = null;
                    while ((response = reader.readLine()) != null){
                        Logger.log(TAG, "url response " + response);
                    }

                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<ResponseList<Audio>>>() {
                    }.getType();

                    ApiResponse<ResponseList<Audio>> apiResponse = gson.fromJson(response, type);
                    if(apiResponse != null){
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    }else {
                        listener.onError(new Error());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }


}
