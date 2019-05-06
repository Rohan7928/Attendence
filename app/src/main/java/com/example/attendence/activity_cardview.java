package com.example.attendence;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.renderscript.Float4;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import static com.example.attendence.Util.getDate;
import static io.opencensus.tags.TagValue.MAX_LENGTH;

public class activity_cardview extends AppCompatActivity implements View.OnClickListener {
    TextView sem_num,date,Time;
    EditText sub_name, sub_divison, sub_dept,rollfrom, rollto;
    RadioGroup radioGroup;
    Button btn_add;
    RadioButton lecture, lab,seminar,workshop,exam;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    int count=1;
    String type="";
    Subjects subjects;
    String id,email;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        db = FirebaseFirestore.getInstance();

        subjects = new Gson().fromJson(getIntent().getStringExtra("list"),Subjects.class);
        id = getIntent().getStringExtra("id");

        Time=findViewById(R.id.time);
        date=findViewById(R.id.current_date);
        sem_num = findViewById(R.id.Sem);
        sub_divison = findViewById(R.id.Division);
        sub_name = findViewById(R.id.Subname);
        sub_dept = findViewById(R.id.Department);
        rollfrom = findViewById(R.id.roll_from);
        rollto = findViewById(R.id.roll_to);
        radioGroup = findViewById(R.id.rd_group);
        lecture=findViewById(R.id.rd_lecture);
        lab=findViewById(R.id.rd_lab);
        exam=findViewById(R.id.rd_exam);
        workshop=findViewById(R.id.rd_workshop);
        seminar=findViewById(R.id.rd_seminar);
        btn_add = findViewById(R.id.button_add);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_lecture) {
                    type = lecture.getText().toString().trim();
                }
                if (checkedId == R.id.rd_lab) {
                    type = lab.getText().toString().trim();
                }
                if (checkedId == R.id.rd_exam) {
                    type = exam.getText().toString().trim();
                }
                if (checkedId == R.id.rd_seminar) {
                    type = seminar.getText().toString().trim();
                }
                if (checkedId == R.id.rd_workshop) {
                    type = workshop.getText().toString().trim();
                }
            }
        });

        if(subjects==null)
        {

        }
        else {
            sem_num.setText(subjects.semester);
            sub_name.setText(subjects.sub_name);
            sub_dept.setText(subjects.sub_dept);
            sub_divison.setText(subjects.sub_division);
            rollfrom.setText(String.valueOf(subjects.rollfrom));
            rollto.setText(String.valueOf(subjects.rollto));
            String type = subjects.getType();
            if (type.equals("Lecture")) {
                lecture.setChecked(true);
            } else if (type.equals("Lab")) {
                lab.setChecked(true);
            } else if (type.equals("Seminar")) {
                seminar.setChecked(true);
            } else if (type.equals("Workshop")) {
                workshop.setChecked(true);
            } else if (type.equals("Exam")) {
                exam.setChecked(true);
            } else {
                return;
            }
        }
           final Long timestemp = new Date().getTime();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
            String strDate = mdformat.format(calendar.getTime());

            date.setOnClickListener(this);
           Time.setText(strDate);
              btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semester = sem_num.getText().toString();
                String subname = sub_name.getText().toString().trim();
                String subdivision = sub_divison.getText().toString().trim();
                String subdept = sub_dept.getText().toString().trim();
                String email= auth.getCurrentUser().getEmail();
                int roll_from = Integer.parseInt(rollfrom.getText().toString().trim());
                int roll_to = Integer.parseInt(rollto.getText().toString().trim());
                String current = date.getText().toString().trim();

                if (TextUtils.isEmpty(subname)) {
                    sub_name.setError("Enter subject name");
                    Toast.makeText(activity_cardview.this, "Enter Subject", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(subdivision)) {
                    sub_divison.setError("Enter subject code");
                    Toast.makeText(activity_cardview.this, "Enter Division", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(subdept)) {
                    sub_dept.setError("Enter department");
                    Toast.makeText(activity_cardview.this, "Enter Department", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(String.valueOf(roll_from))) {
                    rollfrom.setError("Enter a number");
                    Toast.makeText(activity_cardview.this, "Enter Roll from", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(String.valueOf(roll_to))) {
                    rollto.setError("Enter a number");
                    Toast.makeText(activity_cardview.this, "Enter Roll to", Toast.LENGTH_SHORT).show();
                }
                //if (roll_from > roll_to) {
                //  rollfrom.setError("Starting Number should be greater");
                //Toast.makeText(activity_cardview.this, "Starting Number should be greater", Toast.LENGTH_SHORT).show();
                //}

                if (subjects == null) {
                    storedata(email,semester, subname, subdept, subdivision, type, roll_from, roll_to, current);
                }else
                {
                    updatedata(email,semester, subname, subdept, subdivision, type, roll_from, roll_to, current);
                }
            }
        });

    }

    private void storedata(String email, String semester, String subname, String subdept, String subdivision, String type, int start, int end, String current) {
        progressDialog.show();

        List<Student> student = new ArrayList<>();
        for (int c = start; c <= end; c++) {
            Student stu = new Student("student " + c, c, false,current);
            student.add(stu);
        }
        Subjects subjects = new Subjects(email,semester,subname,subdept,subdivision, type, student, start, end,current);
        db.collection("Data").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Subjects")
                .document(UUID.randomUUID().toString()).
               set(subjects)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toast.makeText(activity_cardview.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity_cardview.this, "Subject", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_cardview.this, activity_navigation.class));
                    progressDialog.dismiss();
                }
            }
        });
    }
    private void updatedata(String email, String semester, String subname, String subdept, String subdivision, String type, int start, int end, String current) {
        progressDialog.show();

        List<Student> student = new ArrayList<>();
        for (int c = start; c <= end; c++) {
            Student stu = new Student("student " + c, c, false,current);
            student.add(stu);
        }
            Subjects subjects = new Subjects(email,semester,subname,subdept,subdivision, type, student, start, end,current);
        db.collection("Data").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Subjects")
                .document(UUID.randomUUID().toString()).set(subjects)
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.hide();
                    Toast.makeText(activity_cardview.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_cardview.this, "Subject", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity_cardview.this, activity_navigation.class));

                        progressDialog.dismiss();
                    }
                }
            });

        }

    public void DecreaseInt(View view) {
     if(count>1)
     {
         count=count-1;
         display(count);
     }
     else
     {
         Toast.makeText(this, "Semester Starts from 1", Toast.LENGTH_SHORT).show();
     }
    }

    private void display(int num) {
        //TextView textView=findViewById(R.id.Sem);
        sem_num.setText("" +num);
    }

    public void IncreaseInt(View view) {
        count=count+1;
        display(count);
    }
//Date picker
    @Override
    public void onClick(View v) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(activity_cardview.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        //tvDOB.setText(sdf.format(cldr.getTime()));
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }
}