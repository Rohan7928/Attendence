package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class activity_viewstatus extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewStatusAdapter viewStatusAdapter;
    ProgressDialog progressDialog;
    TextView txtstatus;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstatus);
        auth=FirebaseAuth.getInstance();
        txtstatus=findViewById(R.id.txt_status);
        db = FirebaseFirestore.getInstance();
        floatingActionButton=findViewById(R.id.fab_edit);
        viewStatusAdapter=new   ViewStatusAdapter(this);
        recyclerView = findViewById(R.id.Recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(viewStatusAdapter);
        getsavedata();
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),activity_status.class));
        }
    });

    }

    private void getsavedata() {
        db.collection("Data")
                .orderBy("timesep", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().size()== 0)
                    {
                        txtstatus.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txtstatus.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Teacherstatus teacherstatus = document.toObject(Teacherstatus.class);
                            viewStatusAdapter.addData(teacherstatus, document.getId());
                            viewStatusAdapter.notifyDataSetChanged();

                            //Log.e("subject ", subjects.sub_name);
                        }
                    }
                }
            }
        });

    }
}
