package com.example.madproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class moodForm extends AppCompatActivity {

    ImageView mainMood, happy, sad, dizzy, drool, angry, scared, kiss;
    ImageView returnForm;
    TextView moodText;
    Button submitMood;

    Context c = this;

    int imageMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        moodCRUD();
    }

    private void moodCRUD() {
        returnForm = findViewById(R.id.returnForm);
        mainMood = findViewById(R.id.mainMood);
        happy = findViewById(R.id.happy);
        sad = findViewById(R.id.sad);
        dizzy = findViewById(R.id.dizzy);
        drool = findViewById(R.id.drool);
        angry = findViewById(R.id.angry);
        scared = findViewById(R.id.scared);
        kiss = findViewById(R.id.kiss);
        submitMood = findViewById(R.id.submitMood);
        moodText = findViewById(R.id.moodText);
        imageMood = R.drawable.scared;

        submitMood.setOnClickListener(v -> {
            Intent passMood = new Intent();
            passMood.putExtra("moodCurrent", imageMood);
            setResult(Activity.RESULT_OK, passMood);
            finish();

        });


        happy.setOnClickListener(v -> setMood(R.drawable.happy, "Happy, happy, and happy!", R.color.yellow));
        sad.setOnClickListener(v -> setMood(R.drawable.sad, "Not your day, huh?!", R.color.blue));
        dizzy.setOnClickListener(v -> setMood(R.drawable.dizzy, "I need some milk...", R.color.orange));
        drool.setOnClickListener(v -> setMood(R.drawable.drool, "Tired from life?", R.color.green));
        angry.setOnClickListener(v -> setMood(R.drawable.angry, "You probably need a rage room, not a journal", R.color.red));
        scared.setOnClickListener(v -> setMood(R.drawable.scared, "I'm here, don't be scared", R.color.purple));
        kiss.setOnClickListener(v -> setMood(R.drawable.kiss, "Getting all smoochy, I see", R.color.pink));
    }

    private void setMood(int drawable, String text, int colorRes) {
        mainMood.setImageResource(drawable);
        moodText.setText(text);
        moodText.setTextColor(getResources().getColor(colorRes));
        imageMood = drawable;
    }
}