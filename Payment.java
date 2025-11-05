package com.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private double amount;

    private LocalDateTime date;

    public Payment() {}

    public Payment(Student student, double amount) {
        this.student = student;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    // getters & setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    @Override
    public String toString() {
        return "Payment{id=" + paymentId + ", student=" + (student!=null?student.getStudentId():"null") +
                ", amount=" + amount + ", date=" + date + "}";
    }
}
