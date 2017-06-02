package com.infoservices.lue.socialMedia;

/**
 * Created by Talent track on 03-02-2016.
 */
public interface Callback<T> {
    public void onSuccess(T t);
    public void onError(String errorMessage);
}
