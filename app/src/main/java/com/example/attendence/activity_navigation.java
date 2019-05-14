package com.example.attendence;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class activity_navigation extends AppCompatActivity implements StudentAdapter.doAlert, View.OnClickListener {
  DrawerLayout drawerLayout;
  RecyclerView recyclerView;
  StudentAdapter adapter;
  ProgressDialog progressDialog;
  NavigationView navigationView;
  ImageView imageView;
  TextView status;
  FloatingActionButton add;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a sec...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        drawerLayout = findViewById(R.id.drawable);
        navigationView = findViewById(R.id.navigation);
        imageView = findViewById(R.id.btn_img);
        add = findViewById(R.id.Add);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler);
        status = findViewById(R.id.status);
        adapter = new StudentAdapter(getApplicationContext(), this, "");
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getsavedata();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_navigation.this, activity_cardview.class));
            }
        });


        View view = navigationView.getHeaderView(0);
        LinearLayout home = view.findViewById(R.id.header_profile);
        LinearLayout status = view.findViewById(R.id.header_status);
        LinearLayout changeassword = view.findViewById(R.id.header_changepass);
        LinearLayout logout = view.findViewById(R.id.header_logout);
        final CircularImageView imageView=view.findViewById(R.id.teacher_photo);
        final TextView user = view.findViewById(R.id.header_user);
        home.setOnClickListener(this);
        status.setOnClickListener(this);
        changeassword.setOnClickListener(this);
        logout.setOnClickListener(this);




        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                UserDataR userDataR=documentSnapshot.toObject(UserDataR.class);
                user.setText(userDataR.getFname() +" "+userDataR.getLname());
                Picasso.get().load(userDataR.getProfileurl()).into(imageView);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.header_profile:
            {
                startActivity(new Intent(getApplicationContext(),profile.class));
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_status:
            {

               Intent intent=new Intent(getApplicationContext(),activity_viewstatus.class);
                intent.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_changepass:
            {
                startActivity(new Intent(getApplicationContext(),activity_studentpassword.class));
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_logout:
            {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(),activity_login.class));
            drawerLayout.closeDrawer(Gravity.START);
            finish();
            break;
            }
        }

    }

    private void getsavedata() {
        db.collection("Data").document(FirebaseAuth.getInstance().getUid()).collection("Subjects")
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
    @Override
    public void onAlert(final int i, final String s1, final String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String uid = FirebaseAuth.getInstance().getUid();
                        DocumentReference reference = db.collection("Data").document(uid).collection("Subjects").document(s1);
                        reference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        deleteAttendence(s1,i);
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
    private void deleteAttendence(String s, final int i) {
        String uid = FirebaseAuth.getInstance().getUid();
        DocumentReference reference = db.collection("Data").document(uid).collection("Attendence").document(s);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity_navigation.this, "Attendence Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
