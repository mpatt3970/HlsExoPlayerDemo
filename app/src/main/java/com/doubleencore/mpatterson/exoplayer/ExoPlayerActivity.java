package com.doubleencore.mpatterson.exoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import com.doubleencore.mpatterson.R;
import com.doubleencore.mpatterson.controls.ControlsContainer;
import com.doubleencore.mpatterson.controls.VideoControlsView;
import com.doubleencore.mpatterson.interfaces.IEndedListener;

/**
 * Created by michael on 9/2/15.
 */
public class ExoPlayerActivity extends Activity implements IEndedListener, AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = ExoPlayerActivity.class.getSimpleName();

    public static final String EXTRA_URL = "extra_url";

    private VideoPlayerView mPlayerView;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        mPlayerView = (VideoPlayerView) findViewById(R.id.player_view);

        VideoControlsView controlsView = new VideoControlsView(this);
        controlsView.setControlsListener(mPlayerView);

        ControlsContainer controlsContainer = (ControlsContainer) findViewById(R.id.controls_container);
        controlsContainer.addView(controlsView);

        mPlayerView.setPlayerListener(controlsView);
        mPlayerView.setEndedListener(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);
            mPlayerView.setUrl(url);
        } else {
            showError();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.preparePlayer();
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.destroyPlayer();
    }

    private void showError() {
        Toast.makeText(this, R.string.video_player_error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEnded() {
        finish();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // dont do anything
                // should happen after onResume when we requestAudioFocus
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mPlayerView.onPause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // just keep playing for short interruptions, ie notification noises
                break;
        }
    }
}
