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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class journalLists extends AppCompatActivity {

    Context c = this;
    Button addJournal;
    LinearLayout journalEntries;

    Button switcah;

    // constants
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.journalEntries), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        journalEntries = findViewById(R.id.journalEntries);
        addJournal = findViewById(R.id.addJournal);

        journalFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String action = data.getStringExtra("action");

                            if ("delete".equals(action)) {
                                int id = data.getIntExtra("entryId", -1);
                                View entry = findViewById(id);
                                if (entry != null) {
                                    journalEntries.removeView(entry);
                                }
                                return;
                            }

                            if ("edit".equals(action)) {
                                Intent editIntent = new Intent(c, journalform.class);
                                editIntent.putExtra("current1", data.getStringExtra("title"));
                                editIntent.putExtra("current2", data.getStringExtra("feel"));
                                editIntent.putExtra("moodChange", data.getIntExtra("mood", R.drawable.dizzy));
                                editIntent.putExtra("entryId", data.getIntExtra("entryId", -1)); // ✅ add this
                                journalFormLauncher.launch(editIntent);
                                return;
                            }

                            String title = data.getStringExtra("journalTitle");
                            String feel = data.getStringExtra("journalFeel");
                            int mood = data.getIntExtra("journalMood", R.drawable.dizzy);
                            int entryId = data.getIntExtra("entryId", -1);

                            if (entryId != -1) {
                                View v = findViewById(entryId);
                                if (v instanceof LinearLayout) {
                                    LinearLayout container = (LinearLayout) v;
                                    LinearLayout journalDesc = (LinearLayout) container.getChildAt(2);
                                    TextView titleList = (TextView) journalDesc.getChildAt(0);
                                    TextView feelList = (TextView) journalDesc.getChildAt(1);
                                    ImageView moodList = (ImageView) container.getChildAt(4);
                                    titleList.setText(title);
                                    feelList.setText(feel);
                                    moodList.setImageResource(mood);
                                }
                            } else {
                                addJournalEntry(title, feel, mood);
                            }
                        }

                    }
                }
        );

        addJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addJournalIntent = new Intent(c, journalform.class);
                journalFormLauncher.launch(addJournalIntent);
            }
        });

        //switcah = findViewById(R.id.switcah);

//        switcah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent switching = new Intent (c, lifeLatelyList.class);
//                startActivity(switching);
//            }
//        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(journalLists.this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_journal) {
                //startActivity(new Intent(journalLists.this, journalLists.class));
                return true;
            } else if (id == R.id.nav_mood) {
                startActivity(new Intent(journalLists.this, moodList.class));
                return true;
            } else if (id == R.id.nav_lifestyle) {
                startActivity(new Intent(journalLists.this, lifeLatelyList.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(journalLists.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void addJournalEntry(String title, String feel, int mood) {
        float scale = getResources().getDisplayMetrics().density;
        int pixelValue = (int) (dpDesc * scale + 0.5f);
        int moodPixel = (int) (moodDP * scale + 0.5f);
        int margin15 = (int) (marginTB * scale + 0.5f);
        int margin30 = (int) (marginLR * scale + 0.5f);
        int number10 = (int) (numberM * scale + 0.5f);

        LinearLayout containerList = new LinearLayout(c);
        containerList.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        containerList.setOrientation(LinearLayout.HORIZONTAL);
        containerList.setPadding(75, 75, 75, 75);
        LinearLayout.LayoutParams parentMargin = (LinearLayout.LayoutParams) containerList.getLayoutParams();
        parentMargin.setMargins(margin30, margin15, margin30, margin15);
        containerList.setLayoutParams(parentMargin);
        containerList.setGravity(Gravity.CENTER_VERTICAL);
        containerList.setBackgroundResource(R.drawable.round);


        int entryId = View.generateViewId();
        containerList.setId(entryId);

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
        titleList.setText(title);
        titleList.setTextSize(20);
        titleList.setTextColor(Color.parseColor("#8D815C"));
        titleList.setTypeface(null, Typeface.BOLD);

        TextView feelList = new TextView(c);
        feelList.setText(feel);
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
        if (mood != 0) {
            moodList.setImageResource(mood);
        } else {
            moodList.setImageResource(R.drawable.dizzy);
        }


        containerList.addView(numberList);
        containerList.addView(leftLine);
        containerList.addView(journalDesc);
        containerList.addView(rightLine);
        containerList.addView(moodList);

        containerList.setOnClickListener(v -> {
            Intent viewJournal = new Intent(c, individualJournal.class);
            viewJournal.putExtra("entryId", entryId);
            viewJournal.putExtra("title", title);
            viewJournal.putExtra("feel", feel);
            viewJournal.putExtra("mood", mood);
            journalFormLauncher.launch(viewJournal);
        });


        journalEntries.addView(containerList);
        journalCounter++;
    }
}
