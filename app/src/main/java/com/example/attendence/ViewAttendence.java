package com.example.attendence;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewAttendence extends AppCompatActivity implements View.OnClickListener {


    Subjects subject;
    String subNodeId;
    String tuid="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_att);
        subject = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
        tuid = getIntent().getStringExtra("tuid");
        subNodeId = getIntent().getStringExtra("id");
        getData();

    }

    private void getData() {
        if (tuid.isEmpty())
        {
           String uid=FirebaseAuth.getInstance().getUid();
            FirebaseFirestore.getInstance().collection("Data").document(uid)
                    .collection("Attendence").document(subNodeId).collection("attendence")
                    .orderBy("date", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Attendence> attendencesList = new ArrayList<>();
                                ArrayList<Student> students = new ArrayList<>();
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    Attendence attendence = snapshot.toObject(Attendence.class);
                                    attendencesList.add(attendence);
                                }
                                addHeaders(attendencesList);
                                addData(attendencesList);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewAttendence.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            FirebaseFirestore.getInstance().collection("Data").document(tuid)
                    .collection("Attendence").document(subNodeId).collection("attendence")
                    .orderBy("date", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Attendence> attendencesList = new ArrayList<>();
                                ArrayList<Student> students = new ArrayList<>();
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    Attendence attendence = snapshot.toObject(Attendence.class);
                                    attendencesList.add(attendence);
                                }
                                addHeaders(attendencesList);
                                addData(attendencesList);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewAttendence.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     *
     * @param attendencesList
     */
    public void addHeaders(ArrayList<Attendence> attendencesList) {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Name", Color.BLACK, Typeface.BOLD,ContextCompat.getColor(this, R.color.gray)));
        tr.addView(getTextView(0, "Roll No.", Color.BLACK, Typeface.BOLD, ContextCompat.getColor(this, R.color.gray)));
        for (Attendence attendence : attendencesList) {
            tr.addView(getTextView(0, attendence.date, Color.BLACK, Typeface.BOLD, ContextCompat.getColor(this, R.color.gray)));

        }
        tr.addView(getTextView(0, "Total Present", Color.BLACK, Typeface.BOLD, ContextCompat.getColor(this, R.color.gray)));
        tr.addView(getTextView(0, "Total Absent", Color.BLACK, Typeface.BOLD, ContextCompat.getColor(this, R.color.gray)));
        tr.addView(getTextView(0, "Percentage", Color.BLACK, Typeface.BOLD, ContextCompat.getColor(this, R.color.gray)));


        tl.addView(tr, getTblLayoutParams());
    }

    public void addData(ArrayList<Attendence> attendencesList) {
        int attsize = attendencesList.size();
        TableLayout tl = findViewById(R.id.table);
        try {
            for (int i = 0; i < attendencesList.get(0).getList().size(); i++) {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(getLayoutParams());
                tr.addView(getTextView(i, attendencesList.get(0).getList().get(i).name, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                tr.addView(getTextView(i, String.valueOf(attendencesList.get(0).getList().get(i).getRoll_no()), Color.BLACK, Typeface.NORMAL, Color.WHITE));

                int totalP = 0;
                int totalA = 0;
                float total = 0.0f;
                for (int a = 0; a < attsize; a++) {
                    Boolean status = attendencesList.get(a).getList().get(i).status;
                    if (status) {
                        tr.addView(getTextView(i, "P", Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.Green)));
                        totalP = totalP + 1;
                        total = totalP * 100 / (totalA + totalP);

                    } else {
                        totalA = totalA + 1;
                        tr.addView(getTextView(i, "A", Color.WHITE, Typeface.NORMAL, Color.RED));
                    }

                }
                tr.addView(getTextView(i, String.valueOf(totalP), Color.BLACK, Typeface.NORMAL, Color.WHITE));
                tr.addView(getTextView(i, String.valueOf(totalA), Color.BLACK, Typeface.NORMAL, Color.WHITE));
                tr.addView(getTextView(i, String.valueOf(total + "%"), Color.BLACK, Typeface.NORMAL, Color.WHITE));
                tl.addView(tr, getTblLayoutParams());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "No Attendance found ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(this, "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}