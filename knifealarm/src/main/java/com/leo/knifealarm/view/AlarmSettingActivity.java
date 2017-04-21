package com.leo.knifealarm.view;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.leo.knifealarm.AlarmBuilder;
import com.leo.knifealarm.KnifeAlarm;
import com.leo.knifealarm.R;
import com.leo.knifealarm.data.Alarm;
import com.leo.knifealarm.util.AudioPlayer;
import com.leo.knifealarm.util.RingtoneUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leo on 2017/4/19.
 */

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ALARM_RING_SELECTED_REQUEST_CODE = 1;

    private TextView mTvBarTitle;
    private TextView mTvBarCancel;
    private TextView mTvBarSave;
    private TextView mTvTimeCounter;

    private TimePicker mTimePicker;

    private AlarmSettingIndicator mIndiRepeat;
    private AlarmSettingIndicator mIndiRing;
    private AlarmSettingIndicator mIndiTag;
    private AlarmSettingSeek mSeekVoice;
    private AlarmSettingSwitch mSwitchRing;
    private AlarmSettingSwitch mSwitchVabrate;

    private int mHour;
    private int mMinute;
    private String mTag;
    private boolean mIsPlayAudio;
    private String mAudioPath;
    private float mVolume;
    private boolean mIsVabrate;
    private boolean mIsRepeat;
    private int[] mCustomDays;

    private String mAudioTitle;
    private int mDefaultAudioPostion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting_activity);

        initData();
        setupView();
    }

    private void initData() {
        Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        if (alarm == null) {
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mTag = "闹钟";
            mIsPlayAudio = true;
            mVolume = 0.5f;
            mIsVabrate = true;
            mIsRepeat = false;
            mCustomDays = new int[7];
            Uri audioUri = RingtoneUtils.getDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
            mAudioPath = audioUri.toString();
            mDefaultAudioPostion = -1;
            mAudioTitle = "跟随系统";
        } else {
            mHour = alarm.getHour();
            mMinute = alarm.getMinute();
            mTag = alarm.getTag();
            mIsPlayAudio = alarm.isPlayAudio();
            mVolume = alarm.getVolume();
            mIsVabrate = alarm.isVibrate();
            mIsRepeat = alarm.isRepeat();
            mCustomDays = alarm.getRepeatDays();
            mAudioPath = alarm.getAudioPath();
            mDefaultAudioPostion = RingtoneUtils.getRingtonePositionByUri(this,
                    RingtoneManager.TYPE_ALARM, Uri.parse(mAudioPath));
            mAudioTitle = RingtoneUtils.getRingtoneByPosition(this, RingtoneManager.TYPE_ALARM,
                    mDefaultAudioPostion).getTitle(this);
        }
    }

    private void setupView() {
        mTvBarTitle = (TextView) findViewById(R.id.tv_title);
        mTvBarTitle.setText(mDefaultAudioPostion == -1 ?
                getString(R.string.create_alarm) : getString(R.string.edit_alarm));
        mTvBarCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvBarCancel.setOnClickListener(this);
        mTvBarSave = (TextView) findViewById(R.id.tv_save);
        mTvBarSave.setOnClickListener(this);
        mTvTimeCounter = (TextView) findViewById(R.id.tv_time_counter);

        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                log("hour: " + hourOfDay + ", minute: " + minute);
                mHour = hourOfDay;
                mMinute = minute;
            }
        });

        setupSettingItemView();
    }

    private void setupSettingItemView() {
        mIndiRepeat = (AlarmSettingIndicator) findViewById(R.id.indicator_repeat);
        mIndiRepeat.setOnItemClickListener(new AlarmSettingIndicator.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                String valueText = mIndiRepeat.getValueText();
                int position = 0;
                final String[] strs = getResources().getStringArray(R.array.repeat);
                for (int i = 0; i < strs.length; i++) {
                    if (strs[i].equalsIgnoreCase(valueText)) {
                        position = i;
                        break;
                    }
                }
                List<String> data = Arrays.asList(strs);
                final AlarmSelectingDialog dialog = new AlarmSelectingDialog(AlarmSettingActivity.this, data, position);
                dialog.setTitle("重复")
                        .setOnItemSelectListener(new AlarmSelectingAdapter.OnItemSelectListener() {
                            @Override
                            public void onItemSelected(int position, String text) {
                                // TODO conut select days
                                log("select repeat: " + text);
                                mIndiRepeat.setValueText(text);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        mIndiRing = (AlarmSettingIndicator) findViewById(R.id.indicator_ring);
        mIndiRing.setValueText(mAudioTitle);
        mIndiRing.setOnItemClickListener(new AlarmSettingIndicator.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(AlarmSettingActivity.this, AlarmSelectingRingAvtivity.class);
                intent.putExtra("position", mDefaultAudioPostion);
                startActivityForResult(intent, ALARM_RING_SELECTED_REQUEST_CODE);
            }
        });

        mIndiTag = (AlarmSettingIndicator) findViewById(R.id.indicator_tag);
        mIndiTag.setValueText(mTag);
        mIndiTag.setOnItemClickListener(new AlarmSettingIndicator.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                new AlarmInputDialog(AlarmSettingActivity.this)
                        .setTitle("标签")
                        .setEditText(mTag)
                        .setOnInputLisenter(new AlarmInputDialog.OnInputLinstener() {
                            @Override
                            public void onClickOk(String text) {
                                mIndiTag.setValueText(text);
                                mTag = text;
                                log("tag: " + mTag);
                            }

                            @Override
                            public void onClickCancel() {
                                // do nothing
                            }
                        }).show();
            }
        });

        final AudioPlayer tempPlayer = new AudioPlayer(this);
        mSeekVoice = (AlarmSettingSeek) findViewById(R.id.seek_voice);
        mSeekVoice.setProgress(mVolume);
        mSeekVoice.setOnProgressChangeListener(new AlarmSettingSeek.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(float progress) {
                mVolume = progress;
                log("volume: " + mVolume);
                if (!tempPlayer.isPlaying()) {
                    tempPlayer.play(Uri.parse(mAudioPath), true, mVolume, null);
                }
                tempPlayer.setVolume(mVolume);
            }

            @Override
            public void onStopTracking() {
                tempPlayer.stop();
            }
        });

        mSwitchRing = (AlarmSettingSwitch) findViewById(R.id.switch_ring);
        mSwitchRing.setCheck(mIsPlayAudio);
        mSwitchRing.setOnCheckChangeListener(new AlarmSettingSwitch.OnCheckChangeListener() {
            @Override
            public void onSwitch(boolean b) {
                mIsPlayAudio = b;
                log("play audio: " + mIsPlayAudio);
            }
        });

        mSwitchVabrate = (AlarmSettingSwitch) findViewById(R.id.switch_vabrate);
        mSwitchVabrate.setCheck(mIsVabrate);
        mSwitchVabrate.setOnCheckChangeListener(new AlarmSettingSwitch.OnCheckChangeListener() {
            @Override
            public void onSwitch(boolean b) {
                mIsVabrate = b;
                log("vabrate: " + mIsVabrate);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALARM_RING_SELECTED_REQUEST_CODE
                && resultCode == RESULT_OK) {
            if (data != null) {
                mAudioPath = data.getStringExtra("audioPath");
                mDefaultAudioPostion = RingtoneUtils.getRingtonePositionByUri(this,
                        RingtoneManager.TYPE_ALARM, Uri.parse(mAudioPath));
                Log.i("TEST", "pos: " + mDefaultAudioPostion);
                String title = data.getStringExtra("audioTitle");
                mIndiRing.setValueText(title);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mTvBarCancel) {
            showQuitDialog();
        } else if (view == mTvBarSave) {
            createAlarm();
            finish();
        }
    }

    private void createAlarm() {
        Alarm alarm = new AlarmBuilder().create()
                .hour(mHour)
                .minute(mMinute)
                .repeat(mIsRepeat, mCustomDays)
                .audio(mIsPlayAudio, mAudioPath, mVolume)
                .vibrate(mIsVabrate)
                .tag(mTag)
                .build();
        log(alarm.toString());
        KnifeAlarm.getInstance().setAlarm(alarm);
    }

    private void showQuitDialog() {

    }

    private void updateSelectedDays(String selectedText) {
        String[] str = getResources().getStringArray(R.array.repeat);
        switch (selectedText) {
//            case str[0]:
//                break;
        }
    }

    /* ------------------------------------- */
    private void logAlarmMsg() {
        log("---------- Alarm -----------");
        log("hour           --> " + mHour);
        log("minute         --> " + mMinute);
        log("tag            --> " + mTag);
        log("is play audio  --> " + mIsPlayAudio);
        log("audio path     --> " + mAudioPath);
        log("volume         --> " + mVolume);
        log("is vabrate     --> " + mIsVabrate);
        log("is repeat      --> " + mIsRepeat);
        log("days           --> " + mCustomDays);
    }

    private void log(String text) {
        Log.i("TEST", text);
    }
}
