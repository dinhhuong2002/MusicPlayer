package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final int PAUSE = 0;
    private static final int PLAY = 1;
    private static MediaPlayer player = new MediaPlayer();
    private static final int STATE_IDE = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;
    private List<Song> listsong = new ArrayList<>();
    private TextView tvName, tvSinger, tvTime;
    private SeekBar seekBar;
    private ImageView ivPlay;
    private int index;
    private Song song;
    private Thread thread;
    private int state = STATE_IDE;
    private String totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPlay = findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        findViewById(R.id.iv_next).setOnClickListener(this);
        findViewById(R.id.iv_previous).setOnClickListener(this);
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.tv_time);
        tvSinger = findViewById(R.id.tv_singer);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        //Tạo ds bài hát
        listsong = getListSong();
        //lấy đối tượng RecyclerView từ layout file
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        //Khai báo loại giao diện cho RecyclerView (1 cột - nhiều hàng)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Đổ Adapter vào RecyclerView
        recyclerView.setAdapter(new SongAdapter(this, listsong));
    }

    //Lấy danh sách bài hát
    private List<Song> getListSong() {
        List<Song> songList = new ArrayList<Song>();

        songList.add(new Song("Animal", R.raw.animal, "03:51", "   Maroon 5"));
        songList.add(new Song("Bad Habits", R.raw.badhabit, "03:50", "    Ed Sheeran"));
        songList.add(new Song("Bang Bang Bang", R.raw.bangbangbang, "03:39", "    Big Bang"));
        songList.add(new Song("Black space", R.raw.blackspace, "03:51", "     Taylor Swift"));
        songList.add(new Song("Chạy ngay đi", R.raw.chayngaydi, "04:31", "    Sơn Tùng M-TP"));
        songList.add(new Song("Chúng ta không thuộc về nhau", R.raw.chungtakhongthuocvenhau, "03:51", "   Sơn Tùng M-TP"));
        songList.add(new Song("Double Take", R.raw.doubletake, "04:00", "    Dusk Till Dawn"));
        songList.add(new Song("Hãy trao cho anh", R.raw.haytraochoanh, "04:05", "    Sơn Tùng M-TP"));
        songList.add(new Song("Không phải dạng vừa đâu",R.raw.khongphaidangvuadau,"04:06","    Sơn Tùng M-TP"));
        songList.add(new Song("Look What You Make Me Do", R.raw.lookwhatyoumakemedo, "03:32", "    Taylor Swift"));
        songList.add(new Song("Mascara", R.raw.mascara, "04:55", "    Chillies"));
        songList.add(new Song("Mơ Hồ",R.raw.moho,"04:20",   "Bùi Anh Tuấn"));
        songList.add(new Song("Nàng Thơ",R.raw.nangtho,"04:15","Hoàng Dũng"));
        songList.add(new Song("Positions", R.raw.positions, "02:51", "    Ariana Grande"));
        songList.add(new Song("Shut Down", R.raw.shutdown, "02:55", "    Black Pink"));
        songList.add(new Song("Thank You, Next", R.raw.thankyounext, "03:26", "    Ariana Grande"));

        return songList;
    }

    //xử lý sự kiện Click
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_play) {
            playPause();
        } else if (v.getId() == R.id.iv_next) {
            next();
        } else if (v.getId() == R.id.iv_previous) {
            back();
        }
    }

    public void playSong(Song song) {
        index = listsong.indexOf(song);
        this.song = song;
        play();
    }

    private void back() {
        if (index == 0) {
            index = listsong.size() - 1;
        } else {
            index--;
        }
        play();
    }

    private void next() {
        if (index == listsong.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        play();
    }

    private void playPause() {
        if (state == STATE_PLAYING && player.isPlaying()) {
            player.pause();
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause_icon));
            state = STATE_PAUSE;
        } else if (state == STATE_PAUSE) {
            player.start();
            state = STATE_PLAYING;
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_icon));
        } else {
            play();
        }
    }

    private void play() {
        song = listsong.get(index);
        tvName.setText(song.getName());
        tvSinger.setText(song.getSinger());
        player.reset();
        try {
            player = MediaPlayer.create(this, song.getPath());
            player.start();
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_icon));
            state = STATE_PLAYING;
            totalTime = getTime(player.getDuration());
            seekBar.setMax(player.getDuration());
            if (thread == null) {
                startLooping();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startLooping() {
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        return;
                    }
                    runOnUiThread(() -> updateTime());
                }
            }
        };
        thread.start();
    }

    private void updateTime() {
        if (state == STATE_PLAYING || state == STATE_PAUSE) {
            int time = player.getCurrentPosition();
            tvTime.setText(String.format("%s/%s", getTime(time), totalTime));
            seekBar.setProgress(time);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getTime(int time) {
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    @Override

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (state == STATE_PLAYING || state == STATE_PAUSE) {
            player.seekTo(seekBar.getProgress());
        }
    }
}