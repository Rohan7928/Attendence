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
import android.widget.LinearLayout;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class studenthome extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    StatusHome statusadapter;
    ProgressDialog progressDialog;
    NavigationView navigationView;
    ImageView imageView;
    Button add;
    TextView infostatus;
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
        infostatus=findViewById(R.id.txt_infostatus);
        add=findViewById(R.id.add);
        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
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
        View view=navigationView.getHeaderView(0);
        LinearLayout home=view.findViewById(R.id.header_Home);
        LinearLayout status=view.findViewById(R.id.header_Status);
        LinearLayout changeassword=view.findViewById(R.id.header_Changepass);
        final CircularImageView imageView=view.findViewById(R.id.student_photo);

        LinearLayout logout=view.findViewById(R.id.header_Logout);
        final TextView User=view.findViewById(R.id.header_User);
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
                User.setText(userDataR.getFname() +" "+ userDataR.getLname());
                Picasso.get().load(userDataR.getProfileurl()).into(imageView);

            }
        });
    }
   private void getsaveddata() {
      db.collection("users").document(auth.getUid())
              .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if (task.isSuccessful())
              {
                  infostatus.setVisibility(View.GONE);
                  UserDataR da=task.getResult().toObject(UserDataR.class);
                  db.collection("users")
                          .whereEqualTo("department", da.getDepartment())
                          .whereEqualTo("type","Teacher")
                          .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful())
                          {

                                  for (QueryDocumentSnapshot document : task.getResult()) {
                                      UserDataR teacherstatus = document.toObject(UserDataR.class);
                                      statusadapter.addData(teacherstatus, document.getId());
                                      statusadapter.notifyDataSetChanged();
                                      //Log.e("subject ", subjects.sub_name);

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
      });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.header_Home: {
                startActivity(new Intent(getApplicationContext(), studenthome.class));
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_Status: {
                getstatus();
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_Changepass: {
                startActivity(new Intent(getApplicationContext(), activity_studentpassword.class));
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.header_Logout: {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), student_login.class));
                drawerLayout.closeDrawer(Gravity.START);
                break;
            }
        }
    }

    private void getstatus() {

        db.collection("users").document(auth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    UserDataR da=task.getResult().toObject(UserDataR.class);
                    db.collection("users")
                            .whereEqualTo("department", da.getDepartment())
                            .whereEqualTo("type","Teacher")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    UserDataR teacherstatus = document.toObject(UserDataR.class);
                                    Intent intent=new Intent(getApplicationContext(),activity_viewstatus.class);
                                    intent.putExtra("uid",teacherstatus.getUid());
                                    intent.putExtra("isstudent","true");
                                    startActivity(intent);
                                    //Log.e("subject ", subjects.sub_name);

                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
