package com.example.sherif.skullmp3;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherif on 9/21/2017.
 */
public class SongFetcher {
    private static SongFetcher instance;
    private Context mContext;
    private List<Song> songList;
    private SongFetcher(Context context){
        //singleton constructor
        mContext=context;
        songList=new ArrayList<>();
    }

    public static SongFetcher getReference(Context context){
        if(instance==null){
            instance=new SongFetcher(context);
        }return instance;
    }


    public List<Song> fetchSongs(){
        ContentResolver musicResolver = mContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int pathColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);

            do {
                String path= musicCursor.getString(pathColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisTitle, thisArtist,path));
            }
            while (musicCursor.moveToNext());
        }
        return getSongList();
    }

    public List<Song> getSongList(){
        return this.songList;
    }

    public boolean canGetNextSong(int index){
        return ((index+1)<songList.size());
    }

    public boolean canGetPreviousSong(int index){
        return((index-1)>=0);
    }

    public Song fetchNextSong(int index){
        return songList.get(index+1);
    }

    public Song fetchPreviousSong(int index){
        return songList.get(index-1);
    }

    public Song fetchFirstSong(){
        return songList.get(0);
    }


}
