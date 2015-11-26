package com.doubleencore.mpatterson.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.doubleencore.mpatterson.R;
import com.doubleencore.mpatterson.exoplayer.VideoControllerView;

/**
 * Created by michael on 9/2/15.
 */
public class VideoPlayerActivity extends Activity {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();

    public static final String EXTRA_URL = "extra_url";

    private VideoControllerView mPlayerView;

    private String mVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        mPlayerView = (VideoControllerView) findViewById(R.id.player_view);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            mVideoUrl = intent.getStringExtra(EXTRA_URL);
        } else {
            showError();
        }
    }




    private void showError() {
        Toast.makeText(this, R.string.video_player_error, Toast.LENGTH_SHORT).show();
        finish();
    }

}
