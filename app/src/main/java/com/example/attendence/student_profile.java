package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class student_profile extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    CircularImageView profilepic;
    TextView user,phn,e_mail,department,clas,father,mother,sem;
    Button back,edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        user=findViewById(R.id.profile_student_name);
        phn=findViewById(R.id.profile_student_phone);
        db=FirebaseFirestore.getInstance();
        profilepic=findViewById(R.id.studentprofile_image);
        auth=FirebaseAuth.getInstance();
        back=findViewById(R.id.btstudentprofile);
        e_mail=findViewById(R.id.profile_student_email);
        department=findViewById(R.id.profile_student_department);
        clas=findViewById(R.id.profile_student_class);
        father=findViewById(R.id.profile_student_father);
        mother=findViewById(R.id.profile_student_mother);
        sem=findViewById(R.id.profile_student_sem);
        edit=findViewById(R.id.profile_student_edit);
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                UserDataR userDataR=documentSnapshot.toObject(UserDataR.class);
                user.setText(userDataR.getFname() +" "+userDataR.getLname());
                e_mail.setText(userDataR.getEmail());
                phn.setText(userDataR.getPhone());
                department.setText(userDataR.getDepartment());
                clas.setText(userDataR.getClas());
                sem.setText(userDataR.getSem());
                father.setText(userDataR.getFather());
                mother.setText(userDataR.getMother());
                Picasso.get().load(userDataR.getProfileurl()).into(profilepic);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),studenthome.class));
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),edit_student.class));
                finish();
            }
        });

    }
}
