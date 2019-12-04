package com.cocofire.qadaptation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cocofire.coco_runtime.CocoBinder;
import com.cocofire.my_annotation.CocoBindView;
import com.cocofire.my_annotation.CocoClick;


public class MainActivity extends AppCompatActivity {

    @CocoBindView(R.id.button2)
    Button btn2;
    @CocoBindView(R.id.button3)
    Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CocoBinder.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @CocoClick({R.id.button3, R.id.button2, R.id.button1})
    public void publish(View view) {
        switch (view.getId()) {
            case R.id.button3:
                btn3.setText("bind btn3");
                break;
            case R.id.button2:
                btn2.setText("bind btn2");
                break;
            case R.id.button1:
                btn3.setText("btn3 change");
                btn2.setText("btn2 change");
                break;
            default:
                break;
        }

    }
}
