package com.cocofire.coco_runtime;

import android.view.View;

public abstract class CocoOnClickListener implements View.OnClickListener {

    static boolean enabled = true;

    private static final Runnable ENABLE_AGAIN = () -> enabled = true;

    @Override
    public void onClick(View v) {
        if (enabled) {
            enabled = false;
            v.post(ENABLE_AGAIN);
            cocoOnClick(v);
        }
    }

    public abstract void cocoOnClick(View view);
}
