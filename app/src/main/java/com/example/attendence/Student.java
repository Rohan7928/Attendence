package com.example.attendence;

import java.text.StringCharacterIterator;

class Student {
    public Student(String name, int c, boolean b, String current) {
        this.name = name;
        this.roll_no = c;
        this.status = b;
        this.current=current;
    }
public Student()
{}
    public Student(String name, int c) {
        this.name = name;
        this.roll_no = c;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(int roll_no) {
        this.roll_no = roll_no;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    String name;
    int roll_no;
    Boolean status;
    String current;
    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }



   }
