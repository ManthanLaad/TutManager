package com.schari.tutmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView attendanceCard;
    private CardView feesCard;
    private CardView studentsCard;
    private CardView enquiryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TutManager");
        }

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
    }
}