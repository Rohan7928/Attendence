package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Change_password extends AppCompatActivity {
    EditText Email;
    Button verify,btnchange;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Email = findViewById(R.id.Email);
        verify = findViewById(R.id.btforgot);
        btnchange = findViewById(R.id.btchangepass);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        firebaseuser = auth.getCurrentUser();
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),activity_navigation.class));
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString();
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    Toast.makeText(Change_password.this, "Enter Valid E mail", Toast.LENGTH_SHORT).show();
               else
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Change_password.this, "Email sent ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                          }
        });


    }
}
