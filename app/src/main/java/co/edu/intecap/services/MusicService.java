package co.edu.intecap.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MusicService extends Service {

    public static final String AUDIO_PATH = "audio_path";
    public static final String AUDIO_NAME = "audio_name";
    public static final String TAG = MusicService.class.getSimpleName();
    private static final String CHANNEL_ID = "music_service";
    private MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(intent.getStringExtra(AUDIO_PATH));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            showNotification(intent.getStringExtra(AUDIO_NAME));

        } catch (IOException e) {
            Log.i(TAG, "Error: " + e.toString());
        }

        return START_STICKY;
    }

    private void showNotification(String songName) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Playing")
                .setContentText(songName)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(100);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
