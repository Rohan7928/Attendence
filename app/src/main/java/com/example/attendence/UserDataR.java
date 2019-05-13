package com.example.attendence;

import com.google.firebase.auth.FirebaseAuth;

class UserDataR {

public UserDataR(){

}
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String lname;
    private String email;
    private String phone;
    private String father;
    private String fname;
    private String type;
    private String mother;
    private String address;
    private String sem;
    private String department;
    private String clas;
    private String uid;

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    private  String profileurl;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }



    public UserDataR(String fname, String lname, String email, String pass, String phone, String father, String mother, String address, String sem, String department, String clas,String type,String uri) {
    this.fname=fname;
    this.lname=lname;
    this.email=email;
    this.phone=phone;
    this.father=father;
    this.mother=mother;
    this.address=address;
    this.sem=sem;
    this.department=department;
    this.clas=clas;
    this.type=type;
    this.uid= FirebaseAuth.getInstance().getUid();
    this.profileurl=uri;

    }
    public UserDataR(String fname,String lname,String email,String phone,String type,String department,String uri){
        this.fname=fname;
        this.lname=lname;
        this.email=email;
        this.phone=phone;
        this.department=department;
        this.type=type;
        this.uid= FirebaseAuth.getInstance().getUid();
        this.profileurl=uri;
    }
}
