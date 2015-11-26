package com.doubleencore.mpatterson.exoplayer;

import android.content.Context;

import com.doubleencore.mpatterson.R;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
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
    private AbsVideoPlayer mPlayer;
    private String mUrl;

    public HlsRendererBuilder() { }

    public void build(Context context, AbsVideoPlayer player, String url) {
        mContext = context;
        mUserAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        mPlayer = player;
        mUrl = url;
        HlsPlaylistParser parser = new HlsPlaylistParser();
        ManifestFetcher<HlsPlaylist> playlistFetcher = new ManifestFetcher<>(mUrl,
                new DefaultUriDataSource(mContext, mUserAgent), parser);
        playlistFetcher.singleLoad(mPlayer.getMainHandler().getLooper(), this);
    }

    @Override
    public void onSingleManifest(HlsPlaylist manifest) {
    }

    @Override
    public void onSingleManifestError(IOException e) {
        mPlayer.onFailure(e);
    }
}
