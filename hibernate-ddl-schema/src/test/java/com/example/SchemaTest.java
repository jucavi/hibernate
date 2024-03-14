package com.example;

import com.example.model.Address;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class SchemaTest {
    @Test
    void inserData() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Address address = new Address("street1", "city1", "counrty1");

        session.beginTransaction();
        session.persist(address);
        session.getTransaction().commit();

        HibernateUtil.shutdown();
    }
}
