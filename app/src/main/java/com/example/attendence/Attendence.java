package com.example.attendence;

import java.util.List;

class Attendence {

    public String sub_name;
    List<Student> list;
    public String date;
    public String sem;

    public List<Student>
    getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }


    public String sub_id;

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }



    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }


}
