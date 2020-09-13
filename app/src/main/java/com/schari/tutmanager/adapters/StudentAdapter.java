package com.schari.tutmanager.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.schari.tutmanager.R;
import com.schari.tutmanager.StudentItemClickListener;
import com.schari.tutmanager.activities.StudentActivity;
import com.schari.tutmanager.fragments.AddStudentFragment;
import com.schari.tutmanager.objects.Student;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ArrayList<Student> students;
    private Context context;

    public boolean showShimmer;
    public int shimmerItems;

    private DatabaseReference reference;
    private StorageReference storageReference;

    private final StudentItemClickListener listener;

    public StudentAdapter(Context context, ArrayList<Student> students,
                          StudentItemClickListener listener) {
        this.students = students;
        this.context = context;
        showShimmer = true;
        shimmerItems = 6;
        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("student");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position) {
        if (showShimmer) {
            holder.studentShimmer.startShimmer();
        } else {
            holder.studentShimmer.stopShimmer();
            holder.studentShimmer.setShimmer(null);

            final Student student = students.get(position);

            if (student.getProfileImage() != null) {
//                String imageRef = "profile_pictures/" + student.getId() + ".jpg";
//                StorageReference ref = storageReference.child(imageRef);
//
//                final long TWO_MEGABYTE = 2 * 1024 * 1024;
//                ref.getBytes(TWO_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        holder.profile.setImageBitmap(bitmap);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("Profile Error", exception.getMessage());
//                    }
//                });

                Picasso.get()
                        .load(student.getProfileImage())
                        .placeholder(context.getResources().getDrawable(R.drawable.avatar))
                        .noFade()
                        .into(holder.profile);
            }

            holder.name.setBackground(null);
            holder.name.setText(student.getName());

            holder.standard.setBackground(null);
            holder.standard.setText("Class " + student.getStandard());

            ViewCompat.setTransitionName(holder.profileCard, student.getId() + "image");

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", student.getId());
                    Fragment f = new AddStudentFragment();
                    f.setArguments(bundle);

                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction().addToBackStack("editToAddStudent")
                            .replace(R.id.student_fragment_container, f).commit();
                    StudentActivity.fab.animate().alpha(0).setDuration(1000);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = student.getId();
                    reference.child(key).removeValue();
                    students.remove(position);
                    notifyDataSetChanged();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStudentItemClick(holder.getAdapterPosition(),
                            student,
                            holder.profileCard);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return ((showShimmer) ? shimmerItems : students.size());
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        private ShimmerFrameLayout studentShimmer;
        private TextView name;
        private TextView standard;
        private ImageView profile;

        private ImageView edit;
        private ImageView delete;

        private CardView profileCard;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentShimmer = itemView.findViewById(R.id.student_shimmer_layout);
            name = itemView.findViewById(R.id.st_item_name);
            standard = itemView.findViewById(R.id.st_item_class);
            profile = itemView.findViewById(R.id.st_item_profile);
            edit = itemView.findViewById(R.id.st_item_edit);
            delete = itemView.findViewById(R.id.st_item_delete);
            profileCard = itemView.findViewById(R.id.student_profile_card);
        }
    }
}
