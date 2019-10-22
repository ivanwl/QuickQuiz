package com.example.ivanwl.myquizapp;

public class Player {
    private String name;
    private String score;

    public Player(String newName, String newScore)
    {
        name = newName;
        score = newScore;
    }

    public String getName()
    {
        return name;
    }

    public String getScore()
    {
        return score;
    }
}
