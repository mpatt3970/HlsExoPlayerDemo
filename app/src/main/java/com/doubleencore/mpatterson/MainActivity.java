package com.doubleencore.mpatterson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.doubleencore.mpatterson.exoplayer.ExoPlayerActivity;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = (ImageView) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExoPlayerActivity.class);
                intent.putExtra(ExoPlayerActivity.EXTRA_URL, getString(R.string.video_url));
                startActivity(intent);
            }
        });
    }
}
