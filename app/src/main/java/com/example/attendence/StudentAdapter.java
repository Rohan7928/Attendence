package com.example.attendence;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyHolder> {
   ArrayList<Subjects> sub_list=new ArrayList<>();
   ArrayList<String> ids=new ArrayList<>();
   Context context;
   FirebaseAuth auth=FirebaseAuth.getInstance();
   FirebaseFirestore db=FirebaseFirestore.getInstance();
    doAlert listner;
    public StudentAdapter(Context context,  doAlert listner) {
        this.context=context;
        this.listner=listner;
    }

    @NonNull
    @Override
    public StudentAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepagedesign,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.MyHolder myHolder, final int i) {
       final Subjects subjects=sub_list.get(i);
        myHolder.tvsub_name.setText(subjects.sub_name);
        myHolder.tvsub_type.setText(subjects.type);
        myHolder.tvsem.setText(subjects.semester);
        myHolder.tv_dept.setText(subjects.sub_dept);
        myHolder.tv_division.setText(subjects.sub_division);
        if (i % 2 == 1) {
            myHolder.cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFA500"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        myHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = new Gson().toJson(subjects);
                Intent intent = new Intent(context, Take_attendence.class);
                intent.putExtra("list", subject);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        myHolder.person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = new Gson().toJson(subjects);
                Intent intent = new Intent(context,Update.class);
                intent.putExtra("list", subject);
                intent.putExtra("id", ids.get(i));
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        myHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = new Gson().toJson(subjects);
               Intent intent = new Intent(context,activity_cardview.class);
                intent.putExtra("list", subject);
                intent.putExtra("id", ids.get(i));
              intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
            }
        });

        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onAlert(i,ids.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return sub_list.size();
    }

    public void addData(Subjects subjects, String id) {
        this.sub_list.add(subjects);
        this.ids.add(id);
        notifyDataSetChanged();
    }
    private void getdata() {

        String uid = auth.getUid();
        db.collection("Subjects").document(uid).collection("subjects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Subjects subjects = document.toObject(Subjects.class);
                                Log.e("subject ", subjects.sub_name);
                            }
                        } else {
                            Log.e("he", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void removeData(int i) {
        this.sub_list.remove(i);
        this.ids.remove(i);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvsub_name, tvsub_type, tvsem, tv_dept, tv_division;
        ImageView check, chart, edit, person, delete;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvsub_name = itemView.findViewById(R.id.tv_subname);
            tvsub_type = itemView.findViewById(R.id.tvsub_type);
            tv_division = itemView.findViewById(R.id.tv_division);
            tvsem = itemView.findViewById(R.id.tv_sem);
            tv_dept = itemView.findViewById(R.id.tv_dept);
            check = itemView.findViewById(R.id.icheck);
            chart = itemView.findViewById(R.id.ichart);
            edit = itemView.findViewById(R.id.iedit);
            person = itemView.findViewById(R.id.iperson);
            delete = itemView.findViewById(R.id.idelete);
            cardView = itemView.findViewById(R.id.card);
        }
    }
    interface  doAlert
    {
        void onAlert(int i, String s);
    }
}
