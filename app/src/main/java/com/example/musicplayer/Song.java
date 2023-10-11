package com.example.musicplayer;

public class Song {
    private String name, singer, duration;
    private int path;

    public Song(String name, int path, String duration, String singer) {
        this.name = name;
        this.path = path;
        this.singer = singer;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getPath() {
        return path;
    }

    public String getSinger() {
        return singer;
    }
}