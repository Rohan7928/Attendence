package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_studentpassword extends AppCompatActivity {
    EditText oldpass,newpass;
     Button verify;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentpassword);
        oldpass = findViewById(R.id.etold);
        newpass = findViewById(R.id.etnew);
        verify = findViewById(R.id.btn_Verify);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait a second...");
        firebaseuser = auth.getCurrentUser();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(!isValidPassword(newpass.getText().toString().trim()))
                {
                    progressDialog.dismiss();
                    newpass.setError("Your password contain special symbol,One letter in capitals and numeric also");
                }
                else {
                    final FirebaseUser user;
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    String oldpassword = oldpass.getText().toString();
                    final String newpassword = newpass.getText().toString();

                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldpassword);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Password change", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                            startActivity(new Intent(getApplicationContext(), activity_choose.class));
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Sorry", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Old password incorrect", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
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
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}