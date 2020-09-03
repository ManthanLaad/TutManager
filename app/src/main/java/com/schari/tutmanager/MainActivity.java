package com.schari.tutmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

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
    }
}