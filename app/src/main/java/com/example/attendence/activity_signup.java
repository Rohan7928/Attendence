package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
public class activity_signup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button btnsubmit;
    EditText etfirst,etlast,etemail,etmobile,etfather,etmother,etaddress,etpassword,etconfirmpass;

    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnsubmit=findViewById(R.id.btSubmit);
        etfirst= findViewById(R.id.etFirstName);
        etlast= findViewById(R.id.etLastName);
        etemail= findViewById(R.id.etEmail);
        etpassword= findViewById(R.id.etPassword);
        etconfirmpass= findViewById(R.id.etConfirmpass);
        etmobile= findViewById(R.id.etMobile);
        etfather= findViewById(R.id.etFather);
        etmother= findViewById(R.id.etMother);
        etaddress= findViewById(R.id.etAddress);
        btnsubmit.setOnClickListener(this);
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
        }
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
        if(fname.isEmpty() || lname.isEmpty() ||email.isEmpty()||pass.isEmpty()||confirmpass.isEmpty() ||phone.isEmpty()||father.isEmpty()||mother.isEmpty()||address.isEmpty())
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
                    Toast.makeText(activity_signup.this, "User Registered", Toast.LENGTH_SHORT).show();
                    savedata(fname,lname,email,pass,phone,father,mother,address);
                } else {
                    Toast.makeText(activity_signup.this, "User not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_signup.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savedata(String fname, String lname, String email, String pass, String phone, String father, String mother, String address) {

        CollectionReference ROLL=fb.collection("Roll No.");

        UserDataR user=new UserDataR(fname, lname, email,pass,phone,father,mother,address);
 fb.collection("Roll No.").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
     @Override
     public void onComplete(@NonNull Task<Void> task) {
         if(task.isSuccessful())
         {
           progressDialog.dismiss();
             Toast.makeText(activity_signup.this, "Record", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(getApplicationContext(),activity_navigation.class));
         }
         else
         {
             progressDialog.dismiss();
             Toast.makeText(activity_signup.this, "Sorry", Toast.LENGTH_SHORT).show();

         }
     }
 });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}