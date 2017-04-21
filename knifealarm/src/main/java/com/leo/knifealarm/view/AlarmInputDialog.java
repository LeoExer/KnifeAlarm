package com.leo.knifealarm.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leo.knifealarm.R;

/**
 * Created by Leo on 2017/4/20.
 */

public class AlarmInputDialog implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTvOk;
    private TextView mTvCancel;
    private EditText mEtInput;
    private Dialog mDialog;

    private OnInputLinstener mListener;

    public AlarmInputDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_input_dialog_layout, null);
        mDialog.setContentView(view);

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvOk = (TextView) view.findViewById(R.id.tv_ok);
        mTvOk.setOnClickListener(this);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        mEtInput = (EditText) view.findViewById(R.id.et_input);
    }

    public AlarmInputDialog setTitle(String text) {
        mTvTitle.setText(text);
        return this;
    }

    public AlarmInputDialog setEditText(String text) {
        mEtInput.setText(text);
        return this;
    }

    public AlarmInputDialog setOnInputLisenter(OnInputLinstener lisenter) {
        mListener = lisenter;
        return this;
    }

    public Dialog show() {
        if (mDialog != null) {
            mDialog.show();
        }
        return mDialog;
    }

    @Override
    public void onClick(View view) {
        mDialog.dismiss();
        mDialog = null;

        if (mListener == null) {
            return;
        }

        if (view == mTvOk) {
            mListener.onClickOk(mEtInput.getText().toString());
        } else if (view == mTvCancel) {
            mListener.onClickCancel();
        }
    }

    interface OnInputLinstener {
        void onClickOk(String text);

        void onClickCancel();
    }
}
