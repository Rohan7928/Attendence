package com.example.attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_choose extends AppCompatActivity {
    ImageView btLogIn;
    ImageView btLogIn2;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        btLogIn = findViewById(R.id.btLogIn);
        btLogIn2= findViewById(R.id.btLogIn2);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Welcome Teacher");
        progressDialog.setCancelable(false);
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(activity_choose.this,activity_login.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Login As Teacher",Toast.LENGTH_SHORT).show();
            }
        });
      btLogIn2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getApplicationContext(),"Login As student" ,Toast.LENGTH_SHORT).show();
              FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
              Intent intent = new Intent(activity_choose.this,student_login.class);
              startActivity(intent);
              /* if (user==null) {
                  progressDialog.dismiss();
                  Intent intent = new Intent(activity_choose.this,student_login.class);
                  startActivity(intent);
                  finish();
              }else
              {
                  progressDialog.dismiss();
                  Intent intent=new Intent(activity_choose.this,studenthome.class);
                  startActivity(intent);
                  finish();
              }*/

          }
      });
    }
}
