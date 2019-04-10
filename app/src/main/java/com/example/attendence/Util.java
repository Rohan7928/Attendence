package com.example.attendence;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Util {

    public static void toast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static String getDate(Long time_stamp_server) {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        return formatter.format(time_stamp_server);
    }
    }
