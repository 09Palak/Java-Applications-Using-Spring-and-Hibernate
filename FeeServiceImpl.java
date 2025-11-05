package com.example.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.StudentDAO;
import com.example.model.Payment;
import com.example.model.Student;

public class FeeServiceImpl implements FeeService {

    private final StudentDAO studentDAO;
    private final SessionFactory sessionFactory;

    // constructor injection via Spring bean definition in AppConfig
    public FeeServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
        // we can fetch sessionFactory indirectly from DAO implementation (not ideal but OK here)
        if (studentDAO instanceof com.example.dao.StudentDAOImpl) {
            this.sessionFactory = ((com.example.dao.StudentDAOImpl) studentDAO).getSessionFactory();
        } else {
            this.sessionFactory = null;
        }
    }

    @Override
    @Transactional
    public Long createStudent(Student s) {
        // open session via DAO (expects current session is bound)
        return studentDAO.save(s);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudent(Long id) {
        return studentDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    @Transactional
    public void updateStudent(Student s) {
        studentDAO.update(s);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student s = studentDAO.findById(id);
        if (s != null) {
            studentDAO.delete(s);
        }
    }

    // Payment: reduce the student's balance by amount and create Payment row.
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payFee(Long studentId, double amount) {
        Session session = sessionFactory.getCurrentSession();
        Student s = studentDAO.findById(studentId);
        if (s == null) throw new RuntimeException("Student not found: " + studentId);

        // Simple check; business rule example
        if (amount <= 0) throw new RuntimeException("Amount must be > 0");

        double newBalance = s.getBalance() - amount;
        if (newBalance < 0) {
            // For example, if we disallow negative balance, we still allow but can throw to trigger rollback.
            throw new RuntimeException("Payment exceeds balance. Operation aborted.");
        }
        s.setBalance(newBalance);
        studentDAO.update(s);

        Payment p = new Payment(s, amount);
        session.save(p);

        // If some error occurs here, transaction will roll back
    }

    // Refund: increase student balance and create negative Payment row or refund entry
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundFee(Long studentId, double amount) {
        Session session = sessionFactory.getCurrentSession();
        Student s = studentDAO.findById(studentId);
        if (s == null) throw new RuntimeException("Student not found: " + studentId);
        if (amount <= 0) throw new RuntimeException("Amount must be > 0");

        s.setBalance(s.getBalance() + amount);
        studentDAO.update(s);

        Payment p = new Payment(s, -amount); // negative amount marks refund
        session.save(p);

        // any exception will roll the change back
    }
}
