package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class edit_teacher extends AppCompatActivity implements View.OnClickListener {
    Button btnsubmit, btncancel;
    EditText etfirst, etlast, etmobile,Department;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);
        btnsubmit = findViewById(R.id.btnsubmit);
        btncancel = findViewById(R.id.btncancel);
        Department = findViewById(R.id.etxtDepartment);
        etfirst = findViewById(R.id.etxtFirstName);
        etlast = findViewById(R.id.etxtLastName);
        etmobile = findViewById(R.id.etxtMobile);
        btnsubmit.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating Data");
        progressDialog.setMessage("wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                UserDataR userDataR=documentSnapshot.toObject(UserDataR.class);
                etfirst.setText(userDataR.getFname());
                etlast.setText(userDataR.getLname());
                etmobile.setText(userDataR.getPhone());
                Department.setText(userDataR.getDepartment());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnsubmit:
                Submit();
                break;
            case R.id.btncancel:
                Home();
                break;

        }
    }

    private void Home() {
        startActivity(new Intent(getApplicationContext(),profile.class));
        finish();
    }

    private void Submit() {
        progressDialog.show();
        final String fname=etfirst.getText().toString().trim();
        final String lname=etlast.getText().toString().trim();
        final String phone=etmobile.getText().toString().trim();

         final String department=Department.getText().toString().trim();
        if(fname.isEmpty() || lname.isEmpty() ||phone.isEmpty() || department.isEmpty())
        {
            Util.toast(this,"Fill all the Requirments");
            return;
        }
        else
        {

        }
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())

                .update("fname",fname,"lname",lname,"phone",phone,"department",department)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(edit_teacher.this, "Data update", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),activity_navigation.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(edit_teacher.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
