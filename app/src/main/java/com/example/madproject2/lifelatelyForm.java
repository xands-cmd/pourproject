package com.example.madproject2;

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

                ArrayList<Integer> selectedIcons = new ArrayList<>();
                if(cbExercise.isChecked())selectedIcons.add(R.drawable.exercisef);
                if(cbChill.isChecked())selectedIcons.add(R.drawable.chillf);
                if(cbShopping.isChecked())selectedIcons.add(R.drawable.shoppingf);
                if(cbLoving.isChecked())selectedIcons.add(R.drawable.lovingf);
                if(cbPhonerot.isChecked())selectedIcons.add(R.drawable.phonerotf);
                if(cbBonding.isChecked())selectedIcons.add(R.drawable.bondingf);
                if(cbSleeping.isChecked())selectedIcons.add(R.drawable.sleepingf);
                if(cbStudying.isChecked())selectedIcons.add(R.drawable.studyingf);

                Intent sendData = new Intent();
                sendData.putIntegerArrayListExtra("cbIcons", selectedIcons);
                sendData.putExtra("lifeNote", noteLife.getText().toString());
                setResult(RESULT_OK, sendData);
                finish();

            }
        });


    }
}