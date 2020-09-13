package com.schari.tutmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schari.tutmanager.R;
import com.schari.tutmanager.fragments.AddEnquiryFragment;
import com.schari.tutmanager.fragments.EnquiryListFragment;

public class EnquiryActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        Toolbar toolbar = findViewById(R.id.enquiry_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enquiry");
        }

        fab = findViewById(R.id.enquiry_fab);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.enquiry_fragment_container, new EnquiryListFragment()).commit();
        fab.animate().alpha(1).setDuration(1000);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.enquiry_fragment_container, new AddEnquiryFragment())
                        .addToBackStack("toAddEnquiry").commit();
                fab.animate().alpha(0).setDuration(100).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fab.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment f = fragmentManager.findFragmentById(R.id.enquiry_fragment_container);
        if (f instanceof EnquiryListFragment) {
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