package com.teachandroid.app.api;

import com.teachandroid.app.api.reponse.Error;

public interface ResponseListener<T> {

    void onResponse(T response);

    void onError(Error error);
}
