package com.example.madproject2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class lifelatelyForm extends AppCompatActivity {

    Context c = this;
    ImageView backLife;

    CheckBox cbExercise, cbChill, cbShopping, cbLoving, cbPhonerot, cbBonding, cbSleeping, cbStudying;

    EditText noteLife;
    Button submitLifeStyle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifelately_form);
        addLife();
    }
    private void addLife() {
        backLife = findViewById(R.id.backLife);
        cbExercise = findViewById(R.id.cbExercise);
        cbChill = findViewById(R.id.cbChill);
        cbShopping = findViewById(R.id.cbShopping);
        cbLoving = findViewById(R.id.cbLoving);
        cbPhonerot = findViewById(R.id.cbPhonerot);
        cbBonding = findViewById(R.id.cbBonding);
        cbSleeping = findViewById(R.id.cbSleeping);
        cbStudying = findViewById(R.id.cbStudying);
        noteLife = findViewById(R.id.noteLife);
        submitLifeStyle = findViewById(R.id.submitLifeStyle);

        submitLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasChecked =
                        cbExercise.isChecked() || cbChill.isChecked() || cbShopping.isChecked() ||
                                cbLoving.isChecked() || cbPhonerot.isChecked() || cbBonding.isChecked() ||
                                cbSleeping.isChecked() || cbStudying.isChecked();

                String note = noteLife.getText().toString().trim();

                if (!hasChecked || note.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setTitle("Error");
                    builder.setMessage("Please select at least one activity and fill in the note.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Okay", null);

                    AlertDialog empty = builder.create();
                    empty.show();
                    return;
                }

                else {
                    ArrayList<Integer> selectedIcons = new ArrayList<>();
                    if (cbExercise.isChecked()) selectedIcons.add(R.drawable.exercisef);
                    if (cbChill.isChecked()) selectedIcons.add(R.drawable.chillf);
                    if (cbShopping.isChecked()) selectedIcons.add(R.drawable.shoppingf);
                    if (cbLoving.isChecked()) selectedIcons.add(R.drawable.lovingf);
                    if (cbPhonerot.isChecked()) selectedIcons.add(R.drawable.phonerotf);
                    if (cbBonding.isChecked()) selectedIcons.add(R.drawable.bondingf);
                    if (cbSleeping.isChecked()) selectedIcons.add(R.drawable.sleepingf);
                    if (cbStudying.isChecked()) selectedIcons.add(R.drawable.studyingf);

                    Intent sendData = new Intent();
                    sendData.putIntegerArrayListExtra("cbIcons", selectedIcons);
                    sendData.putExtra("lifeNote", note);
                    setResult(RESULT_OK, sendData);
                    finish();
                }
            }
        });
        backLife.setOnClickListener(v -> finish());
    }
}