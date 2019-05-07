package com.example.attendence;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class studenthome extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    StatusHome statusadapter;
    ProgressDialog progressDialog;
    NavigationView navigationView;
    ImageView imageView;
    TextView statusdata,status;
    Button add;
    FirebaseAuth auth;
    FirebaseFirestore db;
    AlertDialog dialog;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studenthome);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait a sec...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        drawerLayout=findViewById(R.id.draw__able);
        navigationView=findViewById(R.id.navigation_view);
        imageView=findViewById(R.id.btn_im);
        add=findViewById(R.id.add);
        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        status = findViewById(R.id.txt_info);
        statusdata=findViewById(R.id.tv_info);
        statusadapter=new StatusHome(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(statusadapter);
         getsaveddata();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(Gravity.START))
                {
                    drawerLayout.closeDrawer(Gravity.START);
                }
                else
                {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.homepage:
                    {
                        startActivity(new Intent(getApplicationContext(),studenthome.class));
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.attendance:
                    {
                       // startActivity(new Intent(getApplicationContext(),ViewAttendence.class));
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.changepass:
                    {
                      startActivity(new Intent(getApplicationContext(),activity_studentpassword.class));
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }case R.id.logout:
                    {
                        auth.signOut();
                        startActivity(new Intent(studenthome.this,student_login.class));
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                }
                return true;
            }
        });
    }
   private void getsaveddata() {
        db.collection("Data")
                .orderBy("timesep", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().size()== 0)
                    {
                        status.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        status.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Teacherstatus teacherstatus = document.toObject(Teacherstatus.class);
                            statusadapter.addData(teacherstatus, document.getId());
                            statusadapter.notifyDataSetChanged();
                            //Log.e("subject ", subjects.sub_name);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(studenthome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
