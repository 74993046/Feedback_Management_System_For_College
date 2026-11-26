package com.structude.model;

public class Student {
    private String enrollment;
    private String name;
    private String mono;

    public Student() {}

    public Student(String enrollment, String name, String mono) {
        this.enrollment = enrollment;
        this.name = name;
        this.mono = mono;
    }

    public String getEnrollment() { return enrollment; }
    public void setEnrollment(String enrollment) { this.enrollment = enrollment; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMono() { return mono; }
    public void setMono(String mono) { this.mono = mono; }
}
