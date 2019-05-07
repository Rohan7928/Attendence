package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_studentpassword extends AppCompatActivity {
    EditText Email;
    EditText pass,confirmpass,phn;
    Button change,verify;
    CardView card;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentpassword);
        Email = findViewById(R.id.edit_Email);
        pass = findViewById(R.id.etpass);
        phn = findViewById(R.id.etnumber);
        confirmpass = findViewById(R.id.etconfirmpass);
        change = findViewById(R.id.btnChange);
        verify = findViewById(R.id.btn_Verify);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        card = findViewById(R.id.Cardview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        firebaseuser = auth.getCurrentUser();
        card.setVisibility(View.GONE);
        phn.setVisibility(View.INVISIBLE);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = Email.getText().toString();
                progressDialog.show();
                db.collection("StudentData").document(FirebaseAuth.getInstance().getUid()).collection("user")
                        .document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserDataR userDataR = documentSnapshot.toObject(UserDataR.class);
                        String e_mail=userDataR.getEmail();
                        String phone = userDataR.getPhone();
                        if (e_mail.equals(mail)) {
                            progressDialog.dismiss();
                            phn.setVisibility(View.VISIBLE);
                            enternumber(phone);
                        }
                        else
                        {
                            Toast.makeText(activity_studentpassword.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_studentpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass.getText().toString().trim();
                String cpasword = confirmpass.getText().toString();
                progressDialog.show();
                if (password.equals(cpasword)) {
                    progressDialog.dismiss();
                    changepassword(password);
                    userpassword(password);
                } else {
                    Toast.makeText(activity_studentpassword.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enternumber(String phone) {
        final String mobile = phn.getText().toString();
        if(phone.equals(mobile))
        {
            card.setVisibility(View.VISIBLE);
        }
    }
    private void userpassword(String password) {

        String mail=Email.getText().toString();
        progressDialog.dismiss();

        db.collection("StudentData").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("user")
                .document(mail).update("pass",password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Password Updated..",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changepassword(String password) {
        firebaseuser=auth.getCurrentUser();
        progressDialog.setMessage("your command is in process....");
        progressDialog.show();
        if(firebaseuser != null)
        {
            firebaseuser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(getApplicationContext(),activity_login.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity_studentpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}