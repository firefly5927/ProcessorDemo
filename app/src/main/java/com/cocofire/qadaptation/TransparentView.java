package com.cocofire.qadaptation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TransparentView extends View {
    public TransparentView(Context context) {
        super(context);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private View targetView;

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (targetView != null)
            return targetView.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
