package com.example.attendence;

import com.google.firebase.auth.FirebaseAuth;

class Teacherstatus {
    String data, uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTemail() {
        return temail;
    }

    public void setTemail(String temail) {
        this.temail = temail;
    }

    String temail;

    public String getTimesep() {
        return timesep;
    }

    public void setTimesep(String timesep) {
        this.timesep = timesep;
    }

    String timesep;


    public Teacherstatus() {
        this.uid= FirebaseAuth.getInstance().getUid();
    }

    public String getImagestatus() {
        return imagestatus;
    }

    public void setImagestatus(String imagestatus) {
        this.imagestatus = imagestatus;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    String imagestatus;
    String imagename;


    public String getData() {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

}
