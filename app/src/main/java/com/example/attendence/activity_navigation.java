package com.example.attendence;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
// ,fmldlflkjdfjdlkjfldkjf
public class activity_navigation extends AppCompatActivity implements StudentAdapter.doAlert {
  DrawerLayout drawerLayout;
  RecyclerView recyclerView;
  StudentAdapter adapter;
  ProgressDialog progressDialog;
  NavigationView navigationView;
  ImageView imageView;
  TextView status;
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
        setContentView(R.layout.activity_navigation);
       progressDialog=new ProgressDialog(this);
       progressDialog.setMessage("Wait a sec...");
       progressDialog.setCanceledOnTouchOutside(false);
       progressDialog.setCancelable(false);


        drawerLayout=findViewById(R.id.drawable);
        navigationView=findViewById(R.id.navigation);
        imageView=findViewById(R.id.btn_img);
        add=findViewById(R.id.Add);
        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler);
        status = findViewById(R.id.status);
       adapter=new StudentAdapter(getApplicationContext(),this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
       getsave();
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
                    case R.id.home_page:
                    {
                        startActivity(new Intent(getApplicationContext(),activity_fragment.class));
                      // replaceFragment(new activity_fragment());
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.add_subject:
                    {

                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.view_subject:
                    {
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.enter_student:
                    {
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }
                    case R.id.student_details:
                    {
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }case R.id.change_pass:
                    {
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }case R.id.log_out:
                    {
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    }


                }
                return true;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_navigation.this,activity_cardview.class));
            }
        });
    }

    private void getsave() {
        db.collection("Subjects").document(FirebaseAuth.getInstance().getUid()).collection("subjects")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if (task.getResult().size()== 0)
                    {
                        status.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        status.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subjects subjects = document.toObject(Subjects.class);
                            adapter.addData(subjects, document.getId());
                            adapter.notifyDataSetChanged();
                            Log.e("subject ", subjects.sub_name);
                        }
                    }
                }
            }

        });

    }



    public void initdialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("do you wan'na leave the App");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),activity_choose.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
   dialog=builder.create();
   dialog.setCancelable(false);
   dialog.setCanceledOnTouchOutside(false);


    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_frame, fragment);
        transaction.commit();
    }

    @Override
    public void onAlert(final int i, final String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String uid = FirebaseAuth.getInstance().getUid();
                        DocumentReference reference = db.collection("Subjects").document(uid).collection("subjects").document(s);
                        reference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        adapter.removeData(i);
                                        adapter.notifyDataSetChanged();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
