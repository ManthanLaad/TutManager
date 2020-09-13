package com.schari.tutmanager;

import androidx.cardview.widget.CardView;

import com.schari.tutmanager.objects.Student;

public interface StudentItemClickListener {

    void onStudentItemClick(int pos,
                            Student student, CardView profileImageView);
}
