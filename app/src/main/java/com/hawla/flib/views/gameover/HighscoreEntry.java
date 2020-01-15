package com.hawla.flib.views.gameover;

public class HighscoreEntry {

    private String name;
    private int score;

    HighscoreEntry(String name, int score){
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
