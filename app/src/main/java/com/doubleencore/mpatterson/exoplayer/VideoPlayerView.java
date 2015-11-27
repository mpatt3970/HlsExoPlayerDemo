package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.doubleencore.mpatterson.interfaces.IControlsListener;
import com.google.android.exoplayer.ExoPlayer;

/**
 * Created by michael on 11/26/15.
 */
public class VideoPlayerView extends AbsVideoPlayer implements IControlsListener {

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.v(EXOPLAYER_LISTENER, "onPlayerStateChanged");
        switch(playbackState) {
            case ExoPlayer.STATE_IDLE:
                Log.v(EXOPLAYER_LISTENER, "exoplayer state = idle");
                break;
            case ExoPlayer.STATE_PREPARING:
                Log.v(EXOPLAYER_LISTENER, "exoplayer state = preparing");
                break;
            case ExoPlayer.STATE_BUFFERING:
                Log.v(EXOPLAYER_LISTENER, "exoplayer state = buffering");
                break;
            case ExoPlayer.STATE_READY:
                Log.v(EXOPLAYER_LISTENER, "exoplayer state = ready");
                break;
            case ExoPlayer.STATE_ENDED:
                Log.v(EXOPLAYER_LISTENER, "exoplayer state = ended");
                break;
        }
    }

    @Override
    public void onPause() {
        if (mPlayerController != null) {
            mPlayerController.pause();
        }
    }

    @Override
    public void onPlay() {
        if (mPlayerController != null) {
            mPlayerController.start();
        }
    }

    @Override
    public void onSeekTo(float percentComplete) {
        if (mPlayerController != null && mExoPlayer != null) {
            mPlayerController.seekTo((int) (mExoPlayer.getDuration()*percentComplete));
        }
    }
}
