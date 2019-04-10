package com.example.attendence;

class UserDataR {
    private String fname;

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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
    private String pass;
    private String phone;
    private String father;
    private String mother;
    private String address;

    public UserDataR(String fname, String lname, String email, String pass, String phone, String father, String mother, String address) {
    this.fname=fname;
    this.lname=lname;
    this.email=email;
    this.pass=pass;
    this.phone=phone;
    this.father=father;
    this.mother=mother;
    this.address=address;
    }
    public UserDataR(){

    }
}
