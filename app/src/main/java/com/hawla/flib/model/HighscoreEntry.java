package com.hawla.flib.model;

public class HighscoreEntry {

    private String name;
    private int score;

    public HighscoreEntry(String name, int score){
        this.name = name;
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public String getName(){
        return name;
    }
}
