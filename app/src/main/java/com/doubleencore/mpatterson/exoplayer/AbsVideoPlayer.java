package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.util.PlayerControl;

/**
 * Created by michael on 11/26/15.
 * This class handles underlying business to set up the exoplayer
 */
public abstract class AbsVideoPlayer extends AbsVideoPlayerImpl implements AudioCapabilitiesReceiver.Listener,
        HlsRendererBuilder.Listener {

    private static final String TAG = AbsVideoPlayer.class.getSimpleName();

    // custom vars for the exoplayer
    public static final int RENDERER_COUNT = 2;
    private static final int MIN_BUFFER_MS = 1500;
    private static final int MIN_REBUFFER_MS = 4000;
    public static final int BUFFER_SEGMENT_SIZE = 64*1024;
    public static final int BUFFER_SEGMENT_COUNT = 150;
    public static final int ALLOWED_JOIN_TIME_MS = 5000;
    public static final int MAX_DROPPED_FRAMES = 50;
    // renderer positions
    public static final int VIDEO_RENDERER = 0;
    public static final int AUDIO_RENDERER = 1;

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
        mHandler= new Handler();
        mBuilder = new HlsRendererBuilder();
    }

    public void destroyPlayer() {
        mAudioCapabilitiesReceiver.unregister();
        releasePlayer();
    }

    public void play(String url) {
        mUrl = url;
    }

    public void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            mPlayerController = null;
            mBuilder.cancel();
        }
    }

    public void preparePlayer() {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT, MIN_BUFFER_MS, MIN_REBUFFER_MS);
            mPlayerController = new PlayerControl(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.addListener(this);
            mBuilder.build(getContext(), this, mUrl);
        }
    }

    public Handler getMainHandler() {
        return mHandler;
    }

    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        // called when phone has  hdmi cord plugged in or unplugged
        // wont worry about it for this demo cause I dont have a good way to test
        // should probably release then re-prepare the player
    }

    @Override
    public void onSuccess(TrackRenderer[] renderers) {
        Log.d(TAG, "Success!");
    }

    @Override
    public void onFailure(Exception e) {
        Log.e(TAG, "HlsRendererBuilder failed", e);
    }
}
