package com.example.madproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class journalLists extends AppCompatActivity {

    Context c = this;
    Button addJournal;
    LinearLayout journalEntries;

    HashMap<String, Object> userData;
    ArrayList<JournalEntry> journalList;
    String username = "";

    int dpDesc = 150;
    int moodDP = 70;
    int marginTB = 15;
    int marginLR = 30;
    int numberM = 10;

    int journalCounter = 1;

    ActivityResultLauncher<Intent> journalFormLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");
        userData = UserDatabase.getUser(username);

        journalList = (ArrayList<JournalEntry>) userData.get("journals");

        journalEntries = findViewById(R.id.journalEntries);
        addJournal = findViewById(R.id.addJournal);

        journalFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null)
                            return;

                        Intent data = result.getData();
                        String action = data.getStringExtra("action");
                        int entryId = data.getIntExtra("entryId", -1);

                        if ("delete".equals(action)) {
                            deleteJournal(entryId);
                            return;
                        }

                        if ("edit".equals(action)) {
                            Intent editIntent = new Intent(c, journalform.class);
                            editIntent.putExtra("current1", data.getStringExtra("title"));
                            editIntent.putExtra("current2", data.getStringExtra("feel"));
                            editIntent.putExtra("moodChange", data.getIntExtra("mood", R.drawable.dizzy));
                            editIntent.putExtra("entryId", entryId);
                            journalFormLauncher.launch(editIntent);
                            return;
                        }

                        String title = data.getStringExtra("journalTitle");
                        String feel = data.getStringExtra("journalFeel");
                        int mood = data.getIntExtra("journalMood", R.drawable.dizzy);

                        if (entryId != -1) {
                            updateJournal(entryId, title, feel, mood);
                        }
                        else {
                            addNewJournal(title, feel, mood);
                        }
                    }
                }
        );


        addJournal.setOnClickListener(v -> {
            Intent addJournalIntent = new Intent(c, journalform.class);
            journalFormLauncher.launch(addJournalIntent);
        });

        loadSavedJournals();
        setupBottomNav();
    }

    //load ung mga existing journals
    private void loadSavedJournals() {
        journalEntries.removeAllViews();
        journalCounter = 1;

        for (JournalEntry j : journalList) {
            addJournalEntry(j);
            journalCounter++;
        }
    }

    private void addNewJournal(String title, String feel, int mood) {
        int id = View.generateViewId();
        JournalEntry entry = new JournalEntry(title, feel, mood, id);

        journalList.add(entry);
        UserDatabase.updateUser(username, userData);

        addJournalEntry(entry);
        journalCounter++;
    }

    private void updateJournal(int entryId, String title, String feel, int mood) {

        for (JournalEntry j : journalList) {
            if (j.entryId == entryId) {
                j.title = title;
                j.feel = feel;
                j.mood = mood;
                break;
            }
        }
        UserDatabase.updateUser(username, userData);
        loadSavedJournals();
    }

    private void deleteJournal(int entryId) {
        journalList.removeIf(j -> j.entryId == entryId);
        UserDatabase.updateUser(username, userData);
        loadSavedJournals();
    }

    private void addJournalEntry(JournalEntry entry) {
        float scale = getResources().getDisplayMetrics().density;

        int pixelValue = (int) (dpDesc * scale + 0.5f);
        int moodPixel = (int) (moodDP * scale + 0.5f);
        int margin15 = (int) (marginTB * scale + 0.5f);
        int margin30 = (int) (marginLR * scale + 0.5f);
        int number10 = (int) (numberM * scale + 0.5f);

        LinearLayout containerList = new LinearLayout(c);
        containerList.setOrientation(LinearLayout.HORIZONTAL);
        containerList.setPadding(75, 75, 75, 75);
        containerList.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams parentMargin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parentMargin.setMargins(margin30, margin15, margin30, margin15);
        containerList.setLayoutParams(parentMargin);

        containerList.setBackgroundResource(R.drawable.round);
        containerList.setId(entry.entryId);

        TextView numberList = new TextView(c);
        numberList.setText(String.valueOf(journalCounter));
        numberList.setTextSize(40);
        numberList.setTypeface(null, Typeface.BOLD);
        numberList.setPadding(0, 0, number10, 0);
        numberList.setTextColor(Color.parseColor("#8D815C"));
        numberList.setGravity(Gravity.CENTER);

        View leftLine = new View(c);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(number10, 0, number10, 0);
        leftLine.setLayoutParams(dividerParams);
        leftLine.setBackgroundColor(Color.parseColor("#717C68"));

        LinearLayout journalDesc = new LinearLayout(c);
        journalDesc.setOrientation(LinearLayout.VERTICAL);
        journalDesc.setLayoutParams(new LinearLayout.LayoutParams(pixelValue, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView titleList = new TextView(c);
        titleList.setText(entry.title);
        titleList.setTextSize(20);
        titleList.setTextColor(Color.parseColor("#8D815C"));
        titleList.setTypeface(null, Typeface.BOLD);

        TextView feelList = new TextView(c);
        feelList.setText(entry.feel);
        feelList.setTextSize(15);
        feelList.setTextColor(Color.parseColor("#8D815C"));

        journalDesc.addView(titleList);
        journalDesc.addView(feelList);

        View rightLine = new View(c);
        LinearLayout.LayoutParams dividerParams2 = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        dividerParams2.setMargins(number10, 0, number10, 0);
        rightLine.setLayoutParams(dividerParams2);
        rightLine.setBackgroundColor(Color.parseColor("#717C68"));

        ImageView moodList = new ImageView(c);
        LinearLayout.LayoutParams moodParams = new LinearLayout.LayoutParams(moodPixel, moodPixel);
        moodParams.setMargins(number10, 0, 0, 0);
        moodParams.gravity = Gravity.CENTER_VERTICAL;
        moodList.setLayoutParams(moodParams);
        moodList.setImageResource(entry.mood);

        containerList.addView(numberList);
        containerList.addView(leftLine);
        containerList.addView(journalDesc);
        containerList.addView(rightLine);
        containerList.addView(moodList);

        containerList.setOnClickListener(v -> {
            Intent viewJournal = new Intent(c, individualJournal.class);
            viewJournal.putExtra("entryId", entry.entryId);
            viewJournal.putExtra("title", entry.title);
            viewJournal.putExtra("feel", entry.feel);
            viewJournal.putExtra("mood", entry.mood);
            journalFormLauncher.launch(viewJournal);
        });

        journalEntries.addView(containerList);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            Intent navIntent = null;

            if (item.getItemId() == R.id.nav_home)
                navIntent = new Intent(this, HomeActivity.class);
            else if (item.getItemId() == R.id.nav_mood)
                navIntent = new Intent(this, moodList.class);
            else if (item.getItemId() == R.id.nav_lifestyle)
                navIntent = new Intent(this, lifeLatelyList.class);
            else if (item.getItemId() == R.id.nav_profile)
                navIntent = new Intent(this, ProfileActivity.class);
            else if (item.getItemId() == R.id.nav_journal)
                return true;

            if (navIntent != null) {
                navIntent.putExtra("username", username);
                startActivity(navIntent);
                return true;
            }

            return false;
        });
    }
}
