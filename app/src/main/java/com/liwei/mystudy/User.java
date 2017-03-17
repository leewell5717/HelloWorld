package com.liwei.mystudy;

import java.util.List;

public class User {
    private String name;
    private int age;
    private List<Course> course;

    public User(){

    }

    public User(String name,int age,List<Course> course){
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }
}