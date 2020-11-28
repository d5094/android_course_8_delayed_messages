package com.example.aswin.delayedmessages;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.MainThread;

public class DelayedMessageIntentService extends IntentService {

    public static boolean shouldStop = true;

    public class StopCommandReceiver extends BroadcastReceiver {
        public static final String ACTION_STOP = "stop";

        @Override
        public void onReceive(Context context, Intent intent) {
            shouldStop = true;
        }
    }

    Handler mainHandler;
    public DelayedMessageIntentService() {
        super("DelayedMessageIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //add broadcast receiver to reeceive stop command
        IntentFilter filter = new IntentFilter(StopCommandReceiver.ACTION_STOP);
        StopCommandReceiver stopReceiver =  new StopCommandReceiver();
        registerReceiver(stopReceiver, filter);


        final String message = intent.getStringExtra("message");



        try {
            shouldStop = false;

            // 1. not working. Why?
            // Answer: because IntentService runs in background thread.
//            Thread.sleep(5000);
//            Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_SHORT).show();

            // 2. working, run once, then stop
//            mainHandler = new Handler(DelayedMessageIntentService.this.getMainLooper());
//            Thread.sleep(5000);
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_SHORT).show();
//                }
//            });

            //3. loop forever, how to stop?
            mainHandler = new Handler(DelayedMessageIntentService.this.getMainLooper());
            while(!shouldStop) {
                Thread.sleep(3000);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DelayedMessageIntentService.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //unregister to prevent memory leak
        unregisterReceiver(stopReceiver);
    }
}
