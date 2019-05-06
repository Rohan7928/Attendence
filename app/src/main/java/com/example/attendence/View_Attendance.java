package com.example.attendence;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class View_Attendance extends AppCompatActivity implements View.OnClickListener {

    Subjects subject;
    String subNodeId;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__attendance);
        final Subjects attendence = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
        subject = new Gson().fromJson(getIntent().getStringExtra("list"), Subjects.class);
        db=FirebaseFirestore.getInstance();
        subNodeId = getIntent().getStringExtra("id");
        getData();

    }

    private void getData() {
        db.collection("Data").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Attendence").document(subNodeId).collection("attendence").orderBy("timestemp", Query.Direction.DESCENDING).get()
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
                Toast.makeText(View_Attendance.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
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
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Roll No.", Color.WHITE, Typeface.BOLD, Color.BLUE));
        for (Attendence attendence : attendencesList) {
            tr.addView(getTextView(0, attendence.date, Color.WHITE, Typeface.BOLD, Color.BLUE));

        }
        tr.addView(getTextView(0, "Total Present", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Total Absent", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Percentage", Color.WHITE, Typeface.BOLD, Color.BLUE));


        tl.addView(tr, getTblLayoutParams());
    }

    public void addData(ArrayList<Attendence> attendencesList) {
        int attsize = attendencesList.size();
        TableLayout tl = findViewById(R.id.table);

        for (int i = 0; i < attendencesList.get(0).getList().size(); i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i, attendencesList.get(0).getList().get(i).name, Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            tr.addView(getTextView(i, String.valueOf(attendencesList.get(0).getList().get(i).getRoll_no()), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));

            int totalP=0;
            int totalA=0;
            float total=0.0f;
            for (int a = 0; a < attsize; a++) {
                Boolean status = attendencesList.get(a).getList().get(i).status;
                if (status) {
                    tr.addView(getTextView(i, "P", Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));
                    totalP = totalP + 1;
                    total=totalP*100/(totalA+totalP);

                }
                else
                {
                    totalA = totalA + 1;
                    tr.addView(getTextView(i, "A", Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPink)));
                }

            }
            tr.addView(getTextView(i, String.valueOf(totalP), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            tr.addView(getTextView(i, String.valueOf(totalA), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            tr.addView(getTextView(i, String.valueOf(total+"%"), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            tl.addView(tr, getTblLayoutParams());
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
