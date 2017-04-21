package com.leo.knifealarm.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.leo.knifealarm.R;

import java.util.List;

/**
 * Created by Leo on 2017/4/19.
 */

public class AlarmSelectingDialog {

    private TextView mTvTitle;
    private Dialog mDialog;
    private AlarmSelectingAdapter mAdapter;

    public AlarmSelectingDialog(Context context, List<String> texts) {
        this(context, texts, 0);
    }

    public AlarmSelectingDialog(Context context, List<String> texts, int position) {
        mDialog = new Dialog(context);
        mDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_select_dialog_layout, null);
        mDialog.setContentView(view);

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView rvSelection = (RecyclerView) view.findViewById(R.id.rv_selecion);
        mAdapter = new AlarmSelectingAdapter(texts, position);
        rvSelection.setAdapter(mAdapter);
        rvSelection.setLayoutManager(new LinearLayoutManager(context));
    }

    public AlarmSelectingDialog setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public AlarmSelectingDialog setOnItemSelectListener(AlarmSelectingAdapter.OnItemSelectListener listener) {
        mAdapter.setOnItemSelectListener(listener);
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
}
