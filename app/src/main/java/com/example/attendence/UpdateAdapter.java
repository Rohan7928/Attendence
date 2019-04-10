package com.example.attendence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.Myholder> {
    Context context;
    List<Student> list;
    public UpdateAdapter(Context context, List<Student> students) {
        this.context=context;
        this.list=students;
    }

    @NonNull
    @Override
    public UpdateAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.update_student,viewGroup,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpdateAdapter.Myholder myholder, final int i) {
    Student student=list.get(i);
    myholder.roll_no.setText(String.valueOf(student.roll_no));
    myholder.student_name.setText(String.valueOf(student.name));
    myholder.student_name.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
          list.get(i).setName(myholder.student_name.getText().toString());
            Toast.makeText(context,myholder.student_name.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
       EditText roll_no,student_name;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            roll_no=itemView.findViewById(R.id.roll_student);
            student_name=itemView.findViewById(R.id.student_name);
        }
    }
}
