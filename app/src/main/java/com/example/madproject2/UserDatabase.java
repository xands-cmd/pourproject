package com.example.madproject2;

import java.util.HashMap;

public class UserDatabase {
    private static final HashMap<String, HashMap<String, Object>> usersDB = new HashMap<>();

    public static boolean userExists(String username) {
        return usersDB.containsKey(username);
    }

    public static void registerUser(String username, HashMap<String, Object> userData) {
        usersDB.put(username, userData);
    }

    public static HashMap<String, Object> getUser(String username) {
        return usersDB.get(username);
    }

//    public static boolean validateLogin(String username, String password) {
//        if (!usersDB.containsKey(username)) return false;
//        return usersDB.get(username).get("password").equals(password);
//    }
    public static boolean validateLogin(String username, String password) {
        if (!usersDB.containsKey(username)) return false;

        // Stored password is now an Object → cast to String
        return usersDB.get(username).get("password").equals(password);
    }
}
