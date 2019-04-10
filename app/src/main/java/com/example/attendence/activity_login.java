package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity implements View.OnClickListener {
    TextView forgot;
    EditText etUser, etPassword;
    Button btLogin, bthome;
    ImageView checkUser, checkPassword;
    DataBaseHelper helper;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
       forgot=findViewById(R.id.forgot_pass);
        btLogin =  findViewById(R.id.btLogin);
        bthome =  findViewById(R.id.bthome);
        checkUser = findViewById(R.id.checkuser);
        checkPassword = findViewById(R.id.checkpassword);
        btLogin.setOnClickListener(this);
        bthome.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        auth = FirebaseAuth.getInstance();

        helper = new DataBaseHelper(this);

        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUser.getText().length() > 4)
                    checkUser.setVisibility(View.VISIBLE);
                else
                    checkUser.setVisibility(View.GONE);
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPassword.getText().length() > 4)
                    checkPassword.setVisibility(View.VISIBLE);
                else
                    checkPassword.setVisibility(View.GONE);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Change_password.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btLogin:
                login();
                break;
            case R.id.bthome:
                home();
                break;
        }
    }

    private void home() {
        Intent intent = new Intent(this, activity_choose.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
    }

    private void login() {
        String username = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            etUser.setError("Enter valid email");
            return;
        }

            if (username.isEmpty() || password.isEmpty()) {
            Util.toast(this, "Not valid");
        } else {
            dologin(username, password);

        }
    }

    private void dologin(String username, String password) {
        progressDialog.show();
        auth.signInWithEmailAndPassword(username, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "User login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), activity_navigation.class));
                    }
                });
       /* long id= helper.save(username,password);
        if(id<0)
        {
            Util.toast(this,"Save");
        }
        else
        {
            Util.toast(this,"Error");
        }
        // Intent intent=new Intent(activity_login.this,MainActivity.class);
        //intent.putExtra("UserName",username);
        //startActivity(intent);
        //pref.setIsLlogin(true);*/

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Register in Fire base
    private void doregister() {
        progressDialog.show();
        auth.createUserWithEmailAndPassword("Rohan@gmail.com", "rohan123")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "User is not Cretaed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}