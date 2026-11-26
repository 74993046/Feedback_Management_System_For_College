package com.structude.model;

public class Subject {
    private int code;
    private String name;
    private String teacher;
    private int semester;

    public Subject() {}

    public Subject(int code, String name, String teacher, int semester) {
        this.code = code;
        this.name = name;
        this.teacher = teacher;
        this.semester = semester;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}
