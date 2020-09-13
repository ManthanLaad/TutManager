package com.schari.tutmanager.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.schari.tutmanager.R;
import com.schari.tutmanager.activities.StudentActivity;
import com.schari.tutmanager.objects.Student;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class AddStudentFragment extends Fragment {

    private EditText studentName;
    private Spinner schoolName;
    private EditText otherSchoolName;
    private Spinner standard;
    private EditText address;
    private EditText fatherName;
    private EditText motherName;
    private EditText dateOfBirth;
    private EditText dateOfJoining;
    private EditText personalContact;
    private EditText parentContact;
    private EditText email;
    private EditText previousScore;
    private Spinner fresherRepeater;

    private CardView otherSchoolCard;

    private Button engButton;
    private Button hinButton;
    private Button tlButton;
    private Button matButton;
    private Button sciButton;
    private Button hisButton;
    private Button geoButton;

    private Button uploadImageButton;
    private ImageView profileImage;
    private Button addStudentButton;

    private DatabaseReference reference;
    private StorageReference storageReference;

    private Bundle bundle;
    private String studentId;

    private int DATE_FIELD = 1;

    private Bitmap profileBitmap = null;

    private static boolean IS_ENG_SELECTED = false;
    private static boolean IS_HIN_SELECTED = false;
    private static boolean IS_TL_SELECTED = false;
    private static boolean IS_MAT_SELECTED = false;
    private static boolean IS_SCI_SELECTED = false;
    private static boolean IS_HIS_SELECTED = false;
    private static boolean IS_GEO_SELECTED = false;

    public static boolean IS_IMAGE_SELECTED = false;

    private InputStream stream;

    private Calendar calendar;

    Uri chosenImageUri;

    private ArrayList<String> difficultSubs;

    public AddStudentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("student");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);

        initializeViews(view);

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                updateLabel(DATE_FIELD);
            }
        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_FIELD = 1;
                DatePickerDialog dialog = new DatePickerDialog(getContext(), listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.YEAR));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        dateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_FIELD = 2;
                DatePickerDialog dialog = new DatePickerDialog(getContext(), listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.YEAR));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        schoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 9) {
                    otherSchoolCard.setVisibility(View.VISIBLE);
                    if (bundle != null) {
                        otherSchoolName.setText(bundle.getString("school"));
                    }
                } else {
                    otherSchoolCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSubjectButtons();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    addStudent();
                } else {
                    Snackbar.make(v, "No Internet Connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void updateLabel(int field) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (field == 1) {
            dateOfBirth.setText(sdf.format(calendar.getTime()));
        } else {
            dateOfJoining.setText(sdf.format(calendar.getTime()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            chosenImageUri = data.getData();

            try {

                stream = getContext().getContentResolver()
                        .openInputStream(chosenImageUri);

                BitmapFactory.Options bOptions = new BitmapFactory.Options();
                bOptions.inSampleSize = 4;
                profileBitmap = BitmapFactory.decodeStream(stream, null, bOptions);


                ExifInterface exifInterface = new ExifInterface(getContext().getContentResolver()
                        .openInputStream(chosenImageUri));
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    profileBitmap = Bitmap.createBitmap(profileBitmap, 0, 0,
                            profileBitmap.getWidth(), profileBitmap.getHeight(), matrix, true);
                }

                uploadImageButton.setText("Choose Different Image");
                profileImage.setVisibility(View.VISIBLE);
                profileImage.setImageBitmap(profileBitmap);
                IS_IMAGE_SELECTED = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addStudent() {

        String nameString = studentName.getText().toString().trim();
        String schoolString = schoolName.getSelectedItem().toString();
        String addressString = address.getText().toString().trim();
        String classString = standard.getSelectedItem().toString();
        String fatherNameString = fatherName.getText().toString().trim();
        String motherNameString = motherName.getText().toString().trim();
        String dateOfBirthString = dateOfBirth.getText().toString().trim();
        String dateOfJoiningString = dateOfJoining.getText().toString().trim();
        String personalContactString = personalContact.getText().toString().trim();
        String parentContactString = parentContact.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String previousScoreString = previousScore.getText().toString().trim();
        String fresherRepeaterString = fresherRepeater.getSelectedItem().toString();

        if (otherSchoolCard.getVisibility() == View.VISIBLE) {
            schoolString = otherSchoolName.getText().toString().trim();
        }

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(schoolString) ||
                TextUtils.isEmpty(addressString) || TextUtils.isEmpty(classString) ||
                schoolString.equals(getResources().getStringArray(R.array.schools)[0]) ||
                classString.equals(getResources().getStringArray(R.array.classes)[0]) ||
                TextUtils.isEmpty(dateOfBirthString) || TextUtils.isEmpty(parentContactString) ||
                TextUtils.isEmpty(fresherRepeaterString)) {
            Snackbar.make(addStudentButton, "Please fill in all the details",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        boolean isFresher = fresherRepeaterString.equals(getContext().getResources().
                getStringArray(R.array.admission_type)[0]);

        int standard = Arrays.asList(getContext().getResources().getStringArray(R.array.classes))
                .lastIndexOf(classString) + 7;

        setDifficultSubs();
        Student student = new Student(nameString, schoolString, standard, addressString,
                fatherNameString, motherNameString, dateOfBirthString, dateOfJoiningString,
                personalContactString, parentContactString, emailString, previousScoreString,
                isFresher, difficultSubs);

        addStudentToDB(student);

    }

    private void addStudentToDB(final Student student) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Adding Student...Please wait");
        dialog.setCancelable(false);
        dialog.show();
        final String key = reference.push().getKey();

        if (profileBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            profileBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] data = baos.toByteArray();

            final StorageReference profileRef = storageReference
                    .child("profile_pictures/" + key + ".jpg");

            UploadTask uploadTask = profileRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Snackbar.make(addStudentButton, "Profile Image could not be uploaded",
                            Snackbar.LENGTH_SHORT).show();
                    Log.d("Image Upload Error", Objects.requireNonNull(exception.getMessage()));
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return profileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.d("Image DURI Latest", downloadUri.toString());
                        student.setProfileImage(downloadUri.toString());
                    }
                    student.setId(key);
                    reference.child(key).setValue(student);
                    dialog.dismiss();
                    Snackbar.make(addStudentButton, "Student added!", Snackbar.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    StudentActivity.fab.animate().alpha(1).setDuration(1000).setListener(
                            new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    StudentActivity.fab.setVisibility(View.VISIBLE);
                                }
                            }
                    );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            student.setId(key);
            reference.child(key).setValue(student);
            dialog.dismiss();
            Snackbar.make(addStudentButton, "Student added!", Snackbar.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            StudentActivity.fab.animate().alpha(1).setDuration(1000).setListener(
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            StudentActivity.fab.setVisibility(View.VISIBLE);
                        }
                    }
            );
        }

    }

    private void setDifficultSubs() {
        difficultSubs = new ArrayList<>();
        if (IS_ENG_SELECTED) difficultSubs.add("English");
        if (IS_HIN_SELECTED) difficultSubs.add("Hindi");
        if (IS_TL_SELECTED) difficultSubs.add("Third Language");
        if (IS_MAT_SELECTED) difficultSubs.add("Maths");
        if (IS_SCI_SELECTED) difficultSubs.add("Science");
        if (IS_HIS_SELECTED) difficultSubs.add("History");
        if (IS_GEO_SELECTED) difficultSubs.add("Geography");
    }

    private void initializeViews(View view) {
        studentName = view.findViewById(R.id.student_student_name);
        schoolName = view.findViewById(R.id.student_school_name);
        otherSchoolName = view.findViewById(R.id.student_other_school_name);
        standard = view.findViewById(R.id.student_class);
        address = view.findViewById(R.id.student_address);
        fatherName = view.findViewById(R.id.student_father_name);
        motherName = view.findViewById(R.id.student_mother_name);
        dateOfBirth = view.findViewById(R.id.student_dob);
        dateOfJoining = view.findViewById(R.id.student_doj);
        personalContact = view.findViewById(R.id.student_personal_contact);
        parentContact = view.findViewById(R.id.student_parent_contact);
        email = view.findViewById(R.id.student_email);
        previousScore = view.findViewById(R.id.student_previous_score);
        fresherRepeater = view.findViewById(R.id.student_fresher_status);
        otherSchoolCard = view.findViewById(R.id.other_school_card);
        engButton = view.findViewById(R.id.student_diff_eng);
        hinButton = view.findViewById(R.id.student_difficult_hin);
        tlButton = view.findViewById(R.id.student_difficult_tl);
        matButton = view.findViewById(R.id.student_difficult_mat);
        sciButton = view.findViewById(R.id.student_difficult_sci);
        hisButton = view.findViewById(R.id.student_difficult_his);
        geoButton = view.findViewById(R.id.student_difficult_geo);
        uploadImageButton = view.findViewById(R.id.student_upload_photo_button);
        profileImage = view.findViewById(R.id.student_profile);
        addStudentButton = view.findViewById(R.id.student_add_student_button);
    }

    public void onEngClicked() {
        if (IS_ENG_SELECTED) {
            IS_ENG_SELECTED = false;
            engButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            engButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_ENG_SELECTED = true;
            engButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            engButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onHinClicked() {
        if (IS_HIN_SELECTED) {
            IS_HIN_SELECTED = false;
            hinButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            hinButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_HIN_SELECTED = true;
            hinButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            hinButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onTLClicked() {
        if (IS_TL_SELECTED) {
            IS_TL_SELECTED = false;
            tlButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            tlButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_TL_SELECTED = true;
            tlButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            tlButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onMatClicked() {
        if (IS_MAT_SELECTED) {
            IS_MAT_SELECTED = false;
            matButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            matButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_MAT_SELECTED = true;
            matButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            matButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onSciClicked() {
        if (IS_SCI_SELECTED) {
            IS_SCI_SELECTED = false;
            sciButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            sciButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_SCI_SELECTED = true;
            sciButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            sciButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onHisClicked() {
        if (IS_HIS_SELECTED) {
            IS_HIS_SELECTED = false;
            hisButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            hisButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_HIS_SELECTED = true;
            hisButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            hisButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    public void onGeoClicked() {
        if (IS_GEO_SELECTED) {
            IS_GEO_SELECTED = false;
            geoButton.setBackground(getContext().getResources().getDrawable(R.drawable.input_bg));
            geoButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        } else {
            IS_GEO_SELECTED = true;
            geoButton.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg));
            geoButton.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    private void setSubjectButtons() {
        engButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEngClicked();
            }
        });

        hinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHinClicked();
            }
        });

        tlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTLClicked();
            }
        });

        matButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMatClicked();
            }
        });

        sciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSciClicked();
            }
        });

        hisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHisClicked();
            }
        });

        geoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGeoClicked();
            }
        });
    }
}