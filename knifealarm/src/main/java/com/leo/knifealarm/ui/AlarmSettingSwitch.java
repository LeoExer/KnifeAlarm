package com.leo.knifealarm.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leo.knifealarm.R;
import com.leo.knifealarm.util.DenistyUtils;

/**
 * Created by Leo on 2017/4/19.
 */

public class AlarmSettingSwitch extends FrameLayout {

    private TextView mTvFeild;
    private SwitchCompat mSwitch;

    private OnCheckChangeListener mListener;

    public AlarmSettingSwitch(Context context) {
        this(context, null);
    }

    public AlarmSettingSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlarmSettingSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        obtainAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.alarm_setting_item_switch, this, true);
        mTvFeild = (TextView) findViewById(R.id.tv_feild);
        mSwitch = (SwitchCompat) findViewById(R.id.sc_switch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mListener != null) {
                    mListener.onSwitch(b);
                }
            }
        });
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlarmSettingSwitch);
        setFeildText(ta.getString(R.styleable.AlarmSettingSwitch_switchFeildText));
        setFeildTextColor(ta.getColor(R.styleable.AlarmSettingSwitch_switchFeildTextColor,
                getResources().getColor(R.color.colorPrimaryText)));
        setFeildTextSize(ta.getDimensionPixelSize(R.styleable.AlarmSettingSwitch_switchFeildTextSize,
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

    public void setCheck(boolean check) {
        mSwitch.setChecked(check);
    }

    private int getPX(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        mListener = listener;
    }

    interface OnCheckChangeListener {
        void onSwitch(boolean b);
    }
}
