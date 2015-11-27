package com.doubleencore.mpatterson.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.doubleencore.mpatterson.R;
import com.doubleencore.mpatterson.interfaces.IControlsListener;
import com.doubleencore.mpatterson.interfaces.IPlayerListener;

/**
 * Created by michael on 11/27/15.
 */
public class VideoControlsView extends RelativeLayout implements IPlayerListener {

    private IControlsListener mListener;

    private ImageView mPlayButton;
    private ProgressBar mBuffering;
    private SeekBar mSeekBar;
    private TextView mTimer;

    public VideoControlsView(Context context) {
        this(context, null);
    }

    public VideoControlsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.video_controls, this, true);
        mPlayButton = (ImageView) v.findViewById(R.id.play_button);
        mBuffering = (ProgressBar) v.findViewById(R.id.loading_indicator);
        mSeekBar = (SeekBar) v.findViewById(R.id.seekbar);
        mTimer = (TextView) v.findViewById(R.id.progress_timer);
        setClickListeners();
//        showBuffering();
        hideBuffering();
    }

    private void setClickListeners() {
        mPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayButton.setActivated(!mPlayButton.isActivated());
                // at this moment if activated, should pause, else should play
                if (mPlayButton.isActivated()) {
                    mListener.onPause();
                } else {
                    mListener.onPlay();
                }
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mListener.onSeekTo(seekBar.getProgress()/(float) seekBar.getMax());
            }
        });
    }

    private void showBuffering() {
        mPlayButton.setVisibility(GONE);
        mBuffering.setVisibility(VISIBLE);
    }

    private void hideBuffering() {
        mBuffering.setVisibility(GONE);
        mPlayButton.setVisibility(VISIBLE);
    }

    public void setControlsListener(IControlsListener listener) {
        mListener = listener;
    }
}
