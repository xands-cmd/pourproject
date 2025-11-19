package com.example.madproject2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

public class lifeLatelyList extends AppCompatActivity {

    Context c = this;
    Button lifeEntry;
    LinearLayout lifeStyleMain;

    int small = 30;

    ActivityResultLauncher<Intent> lifeFormLauncher;
    HashMap<String, Object> userData;
    String username = "";

    ArrayList<LifeEntry> lifeEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_life_lately_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lifeLatelyList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });


        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);


        lifeEntries = (ArrayList<LifeEntry>) userData.get("life");

        lifeStyleMain = findViewById(R.id.lifeStyleMain);


        loadSavedEntries();

        lifeFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent getLife = result.getData();

                        ArrayList<Integer> cbIcons = getLife.getIntegerArrayListExtra("cbIcons");
                        String noteLife = getLife.getStringExtra("lifeNote");

                        // Add new entry
                        addLifeEntry(cbIcons, noteLife, true);
                    }
                }
        );

        addLifeButton();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent homeIntent = new Intent(lifeLatelyList.this, HomeActivity.class);
                homeIntent.putExtra("username", username);
                startActivity(homeIntent);
                return true;
            } else if (id == R.id.nav_journal) {
                Intent journalIntent = new Intent(lifeLatelyList.this, journalLists.class);
                journalIntent.putExtra("username", username);
                startActivity(journalIntent);
                return true;
            } else if (id == R.id.nav_mood) {
                Intent moodIntent = new Intent(lifeLatelyList.this, moodList.class);
                moodIntent.putExtra("username", username);
                startActivity(moodIntent);
                return true;
            } else if (id == R.id.nav_lifestyle) {
                return true;
            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(lifeLatelyList.this, ProfileActivity.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    private void addLifeButton() {
        lifeEntry = findViewById(R.id.lifeEntry);
        lifeEntry.setOnClickListener(v -> {
            Intent addLifeStyle = new Intent(c, lifelatelyForm.class);
            lifeFormLauncher.launch(addLifeStyle);
        });
    }


    private void addLifeEntry(ArrayList<Integer> icons, String note, boolean saveToDB) {


        Date current = new Date();
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a");

        String date = dateFmt.format(current);
        String time = timeFmt.format(current);

        LifeEntry entry = new LifeEntry(icons, note, date, time);

        if (saveToDB) {
            lifeEntries.add(entry);
            userData.put("life", lifeEntries);
            UserDatabase.updateUser(username, userData);
        }


        createLifeLayout(entry);
    }

    private void loadSavedEntries() {
        lifeStyleMain.removeAllViews();
        for (LifeEntry entry : lifeEntries) {
            createLifeLayout(entry);
        }
    }


    private void createLifeLayout(LifeEntry entry) {

        float scale = getResources().getDisplayMetrics().density;
        int size20 = (int) (small * scale + 0.5f);


        LinearLayout timeDate = new LinearLayout(c);
        timeDate.setOrientation(LinearLayout.HORIZONTAL);
        timeDate.setGravity(Gravity.CENTER);
        timeDate.setPadding(0, 0, 0, 20);

        TextView spaceBoth = new TextView(c);
        spaceBoth.setText(entry.date + "     " + entry.time);
        spaceBoth.setTextSize(20);

        timeDate.addView(spaceBoth);
        lifeStyleMain.addView(timeDate);

        LinearLayout cbIcon = new LinearLayout(c);
        cbIcon.setOrientation(LinearLayout.HORIZONTAL);
        cbIcon.setGravity(Gravity.CENTER);
        cbIcon.setPadding(0, 0, 0, 20);

        if (entry.icons != null) {
            for (int iconEach : entry.icons) {
                ImageView img = new ImageView(c);
                img.setImageResource(iconEach);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size20, size20);
                params.setMargins(5, 5, 5, 5);
                img.setLayoutParams(params);
                cbIcon.addView(img);
            }
        }

        lifeStyleMain.addView(cbIcon);

        TextView life = new TextView(c);
        life.setText(entry.note);
        life.setTextSize(15);
        life.setPadding(0, 0, 0, 10);
        life.setGravity(Gravity.CENTER);

        lifeStyleMain.addView(life);

        Button deleteBtn = new Button(c);
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        deleteBtn.setLayoutParams(deleteParams);
        deleteBtn.setText("Delete");
        deleteBtn.setGravity(Gravity.CENTER);
        deleteBtn.setBackgroundColor(Color.parseColor("#AA3333"));
        deleteBtn.setTextColor(Color.WHITE);

        deleteBtn.setOnClickListener(v -> {
            lifeEntries.remove(entry);
            userData.put("life", lifeEntries);
            UserDatabase.updateUser(username, userData);
            loadSavedEntries();
        });

        lifeStyleMain.addView(deleteBtn);

        View bottomLine = new View(c);
        LinearLayout.LayoutParams dividerParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        dividerParams.setMargins(0, 30, 0, 30);
        bottomLine.setLayoutParams(dividerParams);
        bottomLine.setBackgroundColor(Color.parseColor("#6C6E1F"));

        lifeStyleMain.addView(bottomLine);
    }
}
