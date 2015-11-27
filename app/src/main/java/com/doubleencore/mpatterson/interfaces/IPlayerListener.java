package com.doubleencore.mpatterson.interfaces;

/**
 * Created by michael on 11/27/15.
 */
public interface IPlayerListener {

    void onBufferingStart();
    void onBufferingComplete();
    void onSetDuration(long durationMs);
    void onUpdateProgress(float percentComplete);
}
