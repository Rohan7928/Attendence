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
    Button btnsubmit, bthome;
    EditText etfirst,etlast,etemail,etmobile,etpassword,etconfirmpass,Department;

    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnsubmit=findViewById(R.id.btSubmit);
        bthome=findViewById(R.id.bthomepage);
        Department=findViewById(R.id.et_Department);
        etfirst= findViewById(R.id.etFirstName);
        etlast= findViewById(R.id.etLastName);
        etemail= findViewById(R.id.etEmail);
        etpassword= findViewById(R.id.etPassword);
        etconfirmpass= findViewById(R.id.etConfirmpass);
        etmobile= findViewById(R.id.etMobile);
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
                case R.id.bthomepage:
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
        final String department=Department.getText().toString().trim();
        if(fname.isEmpty() || lname.isEmpty() ||email.isEmpty()||pass.isEmpty()||confirmpass.isEmpty() ||phone.isEmpty())
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
                    savedata(fname,lname,email,phone,department);
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

    private void savedata(String fname, String lname, String email, String phone, String department) {
    progressDialog.show();
        UserDataR user=new UserDataR(fname, lname, email,phone,"Teacher",department);
        fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
         .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
     @Override
     public void onComplete(@NonNull Task<Void> task) {
         if(task.isSuccessful())
         {
           progressDialog.dismiss();
             Toast.makeText(activity_signup.this, "Record", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(getApplicationContext(),activity_navigation.class));
              finish();
         }
     }
 }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               finish();
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