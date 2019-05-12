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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Change_teacherpassword extends AppCompatActivity {
    EditText oldpass,newpass;
    Button verify,back;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_teacherpassword);
        oldpass = findViewById(R.id.etOld);
        newpass = findViewById(R.id.etNew);
        back=findViewById(R.id.bt_homepass);
        verify = findViewById(R.id.bt_Verify);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second...");
        firebaseuser = auth.getCurrentUser();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),studenthome.class));
                finish();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user;
                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                String oldpassword=oldpass.getText().toString();
                final String newpassword=newpass.getText().toString();

                AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Change_teacherpassword.this, "Password change", Toast.LENGTH_SHORT).show();
                                        auth.signOut();
                                        startActivity(new Intent(getApplicationContext(),student_login.class));
                                        finish();
                                    }else {
                                        Toast.makeText(Change_teacherpassword.this, "Sorry", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(Change_teacherpassword.this, "Old password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

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

        });
    }
}