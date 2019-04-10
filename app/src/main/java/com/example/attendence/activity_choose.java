package com.example.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class activity_choose extends AppCompatActivity {
    Button btLogIn, btSignUp, btExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        btLogIn = findViewById(R.id.btLogIn);
        btSignUp = findViewById(R.id.btSignUp);
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), activity_login.class);
                startActivity(intent);
            }
        });
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Signup" ,Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(getApplicationContext(), activity_signup.class);
              startActivity(intent);
            }
        });
    }
}
