package com.schari.tutmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schari.tutmanager.StudentItemClickListener;
import com.schari.tutmanager.fragments.AddStudentFragment;
import com.schari.tutmanager.R;
import com.schari.tutmanager.fragments.StudentListFragment;
import com.schari.tutmanager.objects.Student;

public class StudentActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public static FloatingActionButton fab;

    public static StudentItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Toolbar toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Students");
        }

        fab = findViewById(R.id.student_fab);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.student_fragment_container, new StudentListFragment()).commit();
        fab.animate().alpha(1).setDuration(1000);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.student_fragment_container, new AddStudentFragment())
                        .addToBackStack("toAddStudent").commit();
                fab.animate().alpha(0).setDuration(100)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        listener = new StudentItemClickListener() {
            @Override
            public void onStudentItemClick(int pos, Student student, CardView profileImageView) {
                Intent studentIntent = new Intent(StudentActivity.this,
                        ViewStudentActivity.class);

                studentIntent.putExtra("student", student);
                studentIntent.putExtra("imageTransitionName",
                        ViewCompat.getTransitionName(profileImageView));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        StudentActivity.this,
                        profileImageView,
                        ViewCompat.getTransitionName(profileImageView));

                startActivity(studentIntent, options.toBundle());
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment f = fragmentManager.findFragmentById(R.id.student_fragment_container);
        if (f instanceof StudentListFragment) {
            fab.animate().alpha(1).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    fab.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}