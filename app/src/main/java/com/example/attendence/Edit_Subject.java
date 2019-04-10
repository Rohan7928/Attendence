package com.example.attendence;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class Edit_Subject extends AppCompatActivity {
     int count=1;
    TextView sem_num;
    EditText sub_name, sub_divison, sub_dept,rollfrom, rollto,Time,date;
    RadioGroup radioGroup;
    Button btn_add;
    RadioButton lecture, lab,seminar,workshop,exam;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    String type=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__subject);

        Subjects subjects= new Gson().fromJson(getIntent().getStringExtra("list"),Subjects.class);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        db = FirebaseFirestore.getInstance();

        sem_num = findViewById(R.id.Semnum);
        sub_divison = findViewById(R.id.Div);
        sub_name = findViewById(R.id.Subjectname);
        sub_dept = findViewById(R.id.Dept);
        rollfrom = findViewById(R.id.rollfrom);
        rollto = findViewById(R.id.rollto);
        radioGroup = findViewById(R.id.rdgroup);
        lecture=findViewById(R.id.rdlecture);
        lab=findViewById(R.id.rdlab);
        exam=findViewById(R.id.rdexam);
        workshop=findViewById(R.id.rdworkshop);
        seminar=findViewById(R.id.rdseminar);
        btn_add = findViewById(R.id.buttonadd);

        if (subjects==null)
        {

        }else
        {
            sem_num.setText(subjects.semester);
            sub_name.setText(subjects.sub_name);
        }
        db.collection("Subjects").document(FirebaseAuth.getInstance().getUid()).collection("subjects").document().get();
    }

    public void IncreaseInt(View view) {
        count=count+1;
        display(count);
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

    private void display(int count) {
    sem_num.setText("");
    }

}
