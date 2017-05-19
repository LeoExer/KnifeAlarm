package com.leo.knifealarm.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.knifealarm.R;

/**
 * Created by Leo on 2017/5/19.
 */

public class AlarmSelectDaysDialog implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTvOk;
    private TextView mTvCancel;
    private ImageView mIvMon, mIvTue, mIvWen, mIvThu, mIvFri, mIvSat, mIvSun;
    private Dialog mDialog;

    private int[] mDays;

    private OnSelectDaysListener mListener;

    public AlarmSelectDaysDialog(Context context, int[] days) {
        mDays = days;
        mDialog = new Dialog(context);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_selelct_days_dialog_layout, null);
        mDialog.setContentView(view);

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvOk = (TextView) view.findViewById(R.id.tv_ok);
        mTvOk.setOnClickListener(this);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        mIvMon = (ImageView) view.findViewById(R.id.iv_mon);
        mIvTue = (ImageView) view.findViewById(R.id.iv_tue);
        mIvWen = (ImageView) view.findViewById(R.id.iv_wen);
        mIvThu = (ImageView) view.findViewById(R.id.iv_thu);
        mIvFri = (ImageView) view.findViewById(R.id.iv_fri);
        mIvSat = (ImageView) view.findViewById(R.id.iv_sat);
        mIvSun = (ImageView) view.findViewById(R.id.iv_sun);
        mIvMon.setOnClickListener(this);
        mIvTue.setOnClickListener(this);
        mIvWen.setOnClickListener(this);
        mIvThu.setOnClickListener(this);
        mIvFri.setOnClickListener(this);
        mIvSat.setOnClickListener(this);
        mIvSun.setOnClickListener(this);
    }

    public AlarmSelectDaysDialog setTitle(String text) {
        mTvTitle.setText(text);
        return this;
    }


    public AlarmSelectDaysDialog setOnSelectDaysListener(OnSelectDaysListener lisenter) {
        mListener = lisenter;
        return this;
    }

    public Dialog show() {
        if (mDialog != null) {
            mDialog.show();
        }
        return mDialog;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void release() {
        dismiss();;
        mDialog = null;
    }

    @Override
    public void onClick(View view) {
        if (view == mIvMon) {
            update(1);
            mIvMon.setSelected(mDays[1] == 1);
        } else if (view == mIvTue) {
            update(2);
            mIvTue.setSelected(mDays[2] == 1);
        } else if (view == mIvWen) {
            update(3);
            mIvWen.setSelected(mDays[3] == 1);
        } else if (view == mIvThu) {
            update(4);
            mIvThu.setSelected(mDays[4] == 1);
        } else if (view == mIvFri) {
            update(5);
            mIvFri.setSelected(mDays[5] == 1);
        } else if (view == mIvSat) {
            update(6);
            mIvSat.setSelected(mDays[6] == 1);
        } else if (view == mIvSun) {
            update(0);
            mIvSun.setSelected(mDays[0] == 1);
        } else if (view == mTvOk) {
            if (mListener != null) {
                mListener.onClickOk(mDays);
            }
            dismiss();
        } else if (view == mTvCancel) {
            if (mListener != null) {
                mListener.onClickCancel();
            }
            dismiss();
        }
    }

    interface OnSelectDaysListener {
        void onClickOk(int[] days);

        void onClickCancel();
    }

    private void update(int position) {
        int selected = mDays[position];
        if (selected == 0) {
            selected = 1;
        } else {
            selected = 0;
        }
        mDays[position] = selected;
    }
}
