package com.example.madproject2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class individualJournal extends AppCompatActivity {

    TextView titleView, feelView;
    ImageView back;
    Button editBtn, deleteBtn;
    int entryId, mood;
    String title, feel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_journal);

        titleView = findViewById(R.id.titleView);
        feelView = findViewById(R.id.feelView);
        back = findViewById(R.id.back);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();
        entryId = intent.getIntExtra("entryId", -1);
        title = intent.getStringExtra("title");
        feel = intent.getStringExtra("feel");
        mood = intent.getIntExtra("mood", R.drawable.dizzy);

        titleView.setText(title);
        feelView.setText(feel);

        back.setOnClickListener(v -> finish());

        editBtn.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("action", "edit");
            result.putExtra("entryId", getIntent().getIntExtra("entryId", -1));
            result.putExtra("title", title);
            result.putExtra("feel", feel);
            result.putExtra("mood", mood);
            setResult(Activity.RESULT_OK, result);
            finish();
        });


        deleteBtn.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("action", "delete");
            result.putExtra("entryId", entryId);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}
