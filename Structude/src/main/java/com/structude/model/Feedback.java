package com.structude.model;

public class Feedback {
    private String enrollment;
    private int sem;
    private int q1;
    private int q2;
    private int q3;
    private int code;

    public Feedback() {}

    public Feedback(String enrollment, int sem, int q1, int q2, int q3, int code) {
        this.enrollment = enrollment;
        this.sem = sem;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.code = code;
    }

    public String getEnrollment() { return enrollment; }
    public void setEnrollment(String enrollment) { this.enrollment = enrollment; }
    public int getSem() { return sem; }
    public void setSem(int sem) { this.sem = sem; }
    public int getQ1() { return q1; }
    public void setQ1(int q1) { this.q1 = q1; }
    public int getQ2() { return q2; }
    public void setQ2(int q2) { this.q2 = q2; }
    public int getQ3() { return q3; }
    public void setQ3(int q3) { this.q3 = q3; }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
}
