package com.cocofire.qadaptation;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.cocofire.coco_runtime.CocoBinder;
import com.cocofire.my_annotation.CocoBindView;

import androidx.core.widget.NestedScrollView;


public class MainActivity extends Activity {

    @CocoBindView(R.id.map_view)
    NestedScrollView mapView;
    @CocoBindView(R.id.trans_scrollview)
    TransScrollView scrollView;
    @CocoBindView(R.id.transparent_view)
    TransparentView transparentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CocoBinder.bind(this);

        transparentView.setTargetView(mapView);

        scrollView.setListener(event -> {
            int[] rect = new int[2];
            transparentView.getLocationOnScreen(rect);
            int right = rect[0] + transparentView.getMeasuredWidth();
            int bottom = rect[1] + transparentView.getMeasuredHeight();
            Rect r = new Rect(rect[0], rect[1], right, bottom);
            return r.contains((int) event.getRawX(), (int) event.getRawY());
        });

//        ExecutorService tpe = Executors.newCachedThreadPool();
//        for (int i = 0; i < 100; i++)
//            tpe.execute(new ES("onCreate " + i));
//
//        Log.e(Thread.currentThread().toString(), "finally");
    }

    private class ES implements Runnable {
        private String msg;

        private ES(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                Log.e(Thread.currentThread().toString(), msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//                Log.e(Thread.currentThread().toString(), msg + "  finally");
            }
        }
    }


}
