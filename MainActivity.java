package com.example.stopwatch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_start;
    private Button btn_reset;
    private Button btn_record;
    private TextView timeView;
    private TextView recordView;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    int buttonCount = 0;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_record = (Button) findViewById(R.id.btn_record);
        timeView = (TextView) findViewById(R.id.timeView);
        recordView = (TextView) findViewById(R.id.recordView);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonCount == 0) {
                    timeThread = new Thread(new timeThread());
                    timeThread.start();
                    isRunning = true;
                    btn_start.setText("Pause");
                    buttonCount ++;
                } else {
                    isRunning = false;
                    btn_start.setText("Start");
                    buttonCount --;
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonCount == 0) {
                    i = 0;
                    recordView.setText("");
                    timeView.setText("00:00:00.00");
                }
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordView.setText(recordView.getText() + timeView.getText().toString() + "\n");
            }
        });
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            @SuppressLint("DefaultLocale")
            String result = String.format("%02d:%02d:%02d.%02d", hour, min, sec, mSec);

            timeView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.arg1 = i++;
                handler.sendMessage(msg);
            }
        }
    }
} 