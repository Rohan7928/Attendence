package com.example.attendence;

import android.webkit.SafeBrowsingResponse;

import java.util.List;

class Subjects {

    String semester;
    String sub_name;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public Subjects()
    {

    }

    String sub_id;
    String sub_dept;
    String sub_division;
    String type;
    int rollfrom, rollto;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    String current;


       List<Student> students;


    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }


    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_dept() {
        return sub_dept;
    }

    public void setSub_dept(String sub_dept) {
        this.sub_dept = sub_dept;
    }

    public String getSub_division() {
        return sub_division;
    }

    public void setSub_division(String sub_division) {
        this.sub_division = sub_division;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRollfrom() {
        return rollfrom;
    }

    public void setRollfrom(int rollfrom) {
        this.rollfrom = rollfrom;
    }

    public int getRollto() {
        return rollto;
    }

    public void setRollto(int rollto) {
        this.rollto = rollto;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }


    public Subjects(String email,String semester, String subname, String subdept, String subdivision, String type, List<Student> student, int start, int end,String current) {
this.semester=semester;
this.sub_name=subname;
this.sub_dept=subdept;
this.sub_division=subdivision;
this.type=type;
this.students=student;
this.rollfrom=start;
this.rollto=end;
this.current=current;
this.email=email;

    }
}
