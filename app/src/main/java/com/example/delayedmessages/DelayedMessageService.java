package com.example.delayedmessages;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class DelayedMessageService extends Service {
    Thread thread;
    boolean isRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mainHandler = new Handler(this.getMainLooper());
        final String message = intent.getStringExtra("message");
        isRunning = false;

        // 1. main thread: it works, but can it be stopped? NO
//        try {
//            Thread.sleep(5000);
//            Toast.makeText(DelayedMessageService.this, message, Toast.LENGTH_SHORT).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 2.
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                try {
                    while(isRunning) {
                        Thread.sleep(3000);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DelayedMessageService.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}