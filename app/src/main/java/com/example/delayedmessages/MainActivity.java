package com.example.delayedmessages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent serviceIntent;
    TextView tvDownloadStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDownloadStatus = findViewById(R.id.tvDownloadStatus);
    }

    public void sendMessage(View view) {
        //Service
        serviceIntent = new Intent(this, DelayedMessageService.class);
        serviceIntent.putExtra("message", "Good morning");
        startService(serviceIntent);

        //Intent Service
//        serviceIntent = new Intent(this, DelayedMessageIntentService.class);
//        serviceIntent.putExtra("message", "Good morning");
//        startService(serviceIntent);
    }

    public void stopService(View view) {
        stopService(serviceIntent);
    }

    public void download(View view) {
        DownloadTask task = new DownloadTask();
        task.execute(3);
    }

    //Params
    //Progress
    //Result
    private class DownloadTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... values) {
            try {
                int count = values[0];
                for(int i=0; i<count; i++) {
                    Thread.sleep(4000);
                    publishProgress(i+1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int f = values[0];
            String info = String.format("Downloaded %d / 3 files", f);
            tvDownloadStatus.setText(info);
        }

        private static final String CHANNEL_INFO = "info";
        private static final int NOTIF_ID = 1;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvDownloadStatus.setText("Download Finished");

            NotificationCompat.Builder notifBuilder =
                    new NotificationCompat.Builder(MainActivity.this, CHANNEL_INFO)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Download Finished")
                    .setContentText("Your download is finished");

            Notification notification = notifBuilder.build();

            NotificationManager notifManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.O) {
                // Create a NotificationChannel
                NotificationChannel channel = new NotificationChannel(CHANNEL_INFO,
                        "Download Notification", NotificationManager
                        .IMPORTANCE_HIGH);
                notifManager.createNotificationChannel(channel);
            }

            notifManager.notify(NOTIF_ID, notification);

        }
    }
}