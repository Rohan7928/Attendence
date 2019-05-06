package com.example.attendence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.Myholder> {
    List<Student> list;
    Context context;
    HashMap<Integer,Boolean> hashMap;
    ImageView checkal,uncheckal;
    public AttendAdapter(Context context, List<Student> list, ImageView uncheckall, ImageView checkall) {
    this.context=context;
    this.list=list;
    hashMap=new HashMap<>();
    this.checkal=checkall;
    this.uncheckal=uncheckall;
    }

    @NonNull
    @Override
    public AttendAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.take_attend,viewGroup,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendAdapter.Myholder myholder, final int i) {
        final Student student = list.get(i);
        hashMap.put(student.roll_no, false);

        myholder.roll.setText(String.valueOf(student.roll_no));
        myholder.student.setText(String.valueOf(student.name));
        myholder.check.setChecked(student.status);

        checkal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=true;
                if (flag == true) {

                    hideallboxes();
                    Toast.makeText(context, "Marked All as Absent", Toast.LENGTH_SHORT).show();

                    flag = false;

                }

            }
        });
        uncheckal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (flag == false) {
                    showAllBoxes();
                    Toast.makeText(context, "Marked All as Present", Toast.LENGTH_SHORT).show();
                    flag = true;

                }

            }
        });
    }

    private void hideallboxes() {
        for (Student student : list) {
            student.setStatus(false);
        }
        notifyDataSetChanged();
    }

    private void showAllBoxes() {
        for (Student student : list) {
            student.setStatus(true);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView roll,student;
        CheckBox check;

        public Myholder(@NonNull View itemView) {
            super(itemView);
           roll=itemView.findViewById(R.id.roll);
           student=itemView.findViewById(R.id.student);
           check=itemView.findViewById(R.id.check);
           check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    list.get(getAdapterPosition()).setStatus(isChecked);
                }
            });
        }
    }
}
