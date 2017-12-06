package com.example.sherif.skullmp3;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SongAdapter.SongSelector, SeekBar.OnSeekBarChangeListener {
    private int playingIndex=-1,screenHeight,screenWidth;
    private boolean isPlaying=false,playerActivated=true;

    private Song currentSong;
    private List<Song> songs;
    private SongAdapter mAdapter;
    private SongFetcher fetcherReference;
    private MediaPlayer mPlayer;
    private Handler mHandler = new Handler();
    private RecyclerDivider divider;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.player_container) LinearLayout playerContainer;
    @BindView(R.id.icon_forward) ImageButton forward;
    @BindView(R.id.icon_rewind) ImageButton rewind;
    @BindView(R.id.icon_pause_play) ImageButton pausePlay;
    @BindView(R.id.seekbar) SeekBar seekBar;
    @BindView(R.id.txt_song) TextView songName;
    @BindView(R.id.txt_artist) TextView songArtist;
    @BindView(R.id.recycler_songs) RecyclerView songsRecycler;
    @BindView(R.id.layout_current) RelativeLayout currentlyPlayingLayout;

    private int pauseRes=R.drawable.pause50;
    private int playRes=R.drawable.play50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initRecycler();
    }

    private void initRecycler(){
        mAdapter=new SongAdapter(songs,this,this);
        divider=new RecyclerDivider(this,LinearLayoutManager.VERTICAL);
        layoutManager=new LinearLayoutManager(this);
        songsRecycler.setLayoutManager(layoutManager);
        songsRecycler.setItemAnimator(new DefaultItemAnimator());
        songsRecycler.addItemDecoration(divider);
        songsRecycler.setAdapter(mAdapter);
        songsRecycler.setVisibility(View.GONE);
    }

    private void init(){
        songName.setSelected(true);
        fetcherReference=SongFetcher.getReference(this);
        songs=fetcherReference.fetchSongs();
        getScreenDimens();
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(this);
        currentSong=fetcherReference.fetchFirstSong();
        mPlayer=MediaPlayer.create(this, Uri.parse(currentSong.getPath()));
        displaySong();
    }

    private void getScreenDimens(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    private void lowerPlayer() {
        playerContainer.animate().translationYBy(screenHeight).start();
        currentlyPlayingLayout.setVisibility(View.VISIBLE);
    }

    private void liftPlayer() {
        playerContainer.animate().translationYBy(-screenHeight).start();
        currentlyPlayingLayout.setVisibility(View.GONE);

    }


    @OnClick(R.id.icon_pause_play)
    public void play_pause(){
        if(isPlaying) mPlayer.pause();
        else mPlayer.start();
        pausePlay.setImageResource(isPlaying?playRes:pauseRes);
        isPlaying=!isPlaying;
    }

    @OnClick(R.id.icon_forward)
    public void nextSong(){
        selectNextSong();
    }

    private void selectNextSong() {
        if(fetcherReference.canGetNextSong(playingIndex)){
            currentSong=fetcherReference.fetchNextSong(playingIndex);
            playingIndex++;
            setSong();
        }
    }

    @OnClick(R.id.icon_rewind)
    public void previousSong(){
        if(fetcherReference.canGetPreviousSong(playingIndex) && playingIndex>=0){
            currentSong=fetcherReference.fetchPreviousSong(playingIndex);
            playingIndex--;
            mPlayer.stop();
            setSong();
        }
    }

    public void play(){
        isPlaying=true;
        pausePlay.setImageResource(pauseRes);
        this.mPlayer.start();
    }

    @Override
    public void selectSong(Song song, int index) {
        currentSong=song;
        playingIndex=index;
        playerActivated=true;
        songsRecycler.setVisibility(View.GONE);
        setSong();
        liftPlayer();
    }

    private void setSong(){
        if(isPlaying)
            mPlayer.stop();
        if(!seekBar.isEnabled())
            seekBar.setEnabled(true);
        this.mPlayer=MediaPlayer.create(this, Uri.parse(currentSong.getPath()));
        play();
        displaySong();

        this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pausePlay.setImageResource(playRes);
                isPlaying=false;
                selectNextSong();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(playerActivated){
            playerActivated=false;
            songsRecycler.setVisibility(View.VISIBLE);
            lowerPlayer();
        }else{
            super.onBackPressed();
        }
    }

    public void displaySong(){
        songName.setText(currentSong.getName());
        songArtist.setText(currentSong.getArtist());
    }

    private Runnable songUpdater=new Runnable() {
        @Override
        public void run() {
            if(isPlaying){
                seekBar.setProgress(mPlayer.getCurrentPosition());
                mHandler.postDelayed(this,100);
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
       float p=(float)i/100;
        if(currentSong!=null) {
            if (!isPlaying) {
                play();
            }mPlayer.seekTo((int) (p * mPlayer.getDuration()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
