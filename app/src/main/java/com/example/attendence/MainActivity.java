package com.example.attendence;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private  static  int Timeout=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                Intent intent=new Intent(MainActivity.this,activity_choose.class);
                startActivity(intent);
                /*if (user==null)
                {
                  Intent intent=new Intent(MainActivity.this,activity_choose.class);
                  startActivity(intent);
                }else
                {
                 Intent intent=new Intent(MainActivity.this,activity_navigation.class);
                 startActivity(intent);
                }*/
                finish();
            }
        },Timeout);
    }
}
