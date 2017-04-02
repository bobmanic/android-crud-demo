package com.satyendra.bean;

/**
 * Created by Satyendra on 24/03/17.
 */
public class Student {

    private Integer rollno;
    private String name;

    public Student(int rollno, String name) {
        this.rollno = rollno;
        this.name = name;
    }

    public Integer getRollno() {
        return rollno;
    }

    public void setRollno(Integer rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollno=" + rollno +
                ", name='" + name + '\'' +
                '}';
    }
}
