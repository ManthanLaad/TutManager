package com.schari.tutmanager.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schari.tutmanager.R;
import com.schari.tutmanager.objects.Enquiry;

import java.util.ArrayList;

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.EnquiryViewHolder> {

    private ArrayList<Enquiry> enquiries;
    private Context context;

    public boolean showShimmer;
    public int shimmerItems;

    private DatabaseReference reference;

    public EnquiryAdapter(Context context, ArrayList<Enquiry> enquiries) {
        this.enquiries = enquiries;
        this.context = context;
        showShimmer = true;
        shimmerItems = 6;

        reference = FirebaseDatabase.getInstance().getReference().child("enquiry");
    }

    @NonNull
    @Override
    public EnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enquiry_item, parent, false);
        return new EnquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryViewHolder holder, final int position) {
        if (showShimmer) {
            holder.enquiryShimmer.startShimmer();
        } else {
            holder.enquiryShimmer.stopShimmer();
            holder.enquiryShimmer.setShimmer(null);

            final Enquiry enquiry = enquiries.get(position);

            holder.name.setBackground(null);
            holder.name.setText(enquiry.getName());

            holder.school.setBackground(null);
            holder.school.setText(enquiry.getSchool());

            holder.address.setBackground(null);
            holder.address.setText(enquiry.getAddress());

            holder.standard.setBackground(null);
            holder.standard.setText(enquiry.getStandard());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = enquiry.getId();
                    reference.child(key).removeValue();
                    enquiries.remove(position);
                    notifyDataSetChanged();
                }
            });

            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // To be done later
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ((showShimmer) ? shimmerItems : enquiries.size());
    }

    public static class EnquiryViewHolder extends RecyclerView.ViewHolder {

        private ShimmerFrameLayout enquiryShimmer;
        private TextView name;
        private TextView school;
        private TextView address;
        private TextView standard;

        private ImageView confirm;
        private ImageView edit;
        private ImageView delete;

        public EnquiryViewHolder(@NonNull View itemView) {
            super(itemView);

            enquiryShimmer = itemView.findViewById(R.id.enquiry_shimmer_layout);
            name = itemView.findViewById(R.id.eq_item_name);
            school = itemView.findViewById(R.id.eq_item_school);
            address = itemView.findViewById(R.id.eq_item_address);
            standard = itemView.findViewById(R.id.eq_item_class);

            confirm = itemView.findViewById(R.id.eq_item_confirm);
            edit = itemView.findViewById(R.id.eq_item_edit);
            delete = itemView.findViewById(R.id.eq_item_delete);
        }
    }
}
