package com.example.attendence;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public String isLogin="islogin";
    public Pref(Context context)
    {
        preferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor=preferences.edit();
    }

}
