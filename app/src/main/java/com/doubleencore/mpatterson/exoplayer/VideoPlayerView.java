package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.doubleencore.mpatterson.interfaces.IControlsListener;
import com.doubleencore.mpatterson.interfaces.IPlayerListener;
import com.google.android.exoplayer.ExoPlayer;

/**
 * Created by michael on 11/26/15.
 * Use this view to communicate with controls and listen to player state
 */
public class VideoPlayerView extends AbsVideoPlayer implements IControlsListener {

    private IPlayerListener mListener;
    private boolean mShouldSetDuration;

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(IPlayerListener listener) {
        mListener = listener;
        mShouldSetDuration = true;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.v(EXOPLAYER_LISTENER, "onPlayerStateChanged");
        switch(playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                mListener.onBufferingStart();
                break;
            case ExoPlayer.STATE_READY:
                mListener.onBufferingComplete();
                if (mShouldSetDuration) {
                    mListener.onSetDuration(mExoPlayer.getDuration());
                    mListener.onUpdateProgress(0);
                    mShouldSetDuration = false;
                }
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
