package com.leo.knifealarm.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.knifealarm.R;

import java.util.List;

/**
 * Created by Leo on 2017/4/21.
 */
public class AlarmSelectingAdapter extends RecyclerView.Adapter {

    private List<String> mData;
    private int mPosition;

    private OnItemSelectListener mListener;

    public AlarmSelectingAdapter(List<String> data, int position) {
        mData = data;
        mPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_select_item, parent, false);
        return new SelectingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SelectingHolder) holder).updateView();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class SelectingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv;
        ImageView iv;

        public SelectingHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_text);
            iv = (ImageView) itemView.findViewById(R.id.iv_selected);
            itemView.setOnClickListener(this);
            tv.setOnClickListener(this);
            iv.setOnClickListener(this);
        }

        public void updateView() {
            int positon = getAdapterPosition();
            tv.setText(mData.get(positon));
            if (mPosition == positon) {
                tv.setTextColor(tv.getResources().getColor(R.color.colorAccent));
                iv.setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(tv.getResources().getColor(R.color.colorPrimaryText));
                iv.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            mPosition = getAdapterPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onItemSelected(mPosition, mData.get(mPosition));
            }
        }
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mListener = listener;
    }

    interface OnItemSelectListener {
        void onItemSelected(int position, String text);
    }
}
