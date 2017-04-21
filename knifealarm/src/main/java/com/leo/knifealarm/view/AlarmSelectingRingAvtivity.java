package com.leo.knifealarm.view;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.leo.knifealarm.R;
import com.leo.knifealarm.util.RingtoneUtils;

import java.util.List;

/**
 * Created by Leo on 2017/4/21.
 */

public class AlarmSelectingRingAvtivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TYPE = RingtoneManager.TYPE_ALARM;

    private List<String> mSysRingTitles;
    private Uri mSelectedRingtoneUri;
    private int mPosition;
    private Ringtone mPreRingtone;

    private ImageView mIvBack;
    private RecyclerView mRvRing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_select_ring_activity);

        initData();
        setupView();
    }

    private void initData() {
        mPosition = getIntent().getIntExtra("position", -1);
        mSelectedRingtoneUri = RingtoneUtils.getDefaultRingtoneUri(AlarmSelectingRingAvtivity.this,
                TYPE);
        mSysRingTitles = RingtoneUtils.getAllRingtoneTitle(this, TYPE);
        mSysRingTitles.add(0, "跟随系统");
    }

    public void setupView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);

        mRvRing = (RecyclerView) findViewById(R.id.rv_ring);
        mRvRing.setLayoutManager(new LinearLayoutManager(this));
        AlarmSelectingAdapter adapter = new AlarmSelectingAdapter(mSysRingTitles,
                mPosition < 0 ? 0 : mPosition + 1);
        adapter.setOnItemSelectListener(new AlarmSelectingAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(int position, String text) {
                stopPreAudio();
                if (position == 0) { // 跟随系统
                    mPosition = -1;
                    mSelectedRingtoneUri = RingtoneUtils.getDefaultRingtoneUri(AlarmSelectingRingAvtivity.this,
                            TYPE);
                    mPreRingtone = RingtoneManager.getRingtone(AlarmSelectingRingAvtivity.this, mSelectedRingtoneUri);
                    mPreRingtone.play();
                } else {
                    mPosition = position - 1;
                    mPreRingtone = RingtoneUtils.getRingtoneByPosition(AlarmSelectingRingAvtivity.this,
                            TYPE, mPosition);
                    mPreRingtone.play();
                }
            }
        });
        mRvRing.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == mIvBack) {
            back();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        stopPreAudio();

        String title = "跟随系统";
        if (mPosition != -1) {
            mSelectedRingtoneUri = RingtoneUtils.getRingtoneUriByPosition(AlarmSelectingRingAvtivity.this,
                    TYPE, mPosition);
            title = mSysRingTitles.get(mPosition + 1);
        }

        String audioPath = mSelectedRingtoneUri.toString();
        Intent data = new Intent();
        data.putExtra("audioPath", audioPath);
        data.putExtra("audioTitle", title);
        setResult(RESULT_OK, data);
        finish();
    }

    private void stopPreAudio() {
        if (mPreRingtone != null && mPreRingtone.isPlaying()) {
            mPreRingtone.stop();
        }
    }
}
