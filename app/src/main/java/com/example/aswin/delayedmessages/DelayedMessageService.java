package com.example.aswin.delayedmessages;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DelayedMessageService extends Service {

    Thread thread;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mainHandler = new Handler(DelayedMessageService.this.getMainLooper());

        final String message = intent.getStringExtra("message");

        //1. main thread: working, but can it be stopped?

//        try {
//            Thread.sleep(5000);
//            Toast.makeText(DelayedMessageService.this, message, Toast.LENGTH_SHORT).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //2. worker thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(3000);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DelayedMessageService.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
