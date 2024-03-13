package com.example;

import com.example.model.Employee;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class HibernateTest {
    @Test
    void create() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Employee employee1 = new Employee("employee1", 23);
            Employee employee2 = new Employee("employee2", 25);

            // Transaction Start
            session.beginTransaction();
            session.persist(employee1);
            session.persist(employee2);
            session.getTransaction().commit();
        }
    }

    @Test
    void retrieve() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.find(Employee.class, 1L);
            System.out.println(employee);
        }
    }

    @Test
    void update() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.find(Employee.class, 1L);
            employee.setName("employee_updated");

            // Transaction Start
            session.beginTransaction();
            session.merge(employee);
            session.getTransaction().commit();

            Employee employeeDB = session.find(Employee.class, 1L);
            System.out.println(employeeDB);
        }
    }

    @Test
    void delete() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Transaction Start
            session.beginTransaction();
            session.remove(session.find(Employee.class, 1L));
            session.getTransaction().commit();
        }
    }
}
