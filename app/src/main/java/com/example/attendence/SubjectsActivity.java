package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SubjectsActivity extends AppCompatActivity implements StudentAdapter.doAlert {
    RecyclerView recyclerView;
    Button back;
    StudentAdapter adapter;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        uid=getIntent().getStringExtra("uid");
        recyclerView = findViewById(R.id.recycler);
        back=findViewById(R.id.backsubjects);
        adapter=new StudentAdapter(getApplicationContext(),this,uid);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getsavedata();
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),studenthome.class));
               finish();
           }
       });
    }

    @Override
    public void onAlert(int i, String s1, String s) {

    }
        private void getsavedata() {
        FirebaseFirestore.getInstance().collection("Data").document(uid).collection("Subjects")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subjects subjects = document.toObject(Subjects.class);
                            adapter.addData(subjects, document.getId());
                            adapter.notifyDataSetChanged();
                            Log.e("subject ", subjects.sub_name);

                    }
                }
            }

        });
    }
}
