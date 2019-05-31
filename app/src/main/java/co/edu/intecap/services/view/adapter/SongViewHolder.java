package co.edu.intecap.services.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import co.edu.intecap.services.R;
import co.edu.intecap.services.model.Song;
import co.edu.intecap.services.view.listeners.SongEventListner;

class SongViewHolder  extends RecyclerView.ViewHolder {

    public SongEventListner songEventLister;
    TextView tvSongName;
    Song song;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSongName = itemView.findViewById(R.id.tv_song_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songEventLister != null){
                    songEventLister.onSongSelected(song);
                }
            }
        });
    }
}
