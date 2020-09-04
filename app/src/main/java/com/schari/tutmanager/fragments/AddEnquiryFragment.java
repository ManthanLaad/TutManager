package com.schari.tutmanager.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schari.tutmanager.R;
import com.schari.tutmanager.activities.EnquiryActivity;
import com.schari.tutmanager.objects.Enquiry;

import java.util.ArrayList;
import java.util.Arrays;

public class AddEnquiryFragment extends Fragment {

    private EditText studentName;
    private Spinner schoolName;
    private EditText address;
    private Spinner standard;
    private EditText otherSchoolName;
    private Button addEnquiryButton;

    private DatabaseReference reference;

    private Bundle bundle;
    private String enquiryId;

    public AddEnquiryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("enquiry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_enquiry, container, false);

        bundle = getArguments();

        studentName = view.findViewById(R.id.student_name_enquiry);
        schoolName = view.findViewById(R.id.school_name_enquiry);
        address = view.findViewById(R.id.address_enquiry);
        standard = view.findViewById(R.id.class_enquiry);
        addEnquiryButton = view.findViewById(R.id.add_enquiry_button);
        otherSchoolName = view.findViewById(R.id.other_school_name_enquiry);

        if (bundle != null) {
            String nameStr = bundle.getString("name");
            enquiryId = bundle.getString("id");
            String schoolStr = bundle.getString("school");
            String addressStr = bundle.getString("address");
            String classStr = bundle.getString("standard");

            addEnquiryButton.setText("Update");
            studentName.setText(nameStr);
            address.setText(addressStr);
            String[] schools = getContext().getResources().getStringArray(R.array.schools);
            String[] classes = getContext().getResources().getStringArray(R.array.classes);

            int schoolPosition = getSchoolPosition(schools, schoolStr);
            int classPosition = getClassPosition(classes, classStr);

            schoolName.setSelection(schoolPosition);
            standard.setSelection(classPosition);
        }

        schoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 9) {
                    otherSchoolName.setVisibility(View.VISIBLE);
                    if (bundle != null) {
                        otherSchoolName.setText(bundle.getString("school"));
                    }
                } else {
                    otherSchoolName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addEnquiryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEnquiry();
            }
        });

        return view;
    }

    private void addEnquiry() {
        String nameString = studentName.getText().toString().trim();
        String schoolString = schoolName.getSelectedItem().toString().trim();
        String addressString = address.getText().toString().trim();
        String classString = standard.getSelectedItem().toString();

        if (otherSchoolName.getVisibility() == View.VISIBLE) {
            schoolString = otherSchoolName.getText().toString().trim();
        }

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(schoolString) ||
                TextUtils.isEmpty(addressString) || TextUtils.isEmpty(classString) ||
                schoolString.equals(getResources().getStringArray(R.array.schools)[0]) ||
                classString.equals(getResources().getStringArray(R.array.classes)[0])) {
            Snackbar.make(addEnquiryButton, "Please fill in all the details",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        Enquiry enquiry = new Enquiry(nameString, schoolString, addressString, classString);

        if (bundle != null) {
            reference.child(enquiryId).setValue(enquiry);
        } else {
            String key = reference.push().getKey();
            enquiry.setId(key);
            reference.child(key).setValue(enquiry);
            Snackbar.make(addEnquiryButton, "Enquiry Added", Snackbar.LENGTH_SHORT).show();
        }

        getActivity().getSupportFragmentManager().popBackStack();
        EnquiryActivity.fab.animate().alpha(1).setDuration(1000);
    }

    private int getSchoolPosition(String[] schools, String schoolStr) {
        int position = new ArrayList<>(Arrays.asList(schools)).indexOf(schoolStr);
        position = (position > -1) ? position : 9;
        return position;
    }

    private int getClassPosition(String[] classes, String classStr) {
        return new ArrayList<>(Arrays.asList(classes)).indexOf(classStr);
    }
}