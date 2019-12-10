package com.dev_hieu.demo.model;

import androidx.annotation.NonNull;

public class Student {
    //Mã sinh viên
    int id;
    //Tên
    String firstName;
    //Họ
    String lastName;
    //Mã lớp
    String classID;

    public Student(int id, String firstName, String lastName, String classID) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classID = classID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public Student() {
    }


    @NonNull
    @Override
    public String toString() {
        String str = "Student [ id: " + id + "\n"
                + "first Name:    " + firstName + "\n"
                + "last Name:   " + lastName + "\n"
                + "classID: " + classID + "\n";
        return str;
    }
}
