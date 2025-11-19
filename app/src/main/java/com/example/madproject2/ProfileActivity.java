package com.example.madproject2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.HashMap;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {

    private Context c;
    private EditText fNameET, lNameET, bioET;
    private TextView usernameTV, pickDateButton;
    private Spinner genderSpinner;
    private int year, month, day;
    private String selectedDOB = "";
    private Button saveBtn, registerBtn, redirectBtn;
    private Button testerBtn1, testerBtn2;
    private TextView changePicBtn;
    //private ImageView profileImg;
    ShapeableImageView profileImage;
    Bitmap tempProfileImage = null;
    final int REQUEST_CODE  = 1;
    final int REQUEST_CODE_CAM  = 2;
    HashMap<String, Object> userData;
    String username, fName, lName, gender, dob, bio, email, password;
    EditText emailET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        c = this;
        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }

    public void initialize() {
        usernameTV = findViewById(R.id.username);
        fNameET = findViewById(R.id.fName);
        lNameET = findViewById(R.id.lName);
        pickDateButton = findViewById(R.id.pickDateButton);
        genderSpinner = findViewById(R.id.gender_spinner);
        bioET = findViewById(R.id.bio);
        registerBtn = findViewById(R.id.registerBtn);
        saveBtn = findViewById(R.id.saveBtn);
        //profileImg = findViewById(R.id.profileImg);
        profileImage = findViewById(R.id.profileImage);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);


        //tester buttons
        testerBtn1 = findViewById(R.id.testerBtn1);
        testerBtn2 = findViewById(R.id.testerBtn2);
        changePicBtn = findViewById(R.id.changePicBtn);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);

        lName = (String) userData.get("username");
        fName = (String) userData.get("firstName");
        lName = (String) userData.get("lastName");
        gender = (String) userData.get("gender");
        dob = (String) userData.get("dob");
        bio = (String) userData.get("bio");
        email = (String) userData.get("email");
        password = (String) userData.get("password");

        Bitmap savedImage = (Bitmap) userData.get("profileImage");
        if (savedImage != null) {
            profileImage.setImageBitmap(savedImage);
        }

        //Toast.makeText(c, "Hello, " + fName + "!", Toast.LENGTH_SHORT).show();

        if (username != null) usernameTV.setText(username);
        if (fName != null) fNameET.setText(fName);
        if (lName != null) lNameET.setText(lName);
        if (dob != null) pickDateButton.setText(dob);
        if (bio != null) bioET.setText(bio);
        if (email != null) emailET.setText(email);
        if (password != null) passwordET.setText(password);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        if (gender != null) {
            int spinnerPosition = adapter.getPosition(gender);
            if (spinnerPosition >= 0) {
                genderSpinner.setSelection(spinnerPosition);
            }
        }

        pickDateButton.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        saveBtn.setOnClickListener(v -> updateProfile());

        Button logOutBtn = findViewById(R.id.logOut);

        logOutBtn.setOnClickListener(v -> {

            Intent loggingOut = new Intent(ProfileActivity.this, LoginActivity.class);
            loggingOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loggingOut);

            finish();
        });


        //Kylee
        testerBtn1.setOnClickListener(v -> {
            Intent intent1 = new Intent(c, HomeActivity.class);
            //intent1.putExtra("username", username);
            startActivity(intent1);
        });

        //Ian
        testerBtn2.setOnClickListener(v -> {
            Intent intent1 = new Intent(c, journalLists.class);
            //intent1.putExtra("username", username);
            startActivity(intent1);
        });

        //profile pic
        changePicBtn.setOnClickListener(v -> {
            Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cam, REQUEST_CODE_CAM);

            //intent1.putExtra("username", username);
            //startActivity(intent1);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                //startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra("username", username);
                startActivity(homeIntent);
                return true;
            } else if (id == R.id.nav_journal) {
                startActivity(new Intent(ProfileActivity.this, journalLists.class));
                Intent journalIntent = new Intent(ProfileActivity.this, journalLists.class);
                journalIntent.putExtra("username", username);
                startActivity(journalIntent);
                return true;
            } else if (id == R.id.nav_mood) {
                startActivity(new Intent(ProfileActivity.this, moodList.class));
                Intent moodIntent = new Intent(ProfileActivity.this, moodList.class);
                moodIntent.putExtra("username", username);
                startActivity(moodIntent);
                return true;
            } else if (id == R.id.nav_lifestyle) {
                //startActivity(new Intent(ProfileActivity.this, lifeLatelyList.class));
                Intent lifestyleIntent = new Intent(ProfileActivity.this, lifeLatelyList.class);
                lifestyleIntent.putExtra("username", username);
                startActivity(lifestyleIntent);
                return true;
            } else if (id == R.id.nav_profile) {
                //Intent profileIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                //startActivity(profileIntent);
                //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth, 0, 0, 0);

                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);

                    if (selectedDate.after(today)) {
                        Toast.makeText(this, "Future dates are not allowed.", Toast.LENGTH_SHORT).show();
                        selectedDOB = "";
                        pickDateButton.setText("Select Date");
                    } else {
                        selectedDOB = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        pickDateButton.setText(selectedDOB);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void updateProfile() {
        String fName = fNameET.getText().toString().trim();
        String lName = lNameET.getText().toString().trim();
        String dob = pickDateButton.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String bio = bioET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        // Validation
        if (fName.isEmpty()) {
            Toast.makeText(c, "First name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lName.isEmpty()) {
            Toast.makeText(c, "Last name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dob.isEmpty()) {
            Toast.makeText(c, "Date of birth is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fName.length() < 2 || !fName.matches("[a-zA-Z ]+")) {
            Toast.makeText(c, "Invalid first name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lName.length() < 2 || !lName.matches("[a-zA-Z ]+")) {
            Toast.makeText(c, "Invalid last name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDOB(dob)) {
            Toast.makeText(c, "Please select a valid date of birth.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(c, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(c, "Password is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(c, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.matches(".*[A-Za-z].*")) {
            Toast.makeText(c, "Password must contain at least one letter.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.matches(".*[0-9].*")) {
            Toast.makeText(c, "Password must contain at least one number.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> userData = UserDatabase.getUser(username);
        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("dob", dob);
        userData.put("gender", gender);
        userData.put("bio", bio);

        userData.put("email", email);
        userData.put("password", password);

        if (tempProfileImage != null) {
            userData.put("profileImage", tempProfileImage);
        }

        UserDatabase.updateUser(username, userData);

        Toast.makeText(c, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }


    private boolean isValidDOB(String dob) {
        try {
            String[] parts = dob.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);

            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day, 0, 0, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return !selected.after(today);
        } catch (Exception e) {
            return false;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAM && resultCode==RESULT_OK ){
            Bundle extras = data.getExtras();
            Bitmap imgBit = (Bitmap) extras.get("data");
            //imgBit.setImageBitmap(imgBit);
            //profileImg.setImageBitmap(imgBit);
            profileImage.setImageBitmap(imgBit);
            tempProfileImage = imgBit;

            //HashMap<String, Object> userData = UserDatabase.getUser(username);
            //userData.put("profileImage", imgBit);
            //Toast.makeText(this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
        }

    }

}//class
