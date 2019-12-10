package com.cocofire.qadaptation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class TransScrollView extends NestedScrollView {
    public TransScrollView(@NonNull Context context) {
        super(context);
    }

    public TransScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchEventListener mListener;

    public void setListener(TouchEventListener mListener) {
        this.mListener = mListener;
    }

    public interface TouchEventListener {
        boolean dispatchTouchEvent(MotionEvent event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mListener != null && mListener.dispatchTouchEvent(ev)) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
