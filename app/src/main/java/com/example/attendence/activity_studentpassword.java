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
import android.widget.ImageView;
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
    EditText pass, confirmpass, phn;
    ImageView verify;
    CardView card;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentpassword);
        Email = findViewById(R.id.etEmail);
        verify = findViewById(R.id.btn_Verify);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second baby...");
        firebaseuser = auth.getCurrentUser();
        card.setVisibility(View.GONE);
        phn.setVisibility(View.INVISIBLE);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* final String mail = Email.getText().toString();
                progressDialog.show();
                db.collection("StudentData").document(FirebaseAuth.getInstance().getUid()).collection("user")
                        .document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserDataR userDataR = documentSnapshot.toObject(UserDataR.class);
                        String e_mail = userDataR.getEmail();
                        String phone = userDataR.getPhone();
                        if (e_mail.equals(mail)) {
                            progressDialog.dismiss();
                            phn.setVisibility(View.VISIBLE);
                            enternumber(phone);
                        } else {
                            Toast.makeText(activity_studentpassword.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_studentpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
    }
}