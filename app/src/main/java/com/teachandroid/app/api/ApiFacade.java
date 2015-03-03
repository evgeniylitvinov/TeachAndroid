package com.teachandroid.app.api;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teachandroid.app.api.reponse.*;
import com.teachandroid.app.api.reponse.Error;
import com.teachandroid.app.data.Audio;
import com.teachandroid.app.data.Session;
import com.teachandroid.app.store.SessionStore;
import com.teachandroid.app.util.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
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
        Logger.log(TAG, "api request - " + query);

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

    public void getVideoVK() throws UnsupportedEncodingException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder urlBuilder = new StringBuilder("https://api.vk.com/method/");
                urlBuilder.append("video.get");
                urlBuilder.append("?");
                try {
                    urlBuilder.append("count").append("=").append(URLEncoder.encode("100", "UTF-8")).append("&");
                    urlBuilder.append("v").append("=").append(URLEncoder.encode("5.28", "UTF-8")).append("&");
                    urlBuilder.append("access_token").append("=").append(URLEncoder.encode(accessToken, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HttpURLConnection connection = null;

                try {
                    String urlString = urlBuilder.toString();

                    Logger.log(TAG, "запрос " + urlString);
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        Logger.log(TAG, "ответ " + line);
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





    public void getUserAudio(String userId){

    }
}
