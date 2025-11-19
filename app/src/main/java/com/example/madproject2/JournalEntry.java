package com.example.madproject2;

public class JournalEntry {
    public String title;
    public String feel;
    public int mood;
    public int entryId;

    public JournalEntry(String title, String feel, int mood, int entryId) {
        this.title = title;
        this.feel = feel;
        this.mood = mood;
        this.entryId = entryId;
    }
}
