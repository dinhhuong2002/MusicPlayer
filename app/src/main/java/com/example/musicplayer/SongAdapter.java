package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private static final String name = SongAdapter.class.getName();
    private final List<Song> songList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvSinger;

        public SongHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSinger = itemView.findViewById(R.id.tv_singer);
            itemView.setOnClickListener(v -> {
                v.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                ((MainActivity) context).playSong((Song) tvName.getTag());
            });

        }
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Lấy view từ res/Layout/item_song.xml
        View itemSong = layoutInflater.inflate(R.layout.item_song, parent, false);
        return new SongHolder(itemSong);
    }

    @Override
    public void onBindViewHolder(SongAdapter.SongHolder holder, int position) {
        Song item = songList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSinger.setText(item.getSinger());
        holder.tvName.setTag(item);

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
