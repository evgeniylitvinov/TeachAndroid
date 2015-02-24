package com.teachandroid.app.api;

import com.teachandroid.app.api.reponse.Error;

public class SimpleResponseListener<T> implements ResponseListener<T> {
    @Override
    public void onResponse(T response) {

    }

    @Override
    public void onError(Error error) {

    }
}
