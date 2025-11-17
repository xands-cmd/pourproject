package com.example.madproject2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.example.moodpicker.R;

public class MoodPickerActivity extends AppCompatActivity {
    ImageView ivSelectedEmoji, yellow, blue, orange, green, red, purple, pink;
    TextView tvSelectedMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_picker);

        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mood_picker), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initialize() {
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
    }

    View.OnClickListener clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.yellow){
                ivSelectedEmoji.setImageResource(R.drawable.yellow);
                tvSelectedMood.setText("I'm feeling Happy!");
                tvSelectedMood.setTextColor(getColor(R.color.yellow));
            }
            else if (v.getId()==R.id.blue) {
                ivSelectedEmoji.setImageResource(R.drawable.blue);
                tvSelectedMood.setText("I'm feeling Sad!");
                tvSelectedMood.setTextColor(getColor(R.color.blue));
            }
            else if (v.getId()==R.id.orange) {
                ivSelectedEmoji.setImageResource(R.drawable.orange);
                tvSelectedMood.setText("I'm feeling Anxious!");
                tvSelectedMood.setTextColor(getColor(R.color.orange));
            }
            else if (v.getId()==R.id.green) {
                ivSelectedEmoji.setImageResource(R.drawable.green);
                tvSelectedMood.setText("I'm feeling Sick!");
                tvSelectedMood.setTextColor(getColor(R.color.green));
            }
            else if (v.getId()==R.id.red) {
                ivSelectedEmoji.setImageResource(R.drawable.red);
                tvSelectedMood.setText("I'm feeling Angry!");
                tvSelectedMood.setTextColor(getColor(R.color.red));
            }
            else if (v.getId()==R.id.purple) {
                ivSelectedEmoji.setImageResource(R.drawable.purple);
                tvSelectedMood.setText("I'm feeling Scared!");
                tvSelectedMood.setTextColor(getColor(R.color.purple));
            }
            else if (v.getId()==R.id.pink) {
                ivSelectedEmoji.setImageResource(R.drawable.pink);
                tvSelectedMood.setText("I'm feeling Loved!");
                tvSelectedMood.setTextColor(getColor(R.color.pink));
            }
        }
    };
}