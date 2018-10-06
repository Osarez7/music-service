package co.edu.intecap.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {

    public static final String AUDIO_PATH = "audio_path";
    public static final String TAG = MusicService.class.getSimpleName();
    private MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(intent.getStringExtra(AUDIO_PATH));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        } catch (IOException e) {
            Log.i(TAG, "Error: " + e.toString());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
