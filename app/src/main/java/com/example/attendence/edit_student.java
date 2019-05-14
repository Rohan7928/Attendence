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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

public class edit_student extends AppCompatActivity implements View.OnClickListener {
    Button btnsubmit,btncancel;
    EditText etfirst, etlast, etmobile, etfather, etmother, etaddress;
    EditText etsem, etdept, etclass;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        btnsubmit = findViewById(R.id.submit);
        btncancel = findViewById(R.id.cancel);
        etfirst = findViewById(R.id.FirstName);
        etlast = findViewById(R.id.LastName);
        etsem = findViewById(R.id.sem);
        etdept = findViewById(R.id.Department);
        etclass = findViewById(R.id.Class);
        etmobile = findViewById(R.id.Mobile);
        etfather = findViewById(R.id.Father);
        etmother = findViewById(R.id.Mother);
        etaddress = findViewById(R.id.Address);
        btnsubmit.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        fb = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Data");
        progressDialog.setMessage("wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();
         fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                UserDataR userDataR=documentSnapshot.toObject(UserDataR.class);
                etfirst.setText(userDataR.getFname());
                etlast.setText(userDataR.getLname());
                etmobile.setText(userDataR.getPhone());
                etfather.setText(userDataR.getFather());
                etmother.setText(userDataR.getMother());
                etsem.setText(userDataR.getSem());
                etdept.setText(userDataR.getDepartment());
                etclass.setText(userDataR.getClas());
                etaddress.setText(userDataR.getAddress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submit:
                Submit();
                break;
            case R.id.cancel:
                Home();
                break;
        }
    }

    private void Home() {
     startActivity(new Intent(getApplicationContext(),student_profile.class));
     finish();
    }

    private void Submit() {
        progressDialog.show();
        final String fname = etfirst.getText().toString().trim();
        final String lname = etlast.getText().toString().trim();
        final String phone = etmobile.getText().toString().trim();
        final String father = etfather.getText().toString().trim();
        final String mother = etmother.getText().toString().trim();
        final String address = etaddress.getText().toString().trim();
        final String sem = etsem.getText().toString().trim();
        final String department = etdept.getText().toString().trim();
        final String clas = etclass.getText().toString().trim();

        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || father.isEmpty() || mother.isEmpty() || address.isEmpty() || sem.isEmpty() || department.isEmpty() || clas.isEmpty()) {
            Util.toast(this, "Fill all the Requirments");
            return;
        } else {

            }
        fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())

                .update("fname",fname,"lname",lname,"father",father,"mother",mother,"phone",phone,"sem",sem,"clas",clas,"department",department,"address",address)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(edit_student.this, "Data update", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(getApplicationContext(),studenthome.class));
                       finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(edit_student.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
