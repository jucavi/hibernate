package com.example;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.example.model.Employee;

import java.util.List;


public class Hibernate6Test {
    @Test
    void test1() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("select e from Employee e", Employee.class);
        List<Employee> result = query.getResultList();
        Assertions.assertTrue(result.size() > 0);
    }
}
