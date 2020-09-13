package com.schari.tutmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.schari.tutmanager.activities.AttendanceActivity;
import com.schari.tutmanager.activities.EnquiryActivity;
import com.schari.tutmanager.activities.FeeActivity;
import com.schari.tutmanager.activities.StudentActivity;

public class MainActivity extends AppCompatActivity {

    private CardView attendanceCard;
    private CardView feesCard;
    private CardView studentsCard;
    private CardView enquiryCard;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TutManager");
        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        attendanceCard = findViewById(R.id.attendance_card);
        feesCard = findViewById(R.id.fees_card);
        studentsCard = findViewById(R.id.students_card);
        enquiryCard = findViewById(R.id.enquiries_card);

        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
            }
        });

        feesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeeActivity.class));
            }
        });

        enquiryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EnquiryActivity.class));
            }
        });

        studentsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentActivity.class));
            }
        });

        signIn();
    }

    private void signIn() {
        String email = "schari2509@gmail.com";
        String password = "soham2509";
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("SignIn", "Sign In Success");
                    }
                });
    }
}