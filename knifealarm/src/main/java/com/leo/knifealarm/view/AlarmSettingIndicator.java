package com.leo.knifealarm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.knifealarm.R;
import com.leo.knifealarm.util.DenistyUtils;

/**
 * Created by Leo on 2017/4/18.
 */

public class AlarmSettingIndicator extends FrameLayout implements View.OnClickListener {

    private TextView mTvFeild;
    private TextView mTvValue;
    private ImageView mIvIndicator;

    private OnItemClickListener mListener;

    public AlarmSettingIndicator(Context context) {
        this(context, null);
    }

    public AlarmSettingIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlarmSettingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        obtainAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.alarm_setting_item_indicator, this, true);
        mTvFeild = (TextView) findViewById(R.id.tv_feild);
        mTvValue = (TextView) findViewById(R.id.tv_value);
        mIvIndicator = (ImageView) findViewById(R.id.iv_indicator);
        setOnClickListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlarmSettingIndicator);
        setFeildText(ta.getString(R.styleable.AlarmSettingIndicator_feildText));
        setFeildTextColor(ta.getColor(R.styleable.AlarmSettingIndicator_feildTextColor,
                getResources().getColor(R.color.colorPrimaryText)));
        setFeildTextSize(ta.getDimensionPixelSize(R.styleable.AlarmSettingIndicator_feildTextSize,
                DenistyUtils.px2dp(context, getPX(R.dimen.text_size_normal))));
        setValueText(ta.getString(R.styleable.AlarmSettingIndicator_valueText));
        setValueTextColor(ta.getColor(R.styleable.AlarmSettingIndicator_valueTextColor,
                getResources().getColor(R.color.colorSecondaryText)));
        setValueTextSize(ta.getDimensionPixelSize(R.styleable.AlarmSettingIndicator_valueTextSize,
                DenistyUtils.px2dp(context, getPX(R.dimen.text_size_small))));
        setIndicator(ta.getResourceId(R.styleable.AlarmSettingIndicator_indicatorIcon,
                R.drawable.ic_indicator_down));
        ta.recycle();
    }

    public void setFeildText(String text) {
        mTvFeild.setText(text);
    }

    public String getFeildText() {
        return mTvFeild.getText().toString();
    }

    public void setFeildTextColor(@ColorInt int color) {
        mTvFeild.setTextColor(color);
    }

    public void setFeildTextSize(int size) {
        mTvFeild.setTextSize(size);
    }

    public void setValueText(String text) {
        mTvValue.setText(text);
    }

    public String getValueText() {
        return mTvValue.getText().toString();
    }

    public void setValueTextColor(@ColorInt int color) {
        mTvValue.setTextColor(color);
    }

    public void setValueTextSize(int size) {
        mTvValue.setTextSize(size);
    }

    public void setIndicator(@DrawableRes int resId) {
        mIvIndicator.setImageResource(resId);
    }

    public void setIndicator(Bitmap bitmap) {
        mIvIndicator.setImageBitmap(bitmap);
    }


    private int getPX(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(View view);
    }
}
