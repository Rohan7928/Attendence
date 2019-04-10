package com.example.attendence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AddSubject extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<Addition> movielist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
       recyclerView=findViewById(R.id.Recycle_View);
       recyclerView.setHasFixedSize(true);
        movielist=new ArrayList<Addition>();
        Addition addition=new Addition("Welcome","Hello","2011");
        movielist.add(addition);

        addition=new Addition("FAst","Feurious","2012");
        movielist.add(addition);
       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AddSubject.this,LinearLayoutManager.VERTICAL,false);
       recyclerView.setLayoutManager(layoutManager);
        MyAdapter adapter=new MyAdapter(movielist);
        recyclerView.setAdapter(adapter);


    }
}
