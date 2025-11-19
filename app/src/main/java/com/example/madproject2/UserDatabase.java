package com.example.madproject2;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDatabase {

    private static final HashMap<String, HashMap<String, Object>> usersDB = new HashMap<>();

    public static boolean userExists(String username) {
        return usersDB.containsKey(username);
    }

    public static void registerUser(String username, HashMap<String, Object> userData) {

        if (!userData.containsKey("journals")) {
            userData.put("journals", new ArrayList<JournalEntry>());
        }

        if (!userData.containsKey("moods")) {
            userData.put("moods", new ArrayList<MoodEntry>());
        }

        if (!userData.containsKey("life")) {
            userData.put("life", new ArrayList<>());
        }

        usersDB.put(username, userData);
    }

    public static HashMap<String, Object> getUser(String username) {
        HashMap<String, Object> user = usersDB.get(username);
        if (user == null) return null;

        if (!user.containsKey("journals")) {
            user.put("journals", new ArrayList<JournalEntry>());
        }

        if (!user.containsKey("moods")) {
            user.put("moods", new ArrayList<MoodEntry>());
        }

        if (!user.containsKey("life")) {
            user.put("life", new ArrayList<>());
        }

        return user;
    }

    public static void updateUser(String username, HashMap<String, Object> data) {
        usersDB.put(username, data);
    }

    public static boolean validateLogin(String username, String password) {
        if (!usersDB.containsKey(username)) return false;
        return usersDB.get(username).get("password").equals(password);
    }
}
