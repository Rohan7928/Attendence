package com.example.attendence;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class ViewStatusAdapter extends RecyclerView.Adapter<ViewStatusAdapter.Myholder> {
    ArrayList<Teacherstatus> sub_list=new ArrayList<>();
    ArrayList<String> ids=new ArrayList<>();
    Context context;
    public ViewStatusAdapter(activity_viewstatus activity_viewstatus) {
        this.context=activity_viewstatus;
    }

    @NonNull
    @Override
    public ViewStatusAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewstatuspage,viewGroup,false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewStatusAdapter.Myholder myholder, int i) {
        final Teacherstatus teacherstatus=sub_list.get(i);
        myholder.txtemail.setText(teacherstatus.temail);
        myholder.txtinformation.setText(teacherstatus.data);
        myholder.txttime.setText(String.valueOf(teacherstatus.timesep));
        if(teacherstatus.imagestatus !=null) {
            myholder.photo.setVisibility(View.VISIBLE);
            Picasso.get().load(teacherstatus.imagestatus).into(myholder.photo);
        }


        myholder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(teacherstatus.imagestatus), "image/*");
                context.startActivity(intent);
            }
        });


        myholder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public int getItemCount() {
        return sub_list.size();
    }

    public void addData(Teacherstatus teacherstatus, String id) {
        this.sub_list.add(teacherstatus);
        this.ids.add(id);
        notifyDataSetChanged();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView txtemail,txttime,txtinformation;
        ImageView photo, ivDelete;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            txtemail=itemView.findViewById(R.id.txt_email);
            txttime=itemView.findViewById(R.id.txt_time);
            txtinformation=itemView.findViewById(R.id.txt_info);
            photo=itemView.findViewById(R.id.Image_photo);
            ivDelete = itemView.findViewById(R.id.ic_delete);
        }
    }
}
