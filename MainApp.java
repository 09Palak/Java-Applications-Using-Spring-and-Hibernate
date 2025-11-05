package com.example;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.config.AppConfig;
import com.example.model.Course;
import com.example.model.Student;
import com.example.service.FeeService;

public class MainApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        FeeService feeService = ctx.getBean(FeeService.class);

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            printMenu();
            int ch = Integer.parseInt(sc.nextLine());
            try {
                switch (ch) {
                    case 1:
                        System.out.print("Student name: ");
                        String name = sc.nextLine();
                        System.out.print("Course name: ");
                        String cname = sc.nextLine();
                        System.out.print("Course duration: ");
                        String dur = sc.nextLine();
                        System.out.print("Initial balance: ");
                        double bal = Double.parseDouble(sc.nextLine());

                        Course course = new Course(cname, dur);
                        Student s = new Student(name, course, bal);
                        Long id = feeService.createStudent(s);
                        System.out.println("Created student id: " + id);
                        break;

                    case 2:
                        System.out.print("Student ID to view: ");
                        Long viewId = Long.parseLong(sc.nextLine());
                        Student s2 = feeService.getStudent(viewId);
                        System.out.println(s2);
                        break;

                    case 3:
                        List<Student> all = feeService.getAllStudents();
                        all.forEach(System.out::println);
                        break;

                    case 4:
                        System.out.print("Student ID to update: ");
                        Long upId = Long.parseLong(sc.nextLine());
                        Student up = feeService.getStudent(upId);
                        if (up == null) {
                            System.out.println("not found");
                            break;
                        }
                        System.out.print("New name (blank to keep): ");
                        String newName = sc.nextLine();
                        if (!newName.isBlank()) up.setName(newName);
                        System.out.print("New balance (blank to keep): ");
                        String newBalStr = sc.nextLine();
                        if (!newBalStr.isBlank()) up.setBalance(Double.parseDouble(newBalStr));
                        feeService.updateStudent(up);
                        System.out.println("Updated.");
                        break;

                    case 5:
                        System.out.print("Student ID to delete: ");
                        Long delId = Long.parseLong(sc.nextLine());
                        feeService.deleteStudent(delId);
                        System.out.println("Deleted (if existed).");
                        break;

                    case 6:
                        System.out.print("Student ID: ");
                        Long pid = Long.parseLong(sc.nextLine());
                        System.out.print("Amount to pay: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        try {
                            feeService.payFee(pid, amt);
                            System.out.println("Payment successful.");
                        } catch (Exception e) {
                            System.out.println("Payment failed: " + e.getMessage());
                        }
                        break;

                    case 7:
                        System.out.print("Student ID: ");
                        Long rid = Long.parseLong(sc.nextLine());
                        System.out.print("Amount to refund: ");
                        double ramt = Double.parseDouble(sc.nextLine());
                        try {
                            feeService.refundFee(rid, ramt);
                            System.out.println("Refund successful.");
                        } catch (Exception e) {
                            System.out.println("Refund failed: " + e.getMessage());
                        }
                        break;

                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        sc.close();
        ctx.close();
        System.out.println("Application stopped.");
    }

    private static void printMenu() {
        System.out.println("\n--- Online Student Management ---");
        System.out.println("1. Add student");
        System.out.println("2. View student by ID");
        System.out.println("3. View all students");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("6. Pay fee");
        System.out.println("7. Refund fee");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }
}
