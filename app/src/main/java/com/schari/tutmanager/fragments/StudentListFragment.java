package com.schari.tutmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schari.tutmanager.R;
import com.schari.tutmanager.activities.StudentActivity;
import com.schari.tutmanager.adapters.EnquiryAdapter;
import com.schari.tutmanager.adapters.StudentAdapter;
import com.schari.tutmanager.objects.Enquiry;
import com.schari.tutmanager.objects.Student;

import java.util.ArrayList;

public class StudentListFragment extends Fragment {

    private RecyclerView studentRecycler;
    private StudentAdapter adapter;
    private ArrayList<Student> students;

    private DatabaseReference reference;

    private TextView noStudent;

    public StudentListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("student");
        reference.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        noStudent = view.findViewById(R.id.no_student_textview);
        studentRecycler = view.findViewById(R.id.student_recycler);

        students = new ArrayList<>();
        adapter = new StudentAdapter(getContext(), students, StudentActivity.listener);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        studentRecycler.setAdapter(adapter);
        studentRecycler.setLayoutManager(manager);

        fetchStudents();

        return view;
    }

    public void fetchStudents() {
        reference.orderByChild("standard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Student student = dataSnapshot.getValue(Student.class);
                        student.setId(dataSnapshot.getKey());
                        students.add(student);
                    }
                    adapter.showShimmer = false;
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.showShimmer = false;
                    studentRecycler.setVisibility(View.INVISIBLE);
                    noStudent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}