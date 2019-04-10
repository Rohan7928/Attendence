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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.attendence.Util.getDate;

public class Update extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnupdate;
    FirebaseAuth auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        final Subjects subject = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
        final String id=getIntent().getStringExtra("id");
        recyclerView=findViewById(R.id.attend_recycler);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnupdate = findViewById(R.id.btn_update);
        final UpdateAdapter updateAdapter=new UpdateAdapter(this,subject.students);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(updateAdapter);
        final Long timestemp=new Date().getTime();
btnupdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Map<String,List<Student>> map=new HashMap<>();
        List<Student> list=updateAdapter.list;
        map.put("students",list);
        db.collection("Subjects").document(FirebaseAuth.getInstance().getUid())
                .collection("subjects").document(id).update("students",list)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Update.this, "Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),activity_navigation.class));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Update.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(Update.this, "canceled", Toast.LENGTH_SHORT).show();
            }
        });

    }
});
    }
}
