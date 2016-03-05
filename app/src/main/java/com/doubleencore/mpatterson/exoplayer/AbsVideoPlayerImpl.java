package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.media.MediaCodec;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.hls.HlsSampleSource;

import java.io.IOException;

/**
 * Created by michael on 11/26/15.
 */
public abstract class AbsVideoPlayerImpl extends FrameLayout implements HlsSampleSource.EventListener,
        MediaCodecVideoTrackRenderer.EventListener, MediaCodecAudioTrackRenderer.EventListener,
        ExoPlayer.Listener, SurfaceHolder.Callback {

    private static final String SAMPLE_SOURCE_LISTENER = "SampleSource.Listener";
    private static final String TRACK_RENDERER_LISTENER = "TrackRenderer.Listener";
    private static final String VIDEO_TRACK_LISTENER = "VideoTrack.Listener";
    private static final String AUDIO_TRACK_LISTENER = "AudioTrack.Listener";
    protected static final String EXOPLAYER_LISTENER = "ExoPlayer.Listener";
    private static final String SURFACE_HOLDER_CALLBACK = "SurfaceHolder.Callback";


    public AbsVideoPlayerImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        Log.v(SAMPLE_SOURCE_LISTENER, "onLoadStarted");
    }

    @Override
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        Log.v(SAMPLE_SOURCE_LISTENER, "onLoadCompleted");
    }

    @Override
    public void onLoadCanceled(int sourceId, long bytesLoaded) {
        Log.v(SAMPLE_SOURCE_LISTENER, "onLoadCanceled");
    }

    @Override
    public void onLoadError(int sourceId, IOException e) {
        Log.e(SAMPLE_SOURCE_LISTENER, "onLoadError", e);
    }

    @Override
    public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {
        Log.v(SAMPLE_SOURCE_LISTENER, "onUpstreamDiscarded");
    }

    @Override
    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {
        Log.v(SAMPLE_SOURCE_LISTENER, "onDownstreamFormatChanged");
    }

    @Override
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        Log.e(TRACK_RENDERER_LISTENER, "onDecoderInitializationError", e);
    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e) {
        Log.e(TRACK_RENDERER_LISTENER, "onCryptoError", e);
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        Log.v(TRACK_RENDERER_LISTENER, "onDecoderInitialized");
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        Log.v(VIDEO_TRACK_LISTENER, "onDroppedFrames");
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(VIDEO_TRACK_LISTENER, "onVideoSizeChanged");
    }

    @Override
    public void onDrawnToSurface(Surface surface) {
        Log.v(VIDEO_TRACK_LISTENER, "onDrawnToSurface");
    }

    @Override
    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
        Log.v(AUDIO_TRACK_LISTENER, "onAudioTrackInitializationError");
    }

    @Override
    public void onAudioTrackWriteError(AudioTrack.WriteException e) {
        Log.e(AUDIO_TRACK_LISTENER, "onAudioTrackWriteError", e);
    }

    @Override
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        Log.e(AUDIO_TRACK_LISTENER, "onAudioTrackUnderrun");
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        Log.v(EXOPLAYER_LISTENER, "onPlayWhenReadyCommitted");
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        Log.e(EXOPLAYER_LISTENER, "onPlayerError", e);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(SURFACE_HOLDER_CALLBACK, "surfaceChanged");
    }
}
