package com.example.aswin.delayedmessages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

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
        serviceIntent = new Intent(this, DelayedMessageService.class);
        serviceIntent.putExtra("message", "Good Morning");
        startService(serviceIntent);

//        serviceIntent = new Intent(this, DelayedMessageIntentService.class);
//        serviceIntent.putExtra("message", "Good Morning");
//        startService(serviceIntent);
    }

    public void stopService(View view) {
        stopService(serviceIntent);

//        Intent intent = new Intent();
//        intent.setAction(DelayedMessageIntentService.StopCommandReceiver.ACTION_STOP);
//
//        sendBroadcast(intent);
    }


    public void download(View view) {
        DownloadTask task = new DownloadTask();
        task.execute(3);
    }


    private class DownloadTask extends AsyncTask<Integer, Integer, Void> {
        int count;
        @Override
        protected Void doInBackground(Integer... params) {
            count = params[0];
            try {
                for(int i=0; i<count; i++) {
                    Thread.sleep(1000);
                    publishProgress(i+1);
                }
            } catch(Exception ex) {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int finished = values[0];

            tvDownloadStatus.setText("Finished: " + finished + " / " + count + " files");
        }

        public static final String CHANNEL_INFO = "info";
        public static final int NOTIFICATION_ID = 1;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvDownloadStatus.setText("Download Finished");

            NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_INFO)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Information")
                    .setContentText("Download Finished");
            Notification notif = notifBuilder.build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.O) {
                // Create a NotificationChannel
                NotificationChannel channel = new NotificationChannel(CHANNEL_INFO,
                        "Downlload Notification", NotificationManager
                        .IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(NOTIFICATION_ID, notif);
        }
    }
}
