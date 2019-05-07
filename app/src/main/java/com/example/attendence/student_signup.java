package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class student_signup extends AppCompatActivity implements View.OnClickListener {
    Button btnsubmit,bthome;
    EditText etfirst,etlast,etemail,etmobile,etfather,etmother,etaddress,etpassword,etconfirmpass;
    EditText etsem,etdept,etclass;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        btnsubmit=findViewById(R.id.btSubmit);
        bthome=findViewById(R.id.bthomepage1);
        etfirst= findViewById(R.id.etFirstName);
        etlast= findViewById(R.id.etLastName);
        etsem=findViewById(R.id.etsem);
        etdept=findViewById(R.id.etDepartment);
        etclass=findViewById(R.id.etclass);
        etemail= findViewById(R.id.etEmail);
        etpassword= findViewById(R.id.etPassword);
        etconfirmpass= findViewById(R.id.etConfirmpass);
        etmobile= findViewById(R.id.etMobile);
        etfather= findViewById(R.id.etFather);
        etmother= findViewById(R.id.etMother);
        etaddress= findViewById(R.id.etAddress);
        btnsubmit.setOnClickListener(this);
        bthome.setOnClickListener(this);
        fb = FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        auth=FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.btSubmit:
                Submit();
                break;
            case R.id.bthomepage1:
                Home();
                break;
        }
    }

    private void Home() {
        Intent intent = new Intent(this, activity_choose.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
    }

    private void Submit() {
        final String fname=etfirst.getText().toString().trim();
        final String lname=etlast.getText().toString().trim();
        final String email=etemail.getText().toString().trim();
        final String pass=etpassword.getText().toString().trim();
        String confirmpass=etconfirmpass.getText().toString().trim();
        final String phone=etmobile.getText().toString().trim();
        final String father=etfather.getText().toString().trim();
        final String mother=etmother.getText().toString().trim();
        final String address=etaddress.getText().toString().trim();
        final String sem=etsem.getText().toString().trim();
        final String department=etdept.getText().toString().trim();
        final String clas=etclass.getText().toString().trim();

        if(fname.isEmpty() || lname.isEmpty() ||email.isEmpty()||pass.isEmpty()||confirmpass.isEmpty() ||phone.isEmpty()||father.isEmpty()||mother.isEmpty()||address.isEmpty()||sem.isEmpty()||department.isEmpty()||clas.isEmpty())
        {
            Util.toast(this,"Fill all the Requirments");
        }
        else
        {
            if(pass.equals(confirmpass)) {

            }
            else
            {
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            }
        }
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(student_signup.this, "User Registered", Toast.LENGTH_SHORT).show();
                    savedata(fname,lname,email,pass,phone,father,mother,address,sem,department,clas);
                } else {
                    Toast.makeText(student_signup.this, "User not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(student_signup.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void savedata(String fname, String lname, String email, String pass, String phone, String father, String mother, String address, String sem, String department, String clas) {
        UserDataR user=new UserDataR(fname, lname, email,pass,phone,father,mother,address,sem,department,clas,"Student");
        fb.collection("StudentData").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("user")
                .document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(student_signup.this, "Record", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),studenthome.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(student_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

