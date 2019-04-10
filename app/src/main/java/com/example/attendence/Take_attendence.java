package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.attendence.Util.getDate;

public class Take_attendence extends AppCompatActivity {
RecyclerView recyclerView;
    TextView date;
    Button btnattend;
    FirebaseAuth auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);
        final Subjects subject = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
      recyclerView=findViewById(R.id.attend_recycler);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        date = findViewById(R.id.txtdate);
        btnattend = findViewById(R.id.btn_attend);
        final AttendAdapter attendAdapter=new AttendAdapter(this,subject.students);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(attendAdapter);
      date.setText(subject.current);
        //final Long timestemp=new Date().getTime();
    //date.setText(getDate(timestemp));
    btnattend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Take_attendence.this, "xdjxc", Toast.LENGTH_SHORT).show();
           List<Student> list=attendAdapter.list;
            Attendence attendence=new  Attendence();
            attendence.setList(list);
           db.collection("Subjects").document(FirebaseAuth.getInstance().getUid())
                   .collection("attendence").document(String.valueOf(date)).set(attendence)
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Take_attendence.this, "Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),activity_navigation.class));
                        }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(Take_attendence.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });

        }
    });

    }

}
