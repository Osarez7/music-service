package co.edu.intecap.services.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.edu.intecap.services.R;
import co.edu.intecap.services.model.Song;
import co.edu.intecap.services.view.listeners.SongEventListner;

public class SongsAdapter extends RecyclerView.Adapter<SongViewHolder> {

    private List<Song> songList;
    private SongEventListner songEventListner;

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.item_song, viewGroup, false);
        return  new  SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int position) {
           Song song = songList.get(position);
           songViewHolder.song = song;
           songViewHolder.tvSongName.setText(song.getFileName());
           songViewHolder.songEventLister = songEventListner;
    }


    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public SongEventListner getSongEventListner() {
        return songEventListner;
    }

    public void setSongEventListner(SongEventListner songEventListner) {
        this.songEventListner = songEventListner;
    }

    @Override
    public int getItemCount() {
       if(songList == null){
           return  0;
       }else{
           return songList.size();
       }
    }
}
