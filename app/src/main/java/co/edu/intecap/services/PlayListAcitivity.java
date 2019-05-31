package co.edu.intecap.services;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.edu.intecap.services.model.Song;
import co.edu.intecap.services.view.adapter.SongsAdapter;
import co.edu.intecap.services.view.listeners.SongEventListner;

public class PlayListAcitivity extends AppCompatActivity implements SongEventListner {

    private RecyclerView rvSongs;
    private Button btnPlayStop;
    private TextView txtSongName;
    private CardView cardView;
    private String path;
    static String songName;
    public static boolean playing = false;
    private SongsAdapter songAdapter;
    private int REQUEST_READ_EXTERNAL_STORAGE = 10;
    private String songPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_acitivity);
        setupViews();
        setupSongList();

        // If Android Marshmallow or above, then check if permission is granted
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            loadSongs();
        }
    }


    private void setupViews() {
        //initializing views
        btnPlayStop = findViewById(R.id.btnPlayStop);
        txtSongName = findViewById(R.id.txtSongName);
        cardView = findViewById(R.id.cardView);
        rvSongs = findViewById(R.id.rv_songs);

        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    //If song is playing and user clicks on Stop button
                    //Stop the song by calling stopService() and change boolean value
                    //text on button should be changed to 'Play'
                    playing = false;
                    btnPlayStop.setText("Play");
                    Intent intent = new Intent(PlayListAcitivity.this, MusicService.class);

                    stopService(intent);
                } else if (!playing) {
                    //If song is not playing and user clicks on Play button
                    //Start the song by calling startService() and change boolean value
                    //text on button should be changed to 'Stop'
                    playing = true;
                    btnPlayStop.setText("Stop");
                    Intent intent = new Intent(PlayListAcitivity.this, MusicService.class);
                    intent.putExtra(MusicService.AUDIO_PATH, songPath);
                    startService(intent);
                }
            }
        });

    }


    void loadSongs() {
        //If music is playing already on opening starting the app, player should be visible with Stop button
        if (playing) {
            txtSongName.setText(songName);
            cardView.setVisibility(View.VISIBLE);
            btnPlayStop.setText("Stop");
        }

        path = Environment.getExternalStorageDirectory().getAbsolutePath();

        List<Song> songList = findAudiFiles(path);
        Log.d("Songs", "loadSongs:   songList.size()" + songList.size());
        songAdapter.setSongList(songList);
        songAdapter.notifyDataSetChanged();
    }

    private void setupSongList() {
        songAdapter = new SongsAdapter();
        songAdapter.setSongEventListner(this);
        rvSongs.setAdapter(songAdapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
    }

    //Fetching .mp3 and .mp4 files from phone storage
    private List<Song> findAudiFiles(String path) {
        List<Song> songList = new ArrayList<>();
        try {
            File file = new File(path);
            File[] filesArray = file.listFiles();
            String fileName;
            for (File file1 : filesArray) {
                if (file1.isDirectory()) {
                    songList.addAll(findAudiFiles(file1.getAbsolutePath()));
                } else {
                    fileName = file1.getName();
                    if ((fileName.endsWith(".mp3"))) {
                        Log.d("Songs", "adding!! filename: " + fileName);
                        Log.d("Songs", "adding!! path: " + file1.getAbsolutePath());
                        songList.add(new Song(file1.getName(), file1.getAbsolutePath()));
                    }

                }
            }

            return songList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //Handling permissions for Android Marshmallow and above
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted, initialize the views
            loadSongs();
        } else {
            //show the dialog requesting to grant permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
            } else {
                //permission is denied (this is the first time, when "never ask again" is not checked)
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    finish();
                }
                //permission is denied (and never ask again is  checked)
                else {
                    //shows the dialog describing the importance of permission, so that user should grant
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You have forcefully denied Read storage permission.\n\nThis is necessary for the working of app." + "\n\n" + "Click on 'Grant' to grant permission")
                            //This will open app information where user can manually grant requested permission
                            .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            //close the app
                            .setNegativeButton("Don't", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.setCancelable(false);
                    builder.create().show();
                }
            }
        }
    }


    @Override
    public void onSongSelected(Song song) {


        //player is visible
        cardView.setVisibility(View.VISIBLE);

        //If some other song is already playing, stop the service
        if (playing) {
            Intent i = new Intent(PlayListAcitivity.this, MusicService.class);
            stopService(i);
        }

        playing = true;
        songPath = song.getAbsolutePath();

        //getting absolute path of selected song from bean class 'SongObject'

        //Play the selected song by starting the service
        Intent start = new Intent(PlayListAcitivity.this, MusicService.class);
        start.putExtra(MusicService.AUDIO_PATH, song.getAbsolutePath());
        startService(start);

        //Get and set the name of song in the player
        txtSongName.setText(song.getFileName());
        btnPlayStop.setText("Stop");
    }
}