package com.example.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.example.model.Student;

public class StudentDAOImpl implements StudentDAO {

    private final SessionFactory sessionFactory;

    public StudentDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(Student s) {
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.save(s);
    }

    @Override
    public Student findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Student.class, id);
    }

    @Override
    public List<Student> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Student> q = session.createQuery("from Student", Student.class);
        return q.list();
    }

    @Override
    public void update(Student s) {
        Session session = sessionFactory.getCurrentSession();
        session.update(s);
    }

    @Override
    public void delete(Student s) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(s);
    }
}
