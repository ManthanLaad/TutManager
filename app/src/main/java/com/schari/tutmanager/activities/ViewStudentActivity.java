
package com.schari.tutmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.schari.tutmanager.R;
import com.schari.tutmanager.objects.Student;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ViewStudentActivity extends AppCompatActivity {

    private ImageView profile;
    private TextView name;
    private TextView standard;

    private TextView school;
    private TextView previousScore;
    private TextView difficultSubs;
    private TextView fresherStatus;
    private TextView doj;
    private ImageView fresherStatusIcon;

    private TextView parentContact;
    private TextView personalContact;
    private TextView email;

    private TextView address;
    private TextView fatherName;
    private TextView motherName;
    private TextView dob;

    private CardView profileCard;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        supportPostponeEnterTransition();

        initializeViews();

        Intent intent = getIntent();

        student = (Student) intent.getSerializableExtra("student");
        String imageTransitionName = intent.getStringExtra("imageTransitionName");
        profileCard.setTransitionName(imageTransitionName);

        if (student.getProfileImage() != null && student.getProfileImage().length() > 0) {
            Picasso.get()
                    .load(student.getProfileImage())
                    .noFade()
                    .into(profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError(Exception e) {
                            supportStartPostponedEnterTransition();
                        }
                    });
        } else {
            supportStartPostponedEnterTransition();
        }

        setTextValues();

    }

    private void setTextValues() {
        name.setText(student.getName());
        standard.setText("Class " + student.getStandard());
        school.setText(student.getSchool());
        previousScore.setText(String.format("Previous Score: %s", student.getPreviousScore()));
        if (student.isFresher()) {
            fresherStatusIcon.setImageDrawable(getResources().getDrawable(R.mipmap.fresher_icon_round));
            fresherStatus.setText("Fresher");
        } else {
            fresherStatusIcon.setImageDrawable(getResources().getDrawable(R.mipmap.repeater_icon_round));
            fresherStatus.setText("Repeater");
        }
        doj.setText(String.format("Date of Joining: %s", student.getDateOfJoining()));
        parentContact.setText(String.format("Parent Contact: %s", student.getParentContact()));
        address.setText(String.format("Address: %s", student.getAddress()));
        dob.setText(String.format("Date of Birth: %s", student.getDateOfBirth()));

        if (student.getDifficultSubjects() != null && student.getDifficultSubjects().size() > 0) {
            ArrayList<String> ds = student.getDifficultSubjects();
            StringBuilder builder = new StringBuilder();
            builder.append("Difficult Subjects: ");
            for (String sub : ds) {
                builder.append(sub);
                if (ds.indexOf(sub) != (ds.size() - 1)) {
                    builder.append(", ");
                }
            }
            difficultSubs.setText(builder.toString());
        } else {
            difficultSubs.setVisibility(GONE);
        }

        if (student.getPersonalContact() != null && student.getPersonalContact().length() > 0) {
            personalContact.setText(String.format("Personal Contact: %s", student.getPersonalContact()));
        } else {
            personalContact.setVisibility(GONE);
        }

        if (student.getEmail() != null && student.getEmail().length() > 0) {
            email.setText(String.format("Email: %s", student.getEmail()));
        } else {
            email.setVisibility(GONE);
        }

        if (student.getFatherName() != null && student.getFatherName().length() > 0) {
            fatherName.setText(String.format("Father's Name: %s", student.getFatherName()));
        } else {
            fatherName.setVisibility(GONE);
        }
        if (student.getMotherName() != null && student.getMotherName().length() > 0) {
            motherName.setText(String.format("Mother's Name: %s", student.getMotherName()));
        } else {
            motherName.setVisibility(GONE);
        }
    }

    public void initializeViews() {
        profile = findViewById(R.id.view_student_profile);
        name = findViewById(R.id.view_student_name);
        standard = findViewById(R.id.view_student_class);
        profileCard = findViewById(R.id.view_student_profile_card);
        school = findViewById(R.id.view_student_school);
        previousScore = findViewById(R.id.view_student_previous_score);
        difficultSubs = findViewById(R.id.view_student_diff_subs);
        fresherStatus = findViewById(R.id.view_student_fresher_repeater);
        doj = findViewById(R.id.view_student_doj);
        fresherStatusIcon = findViewById(R.id.view_student_fresher_status);
        parentContact = findViewById(R.id.view_student_parent_contact);
        personalContact = findViewById(R.id.view_student_personal_contact);
        email = findViewById(R.id.view_student_email);
        address = findViewById(R.id.view_student_address);
        fatherName = findViewById(R.id.view_student_father_name);
        motherName = findViewById(R.id.view_student_mother_name);
        dob = findViewById(R.id.view_student_dob);
    }
}