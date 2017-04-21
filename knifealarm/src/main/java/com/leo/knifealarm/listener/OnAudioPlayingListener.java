package com.leo.knifealarm.listener;

/**
 * Created by Leo on 2017/4/20.
 */

public interface OnAudioPlayingListener {

    void onStart();

    void onCompleted();

    void onStop();

    void onError(String errMsg);
}
