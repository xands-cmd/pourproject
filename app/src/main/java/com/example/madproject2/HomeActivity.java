package com.example.madproject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    ImageView ivSelectedEmoji, yellow, blue, orange, green, red, purple, pink;
    TextView tvSelectedMood, tvFullName, journalStreak;
    ShapeableImageView profileImage;
    HashMap<String, Object> userData;
    String username, fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }

    private void initialize() {
        tvFullName = findViewById(R.id.tvFullName);
        profileImage = findViewById(R.id.profileImage);
        TextView tvDate = findViewById(R.id.tvDate);
        journalStreak = findViewById(R.id.journalStreak);

        String currentDate = new java.text.SimpleDateFormat("MM/dd/yyyy")
                .format(new java.util.Date());
        tvDate.setText(currentDate);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);

        Bitmap savedImage = (Bitmap) userData.get("profileImage");
        if (savedImage != null) {
            profileImage.setImageBitmap(savedImage);
        }

        fName = (String) userData.get("firstName");
        lName = (String) userData.get("lastName");
        tvFullName.setText(fName + " " + lName);

        ivSelectedEmoji = findViewById(R.id.ivSelectedEmoji);
        tvSelectedMood = findViewById(R.id.tvSelectedMood);

        yellow = findViewById(R.id.yellow);
        blue = findViewById(R.id.blue);
        orange = findViewById(R.id.orange);
        green = findViewById(R.id.green);
        red = findViewById(R.id.red);
        purple = findViewById(R.id.purple);
        pink = findViewById(R.id.pink);

        yellow.setOnClickListener(clicked);
        blue.setOnClickListener(clicked);
        orange.setOnClickListener(clicked);
        green.setOnClickListener(clicked);
        red.setOnClickListener(clicked);
        purple.setOnClickListener(clicked);
        pink.setOnClickListener(clicked);

        //save yung mood kahit lumipat ng different navigation

        String savedMoodText = (String) userData.get("moodText");
        Integer savedMoodColor = (Integer) userData.get("moodColor");
        Integer savedMoodImage = (Integer) userData.get("moodImage");
        if (savedMoodText != null && savedMoodColor != null && savedMoodImage != null) {
            tvSelectedMood.setText(savedMoodText);
            tvSelectedMood.setTextColor(savedMoodColor);
            ivSelectedEmoji.setImageResource(savedMoodImage);
        }

        updateStreak();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent navIntent = null;

            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_journal)
                navIntent = new Intent(this, journalLists.class);
            else if (id == R.id.nav_mood)
                navIntent = new Intent(this, moodList.class);
            else if (id == R.id.nav_lifestyle)
                navIntent = new Intent(this, lifeLatelyList.class);
            else if (id == R.id.nav_profile)
                navIntent = new Intent(this, ProfileActivity.class);

            if (navIntent != null) {
                navIntent.putExtra("username", username);
                startActivity(navIntent);
                return true;
            }
            return false;
        });
    }

    private final View.OnClickListener clicked = v -> {
        String moodText = "";
        int moodColor = 0;
        int moodImage = 0;

        if (v.getId() == R.id.yellow) {
            moodText = "I'm feeling Happy!";
            moodColor = getColor(R.color.yellow);
            moodImage = R.drawable.yellow;
        } else if (v.getId() == R.id.blue) {
            moodText = "I'm feeling Sad!";
            moodColor = getColor(R.color.blue);
            moodImage = R.drawable.blue;
        } else if (v.getId() == R.id.orange) {
            moodText = "I'm feeling Anxious!";
            moodColor = getColor(R.color.orange);
            moodImage = R.drawable.orange;
        } else if (v.getId() == R.id.green) {
            moodText = "I'm feeling Sick!";
            moodColor = getColor(R.color.green);
            moodImage = R.drawable.green;
        } else if (v.getId() == R.id.red) {
            moodText = "I'm feeling Angry!";
            moodColor = getColor(R.color.red);
            moodImage = R.drawable.red;
        } else if (v.getId() == R.id.purple) {
            moodText = "I'm feeling Scared!";
            moodColor = getColor(R.color.purple);
            moodImage = R.drawable.purple;
        } else if (v.getId() == R.id.pink) {
            moodText = "I'm feeling Loved!";
            moodColor = getColor(R.color.pink);
            moodImage = R.drawable.pink;
        }

        tvSelectedMood.setText(moodText);
        tvSelectedMood.setTextColor(moodColor);
        ivSelectedEmoji.setImageResource(moodImage);

        userData.put("moodText", moodText);
        userData.put("moodColor", moodColor);
        userData.put("moodImage", moodImage);
        UserDatabase.updateUser(username, userData);
    };

    //streak count para sa journals everytime na ioopen yung home
    @Override
    protected void onResume() {
        super.onResume();
        updateStreak();
    }

    //function para makuha yung count ng journals from the journal navigation from the userdatabase class
    private void updateStreak() {
        userData = UserDatabase.getUser(username);
        ArrayList<JournalEntry> journalList = (ArrayList<JournalEntry>) userData.get("journals");
        int streak = (journalList != null) ? journalList.size() : 0;
        journalStreak.setText("Streak " + streak);
    }
}
