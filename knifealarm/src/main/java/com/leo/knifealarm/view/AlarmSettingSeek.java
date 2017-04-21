package com.leo.knifealarm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatSeekBar;

import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.leo.knifealarm.R;
import com.leo.knifealarm.util.DenistyUtils;

/**
 * Created by Leo on 2017/4/19.
 */

public class AlarmSettingSeek extends FrameLayout {

    private TextView mTvFeild;
    private AppCompatSeekBar mSeekBar;

    private float mMaxProgress;
    private float mProgress;

    private OnProgressChangeListener mListener;

    public AlarmSettingSeek(Context context) {
        this(context, null);
    }

    public AlarmSettingSeek(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlarmSettingSeek(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        obtainAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.alarm_setting_item_seek, this, true);
        mTvFeild = (TextView) findViewById(R.id.tv_feild);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.sb_seek);
        mMaxProgress = mSeekBar.getMax();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mProgress = i;
                if (mListener != null) {
                    mListener.onProgressChanged(mProgress / mMaxProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mListener != null) {
                    mListener.onStopTracking();
                }
            }
        });
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlarmSettingSeek);
        setFeildText(ta.getString(R.styleable.AlarmSettingSeek_seekFeildText));
        setFeildTextColor(ta.getColor(R.styleable.AlarmSettingSeek_seekFeildTextColor,
                getResources().getColor(R.color.colorPrimaryText)));
        setFeildTextSize(ta.getDimensionPixelSize(R.styleable.AlarmSettingSeek_seekFeildTextSize,
                DenistyUtils.px2dp(context, getPX(R.dimen.text_size_normal))));
        ta.recycle();
    }

    public void setFeildText(String text) {
        mTvFeild.setText(text);
    }

    public void setFeildTextColor(@ColorInt int color) {
        mTvFeild.setTextColor(color);
    }

    public void setFeildTextSize(int size) {
        mTvFeild.setTextSize(size);
    }

    public void setProgress(float progress) {
        mSeekBar.setProgress((int) (progress * 100));
    }

    private int getPX(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        mListener = listener;
    }

    interface OnProgressChangeListener {
        void onProgressChanged(float progress);

        void onStopTracking();
    }
}
