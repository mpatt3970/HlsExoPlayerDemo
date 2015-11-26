package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.util.PlayerControl;

/**
 * Created by michael on 11/26/15.
 * This class handles underlying business to set up the exoplayer
 */
public class AbsVideoPlayer extends AbsVideoPlayerImpl implements AudioCapabilitiesReceiver.Listener {

    private String mUrl;
    private SurfaceHolder mSurfaceHolder;
    private HlsRendererBuilder mBuilder;
    private ExoPlayer mExoPlayer;
    protected PlayerControl mPlayerController;
    private AudioCapabilitiesReceiver mAudioCapabilitiesReceiver;
    private Handler mHandler;

    public AbsVideoPlayer(Context context) {
        this(context, null);
    }

    public AbsVideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        AspectRatioFrameLayout frame = new AspectRatioFrameLayout(this.getContext());
        this.addView(frame);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) frame.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        SurfaceView surfaceView = new SurfaceView(this.getContext());
        mSurfaceHolder = surfaceView.getHolder();
        frame.addView(surfaceView);
        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(getContext(), this);
        mAudioCapabilitiesReceiver.register();
    }

    public void destroyPlayer() {
        mAudioCapabilitiesReceiver.unregister();
        releasePlayer();
    }

    public void play(String url) {
        mUrl = url;
    }

    public void releasePlayer() {

    }

    public void preparePlayer() {

    }

    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        // called when phone has  hdmi cord plugged in or unplugged
        // wont worry about it for this demo cause I dont have a good way to test
        // should probably release then re-prepare the player
    }
}
