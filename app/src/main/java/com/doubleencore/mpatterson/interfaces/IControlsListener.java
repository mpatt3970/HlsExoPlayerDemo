package com.doubleencore.mpatterson.interfaces;

/**
 * Created by michael on 11/27/15.
 */
public interface IControlsListener {

    void onPause();
    void onPlay();
    void onSeekTo(float percentComplete);
    void onControlsHidden();
    void onControlsShown();
}
