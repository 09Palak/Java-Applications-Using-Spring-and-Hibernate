package com.example.service;

import com.example.model.Student;

import java.util.List;

public interface FeeService {
    Long createStudent(Student s);
    Student getStudent(Long id);
    List<Student> getAllStudents();
    void updateStudent(Student s);
    void deleteStudent(Long id);

    // transactional operations
    void payFee(Long studentId, double amount);
    void refundFee(Long studentId, double amount);
}
