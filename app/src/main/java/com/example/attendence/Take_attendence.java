package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.attendence.Util.getDate;

public class Take_attendence extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView date;
    DrawerLayout drawerLayout;
    ImageView uncheck , checkall;
    Button drawer;
   FloatingActionButton btnattend;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        final String id=getIntent().getStringExtra("id");
        final Subjects subject = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
        recyclerView = findViewById(R.id.attend_recycler);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        date = findViewById(R.id.txtdate);
        checkall = findViewById(R.id.checkall);
        drawer = findViewById(R.id.btn_back);
        uncheck = findViewById(R.id.uncheckall);
        btnattend = findViewById(R.id.btn_attend);
        final AttendAdapter attendAdapter = new AttendAdapter(this, subject.students,checkall,uncheck);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(attendAdapter);
        final Long timestemp = new Date().getTime();
        date.setText(getDate(timestemp));
       progressDialog=new ProgressDialog(this);
       progressDialog.setMessage("Saving Attendance..");
       progressDialog.setCanceledOnTouchOutside(false);
       progressDialog.setCancelable(false);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),activity_navigation.class));
            }
        });

        btnattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                List<Student> list = attendAdapter.list;
                String currentdate = date.getText().toString().trim();
                Attendence attendence = new Attendence();
                attendence.setList(list);
                attendence.setDate(currentdate);
                attendence.setSub_id(subject.sub_division);
                attendence.setSub_name(subject.sub_name);
                attendence.setSem(subject.semester);

                db.collection("Data").document(FirebaseAuth.getInstance().getUid())
                        .collection("Attendence")
                        .document(id).collection("attendence")
                        .document(currentdate).set(attendence)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Take_attendence.this, "Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), activity_navigation.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Take_attendence.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
