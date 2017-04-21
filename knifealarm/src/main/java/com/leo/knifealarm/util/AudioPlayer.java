package com.leo.knifealarm.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.leo.knifealarm.listener.OnAudioPlayingListener;

import java.io.IOException;

/**
 * Created by Leo on 2017/4/18.
 */

public class AudioPlayer {

    private static final float DEFAULT_VOLUME = 0.5f;

    private Context mContext;
    private MediaPlayer mPlayer;
    private OnAudioPlayingListener mListener;
    private String mAudioPath;
    private Uri mAudioUri;

    public AudioPlayer(Context context) {
        mContext = context;
    }

    public void play(@NonNull String audioPath) {
        play(audioPath, null);
    }

    public void play(@NonNull String audioPath, OnAudioPlayingListener listener) {
        play(audioPath, DEFAULT_VOLUME, listener);
    }

    public void play(@NonNull String audioPth, float volume, OnAudioPlayingListener listener) {
        play(audioPth, false, volume, listener);
    }

    public void play(@NonNull String audioPath, boolean looping, float volume,
                     OnAudioPlayingListener listener) {
        try {
            mAudioPath = audioPath;
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(audioPath);
            doPlay(looping, volume, listener);
        } catch(IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.onError(e.getMessage());
            }
        }
    }

    public void play(@NonNull Uri audioUri) {
        play(audioUri, null);
    }

    public void play(@NonNull Uri audioUri, OnAudioPlayingListener listener) {
        play(audioUri, DEFAULT_VOLUME, listener);
    }

    public void play(@NonNull Uri audioUri, float volume, OnAudioPlayingListener listener) {
        play(audioUri, false, volume, listener);
    }

    public void play(@NonNull Uri audioUri, boolean looping, float volume,
                     OnAudioPlayingListener listener) {
        try {
            mAudioUri = audioUri;
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mContext, mAudioUri);
            doPlay(looping, volume, listener);
        } catch(IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.onError(e.getMessage());
            }
        }
    }

    private void doPlay(boolean looping, float volume, OnAudioPlayingListener listener)
            throws IOException {
        mListener = listener;
        mPlayer.prepare();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                if(mListener != null) {
                    mListener.onCompleted();
                }
                release();
            }
        });
        mPlayer.setVolume(volume, volume);
        mPlayer.setLooping(looping);
        mPlayer.start();
        if(mListener != null) {
            mListener.onStart();
        }
    }

    public void setVolume(float volume) {
        mPlayer.setVolume(volume, volume);
    }

    public void stop() {
        if(mListener != null) {
            mListener.onStop();
        }

        release();
    }

    public String getAudioPath() {
        return mAudioPath;
    }

    public Uri getAudioUri() {
        return mAudioUri;
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    private void release() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        mAudioPath = null;
        mAudioUri = null;
    }
}
