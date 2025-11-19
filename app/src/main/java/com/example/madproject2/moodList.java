package com.example.madproject2;

import com.example.madproject2.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class moodList extends AppCompatActivity {

    Button moodCRUD;
    Context c = this;

    LinearLayout iconLayout;

    ActivityResultLauncher<Intent> moodFormLauncher;
    HashMap<String, Object> userData;
    ArrayList<MoodEntry> moodEntries;
    String username = "";
    int moodCounter = 1;

    // para mastore yung moods, linked sa drawables
    private static final HashMap<Integer, MoodInfo> MOOD_MAP = new HashMap<>();
    static {
        MOOD_MAP.put(R.drawable.scared, new MoodInfo("Scared", R.color.purple));
        MOOD_MAP.put(R.drawable.happy, new MoodInfo("Happy", R.color.yellow));
        MOOD_MAP.put(R.drawable.angry, new MoodInfo("Angry", R.color.red));
        MOOD_MAP.put(R.drawable.dizzy, new MoodInfo("Dizzy", R.color.orange));
        MOOD_MAP.put(R.drawable.drool, new MoodInfo("Drool", R.color.green));
        MOOD_MAP.put(R.drawable.kiss, new MoodInfo("Smooch", R.color.pink));
        MOOD_MAP.put(R.drawable.sad, new MoodInfo("Sad", R.color.blue));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mood_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        username = getIntent().getStringExtra("username");
        userData = UserDatabase.getUser(username);

        // kuhanin yung current na nagawang mood and store it sa moodEntries
        if (!userData.containsKey("moods")) {
            userData.put("moods", new ArrayList<MoodEntry>());
        }
        moodEntries = (ArrayList<MoodEntry>) userData.get("moods");

        iconLayout = findViewById(R.id.iconLayout);

        // crud ng moods
        moodFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null)
                            return;

                        Intent data = result.getData();
                        String action = data.getStringExtra("action");
                        int moodId = data.getIntExtra("moodId", -1);

                        if ("delete".equals(action)) {
                            deleteMood(moodId);
                            return;
                        }

                        int moodRes = data.getIntExtra("moodCurrent", R.drawable.dizzy);

                        if (moodId != -1) {
                            updateMood(moodId, moodRes);
                        }
                        else {
                            addNewMood(moodRes);
                        }
                    }
                }
        );
        moodLister();
        loadSavedMoods();
        setupBottomNav();
    }


    // kunin yung mga nagawang moods na
    private void loadSavedMoods() {
        iconLayout.removeAllViews();
        moodCounter = 1;
        for (MoodEntry m : moodEntries) {
            addMoodRow(m);
            moodCounter++;
        }
    }

    private void addNewMood(int moodRes) {
        int id = View.generateViewId();
        MoodEntry m = new MoodEntry(moodRes, id);
        moodEntries.add(m);
        UserDatabase.updateUser(username, userData);

        addMoodRow(m);
        moodCounter++;
    }
    private void updateMood(int moodId, int moodRes) {
        for (MoodEntry m : moodEntries) {
            if (m.moodId == moodId) {
                m.moodRes = moodRes;
                break;
            }
        }
        UserDatabase.updateUser(username, userData);
        loadSavedMoods();
    }

    private void deleteMood(int moodId) {
        moodEntries.removeIf(m -> m.moodId == moodId);
        UserDatabase.updateUser(username, userData);
        loadSavedMoods();
    }

   // paglagay ng mood sa list from the form

    private void addMoodRow(MoodEntry moodEntry) {

        Button deleteBtn = new Button(c);
        deleteBtn.setText("DELETE");
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        deleteParams.gravity = Gravity.CENTER_VERTICAL;
        deleteBtn.setLayoutParams(deleteParams);
        deleteBtn.setTextColor(Color.WHITE);
        deleteBtn.setBackgroundColor(getResources().getColor(R.color.red));

        deleteBtn.setOnClickListener(v -> deleteMood(moodEntry.moodId));


        LinearLayout row = new LinearLayout(c);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20, 20, 20, 20);

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        rowParams.setMargins(20, 15, 20, 15);
        row.setLayoutParams(rowParams);
        row.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(c);
        LinearLayout.LayoutParams iconSize =
                new LinearLayout.LayoutParams(200, 200);
        iconSize.gravity = Gravity.CENTER;
        icon.setLayoutParams(iconSize);
        icon.setImageResource(moodEntry.moodRes);

        TextView moodText = new TextView(c);
        moodText.setText(getMoodLabel(moodEntry.moodRes));
        moodText.setTextSize(30);
        moodText.setTypeface(null, Typeface.BOLD);
        moodText.setTextColor(getMoodColor(moodEntry.moodRes));

        TextView dateNow = new TextView(c);
        dateNow.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateNow.setGravity(Gravity.START);

        TextView timeNow = new TextView(c);
        timeNow.setText(new SimpleDateFormat("hh:mm a").format(new Date()));
        timeNow.setGravity(Gravity.START);

        LinearLayout textColumn = new LinearLayout(c);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setPadding(30, 0, 30, 0);
        textColumn.setGravity(Gravity.CENTER);

        textColumn.addView(moodText);
        textColumn.addView(dateNow);
        textColumn.addView(timeNow);

        row.addView(icon);
        row.addView(textColumn);
        row.addView(deleteBtn);
        row.setBackgroundResource(R.drawable.round);
        row.setId(moodEntry.moodId);

        row.setOnClickListener(v -> {
            Intent viewMood = new Intent(c, moodForm.class);
            viewMood.putExtra("moodId", moodEntry.moodId);
            viewMood.putExtra("moodCurrent", moodEntry.moodRes);
            moodFormLauncher.launch(viewMood);
        });

        iconLayout.addView(row);
    }

    private String getMoodLabel(int moodRes) {
        MoodInfo info = MOOD_MAP.get(moodRes);
        return (info != null) ? info.label : "Unknown";
    }

    private int getMoodColor(int moodRes) {
        MoodInfo info = MOOD_MAP.get(moodRes);
        return (info != null) ? getResources().getColor(info.colorRes) : getResources().getColor(R.color.black);
    }

    // punta sa moodform
    private void moodLister() {
        moodCRUD = findViewById(R.id.moodCRUD);

        moodCRUD.setOnClickListener(v -> {
            Intent switchMood = new Intent(c, moodForm.class);
            moodFormLauncher.launch(switchMood);
        });
    }
    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            Intent navIntent = null;

            if (item.getItemId() == R.id.nav_home)
                navIntent = new Intent(this, HomeActivity.class);
            else if (item.getItemId() == R.id.nav_journal)
                navIntent = new Intent(this, journalLists.class);
            else if (item.getItemId() == R.id.nav_lifestyle)
                navIntent = new Intent(this, lifeLatelyList.class);
            else if (item.getItemId() == R.id.nav_profile)
                navIntent = new Intent(this, ProfileActivity.class);
            else if (item.getItemId() == R.id.nav_mood)
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

