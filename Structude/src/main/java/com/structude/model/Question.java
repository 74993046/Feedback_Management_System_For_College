package com.structude.model;

public class Question {
    private int qno;
    private String title;

    public Question() {}

    public Question(int qno, String title) {
        this.qno = qno;
        this.title = title;
    }

    public int getQno() { return qno; }
    public void setQno(int qno) { this.qno = qno; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
