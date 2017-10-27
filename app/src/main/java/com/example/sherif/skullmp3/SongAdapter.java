package com.example.sherif.skullmp3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sherif on 9/21/2017.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{


   private List<Song> songList;
    private SongSelector selectorReference;
    private Context context;
    private LayoutInflater inflater;

    public SongAdapter(List<Song> songList,SongSelector selectorReference,Context context){
        this.songList=songList;
        this.selectorReference=selectorReference;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.song_recycler_item,parent,false);
        return new SongHolder(v);
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        holder.recyclerSongName.setText(songList.get(position).getName());
        holder.recyclerArtistName.setText(songList.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    interface SongSelector{
        public void selectSong(Song song,int index);
    }

    class SongHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recycler_item_txt_name) TextView recyclerSongName;
        @BindView(R.id.recycler_item_txt_artist) TextView recyclerArtistName;

        public SongHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.recycler_item_txt_name,R.id.recycler_item_txt_artist})
        public void interceptClick(){
            selectorReference.selectSong(songList.get(getAdapterPosition()),getAdapterPosition());
        }
    }
}
