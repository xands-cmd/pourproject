package com.example.madproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class journalform extends AppCompatActivity {

    Context c = this;
    ImageView moodPicker;
    ImageView returnJournalEntry;
    Button submitForm;
    EditText feelJournal, titleJournal;

    int getMood;

    ActivityResultLauncher<Intent> chooseMoodLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journalform);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.journalCRUD), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chooseMoodLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            getMood = data.getIntExtra("moodChange", R.drawable.dizzy);
                            moodPicker.setImageResource(getMood);

                            // update title and feel in case they were changed
                            if (data.hasExtra("current1")) {
                                titleJournal.setText(data.getStringExtra("current1"));
                            }
                            if (data.hasExtra("current2")) {
                                feelJournal.setText(data.getStringExtra("current2"));
                            }
                        }
                    }
                }
        );

        journalCRUD();
    }

    private void journalCRUD() {
        moodPicker = findViewById(R.id.moodPicker);
        returnJournalEntry = findViewById(R.id.returnJournalEntry);
        submitForm = findViewById(R.id.submitForm);
        titleJournal = findViewById(R.id.titleJournal);
        feelJournal = findViewById(R.id.feelJournal);

        Intent getMoodIcon = getIntent();

        if(getMoodIcon.hasExtra("current1")) {
            titleJournal.setText(getMoodIcon.getStringExtra("current1"));
        }
        if(getMoodIcon.hasExtra("current2")) {
            feelJournal.setText(getMoodIcon.getStringExtra("current2"));
        }
        if(getMoodIcon.hasExtra("moodChange")) {
            getMood = getMoodIcon.getIntExtra("moodChange", R.drawable.dizzy);
            moodPicker.setImageResource(getMood);
        } else {
            getMood = getMoodIcon.getIntExtra("moodChange", R.drawable.dizzy);
            moodPicker.setImageResource(R.drawable.dizzy);
        }


        moodPicker.setOnClickListener(v -> {
            Intent moods = new Intent(c, choosemood.class);
            moods.putExtra("currentTitle", titleJournal.getText().toString());
            moods.putExtra("currentFeel", feelJournal.getText().toString());
            chooseMoodLauncher.launch(moods);
        });


        submitForm.setOnClickListener(v -> {
            String title = titleJournal.getText().toString();
            String feel = feelJournal.getText().toString();

            Intent passJournal = new Intent();
            passJournal.putExtra("journalTitle", title);
            passJournal.putExtra("journalFeel", feel);
            passJournal.putExtra("journalMood", getMood);


            int entryId = getIntent().getIntExtra("entryId", -1);
            passJournal.putExtra("entryId", entryId);

            setResult(Activity.RESULT_OK, passJournal);
            finish();
        });

        returnJournalEntry.setOnClickListener(v -> finish());
    }
}
