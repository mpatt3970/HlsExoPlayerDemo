package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Handler;

import com.doubleencore.mpatterson.R;
import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.hls.DefaultHlsTrackSelector;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.HlsTrackSelector;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;

import java.io.IOException;

/**
 * Created by michael on 11/26/15.
 */
public class HlsRendererBuilder implements ManifestFetcher.ManifestCallback<HlsPlaylist> {

    public interface Listener {
        void onSuccess(TrackRenderer[] renderers);
        void onFailure(Exception e);
    }

    private Context mContext;
    private String mUserAgent;
    private String mUrl;
    private Handler mHandler;
    private AbsVideoPlayer mPlayer;
    private boolean mCanceled;

    public HlsRendererBuilder() { }

    public void build(Context context, AbsVideoPlayer player, String url) {
        mContext = context;
        mUserAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        mHandler = player.getMainHandler();
        mPlayer = player;
        mUrl = url;

        HlsPlaylistParser parser = new HlsPlaylistParser();
        ManifestFetcher<HlsPlaylist> playlistFetcher = new ManifestFetcher<>(mUrl,
                new DefaultUriDataSource(mContext, mUserAgent), parser);
        mCanceled = false;
        playlistFetcher.singleLoad(mHandler.getLooper(), this);
    }

    public void cancel() {
        mCanceled = true;
    }

    @Override
    public void onSingleManifest(HlsPlaylist manifest) {
        if (mCanceled) return;

        if (manifest == null || ! (manifest instanceof HlsMasterPlaylist)) {
            mPlayer.onFailure(new IllegalStateException("Failed to retrieve a valid hlsPlaylist"));
            return;
        }

        HlsMasterPlaylist masterPlaylist = (HlsMasterPlaylist) manifest;
        int[] variants;
        try {
            variants = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(mContext, masterPlaylist.variants, null, false);
        } catch (Exception e) {
            mPlayer.onFailure(e);
            return;
        }
        if (variants.length == 0) {
            mPlayer.onFailure(new IllegalStateException("No variants selected from manifest"));
            return;
        }

        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(AbsVideoPlayer.BUFFER_SEGMENT_SIZE));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource dataSource = new DefaultUriDataSource(mContext, bandwidthMeter, mUserAgent);

        HlsChunkSource chunkSource = new HlsChunkSource(true, dataSource, mUrl, manifest,
                DefaultHlsTrackSelector.newDefaultInstance(mContext), bandwidthMeter,
                new PtsTimestampAdjusterProvider(), HlsChunkSource.ADAPTIVE_MODE_SPLICE);
        HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl,
                AbsVideoPlayer.BUFFER_SEGMENT_SIZE * AbsVideoPlayer.BUFFER_SEGMENT_COUNT, mHandler, mPlayer, 0);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(mContext, sampleSource,
                MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING,
                AbsVideoPlayer.ALLOWED_JOIN_TIME_MS, mHandler, mPlayer, AbsVideoPlayer.MAX_DROPPED_FRAMES);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
                MediaCodecSelector.DEFAULT, null, true, mHandler, mPlayer);

        TrackRenderer[] renderers = new TrackRenderer[AbsVideoPlayer.RENDERER_COUNT];
        renderers[AbsVideoPlayer.VIDEO_RENDERER] = videoRenderer;
        renderers[AbsVideoPlayer.AUDIO_RENDERER] = audioRenderer;
        mPlayer.onSuccess(renderers);
    }

    @Override
    public void onSingleManifestError(IOException e) {
        if (mCanceled) return;

        mPlayer.onFailure(e);
    }
}
