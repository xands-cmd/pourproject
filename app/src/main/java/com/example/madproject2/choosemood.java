package com.example.madproject2;

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

public class choosemood extends AppCompatActivity {

    Context c = this;
    TextView moodText;
    ImageView mainMood, happy, sad, dizzy, drool, angry, scared, kiss;
    ImageView returnForm;
    Button submitMood;

    int imageMood;
    String currentTit = "";
    String currentFel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choosemood);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.moodChoose), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        moodChoose();
    }

    private void moodChoose() {
        returnForm = findViewById(R.id.returnForm);
        moodText = findViewById(R.id.moodText);
        mainMood = findViewById(R.id.mainMood);
        happy = findViewById(R.id.happy);
        sad = findViewById(R.id.sad);
        dizzy = findViewById(R.id.dizzy);
        drool = findViewById(R.id.drool);
        angry = findViewById(R.id.angry);
        scared = findViewById(R.id.scared);
        kiss = findViewById(R.id.kiss);
        submitMood = findViewById(R.id.submitMood);

        imageMood = R.drawable.scared;

        Intent getCurrent = getIntent();
        if (getCurrent.hasExtra("currentTitle")) currentTit = getCurrent.getStringExtra("currentTitle");
        if (getCurrent.hasExtra("currentFeel")) currentFel = getCurrent.getStringExtra("currentFeel");

        submitMood.setOnClickListener(v -> {
            Intent passMood = new Intent();
            passMood.putExtra("moodChange", imageMood);
            passMood.putExtra("current1", currentTit);
            passMood.putExtra("current2", currentFel);
            setResult(RESULT_OK, passMood);
            finish();
        });

        returnForm.setOnClickListener(v -> {
            Intent passMood = new Intent();
            passMood.putExtra("moodChange", imageMood);
            passMood.putExtra("current1", currentTit);
            passMood.putExtra("current2", currentFel);
            setResult(RESULT_OK, passMood);
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

    //function para sa listener nung bawat emoji
    private void setMood(int drawable, String text, int colorRes) {
        mainMood.setBackground(getResources().getDrawable(drawable));
        moodText.setText(text);
        moodText.setTextColor(getResources().getColor(colorRes));
        imageMood = drawable;
    }
}
