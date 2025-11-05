package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_id")
    private Course course;

    // balance (like fee balance) - double for simplicity
    private double balance;

    public Student(){}

    public Student(String name, Course course, double balance) {
        this.name = name;
        this.course = course;
        this.balance = balance;
    }

    // getters and setters

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "Student{" +
               "id=" + studentId +
               ", name='" + name + '\'' +
               ", course=" + (course!=null ? course.getCourseName() : "none") +
               ", balance=" + balance +
               '}';
    }
}
