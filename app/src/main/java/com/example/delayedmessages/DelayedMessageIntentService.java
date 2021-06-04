package com.example.delayedmessages;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class DelayedMessageIntentService extends IntentService {
    boolean isRunning = false;

    public DelayedMessageIntentService() {
        super("DelayedMessageIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String message = intent.getStringExtra("message");
        final Handler mainHandler = new Handler(this.getMainLooper());
        isRunning = false;

        //1. does it work? NO
        // because intent service runs on worker thread
//        try {
//            Thread.sleep(5000);
//            Log.v("Intent Service", "sending message");
//            Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_LONG).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //2. does it work?
        try {
            Thread.sleep(5000);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //3. loop
//        isRunning = true;
//        while(isRunning) {
//            try {
//                Thread.sleep(5000);
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}