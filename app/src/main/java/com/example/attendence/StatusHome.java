package com.example.attendence;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class StatusHome extends RecyclerView.Adapter<StatusHome.MyHolder> {
    ArrayList<UserDataR> sub_list=new ArrayList<>();
    ArrayList<String> ids=new ArrayList<>();
    Context context;
    public StatusHome(studenthome context) {
        this.context=context;
    }

    @NonNull
    @Override
    public StatusHome.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statuspagedesign,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatusHome.MyHolder myHolder, final int i) {
        final UserDataR user=sub_list.get(i);
        myHolder.txtName.setText(user.getFname()+" "+user.getLname());
        myHolder.txtdepartment.setText("Department :"+user.getDepartment());
        myHolder.txtphn.setText(user.getPhone());
        myHolder.txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ViewAttendence.class);
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });


      /*  myHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(teacherstatus.imagestatus), "image/*");
                context.startActivity(intent);
            }
        });*/


     //myHolder.photo.setImageResource(teacherstatus.imagestatus);


       /* if (i % 2 == 1) {
            myHolder.cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFA500"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }*/
    }

    @Override
    public int getItemCount() {
        return sub_list.size();
    }

    public void addData(UserDataR teacherstatus, String id) {
        this.sub_list.add(teacherstatus);
        this.ids.add(id);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtName,
                txtdepartment,txtphn,txtview;
        CardView cardView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.tv_Name);
            txtdepartment=itemView.findViewById(R.id.tv_Dept);
            txtphn=itemView.findViewById(R.id.tv_Phn);
            txtview=itemView.findViewById(R.id.view_attendance);
            }
    }
}
