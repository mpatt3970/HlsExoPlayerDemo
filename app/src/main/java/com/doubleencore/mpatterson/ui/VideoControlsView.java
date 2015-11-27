package com.doubleencore.mpatterson.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.doubleencore.mpatterson.R;

/**
 * Created by michael on 11/27/15.
 */
public class VideoControlsView extends RelativeLayout {

    public VideoControlsView(Context context) {
        this(context, null);
    }

    public VideoControlsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.video_controls, this, true);
    }
}
