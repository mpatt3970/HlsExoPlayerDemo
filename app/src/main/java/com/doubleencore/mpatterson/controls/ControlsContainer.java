package com.doubleencore.mpatterson.controls;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by michael on 11/27/15.
 */
public class ControlsContainer extends FrameLayout {

    private static final int HIDE_DELAY = 4117;

    private VideoControlsView mControls;
    private Handler mHandler;

    public ControlsContainer(Context context) {
        this(context, null);
    }

    public ControlsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mControls != null) {
                    mControls.setVisibility(VISIBLE);
                    mHandler.removeCallbacks(mHideControls);
                    mHandler.postDelayed(mHideControls, HIDE_DELAY);
                }
            }
        });
        mHandler.postDelayed(mHideControls, HIDE_DELAY);
    }

    private Runnable mHideControls = new Runnable() {
        @Override
        public void run() {
            if (mControls.isBuffering()) {
                mHandler.postDelayed(mHideControls, 1000);
            } else {
                mControls.setVisibility(GONE);
            }
        }
    };

    @Override
    public void addView(View child) {
        super.addView(child);
        if (mControls == null && child instanceof VideoControlsView) {
            mControls = (VideoControlsView) child;
        } else if (mControls != null) {
            throw new IllegalStateException("ControlsContainer can only hold one view");
        } else {
            throw new IllegalStateException("ControlsContainer can only have sub views of type VideoControlsView");
        }
    }
}
