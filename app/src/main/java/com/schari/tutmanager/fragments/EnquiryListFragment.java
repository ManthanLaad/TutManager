package com.schari.tutmanager.fragments;

import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schari.tutmanager.R;
import com.schari.tutmanager.adapters.EnquiryAdapter;
import com.schari.tutmanager.objects.Enquiry;

import java.util.ArrayList;

public class EnquiryListFragment extends Fragment {

    private RecyclerView enquiryRecycler;
    private EnquiryAdapter adapter;
    private ArrayList<Enquiry> enquiries;

    private DatabaseReference reference;

    private TextView noEnquiry;

    public EnquiryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("enquiry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enquiry_list, container, false);

        noEnquiry = view.findViewById(R.id.no_enquiry_textview);
        enquiryRecycler = view.findViewById(R.id.enquiry_recycler);

        enquiries = new ArrayList<>();
        adapter = new EnquiryAdapter(getContext(), enquiries);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        enquiryRecycler.setAdapter(adapter);
        enquiryRecycler.setLayoutManager(manager);

        fetchEnquiries();

        return view;
    }

    public void fetchEnquiries() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("Children", dataSnapshot.getValue().toString());
                        Enquiry enquiry = dataSnapshot.getValue(Enquiry.class);
                        enquiry.setId(dataSnapshot.getKey());
                        enquiries.add(enquiry);
                    }
                    adapter.showShimmer = false;
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.showShimmer = false;
                    enquiryRecycler.setVisibility(View.INVISIBLE);
                    noEnquiry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}