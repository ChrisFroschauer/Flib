package com.hawla.flib.model;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class HighscoreEntryTest {

    @Test
    public void test_highscoreEntry_constructor() {
        String name = "BLA";
        int score = 10000;
        // create:
        HighscoreEntry highscoreEntry = new HighscoreEntry(name, score);
        // check
        assertEquals(name, highscoreEntry.getName());
        assertEquals(score, highscoreEntry.getScore());
    }
}
