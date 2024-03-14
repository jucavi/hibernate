package com.example;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
API Transaction de Hibernate proporciona una maenera uniforme de manejar transacciones
independientemente del mecanismo utilizado (jdbc, jta ctm, jta btm)
 */
public class TransactionTest {

    //@Transactional
    @Test
    @DisplayName("Modo JDBC: la transacción ls gestiona la aplicación localmente")
    void jdbc_transaction() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Modo JDBC: llama a java.sql.Connection#setAutocommit(false) para iniciar una transacción
            session.beginTransaction();
            session.createMutationQuery("update Employee set dni = :dni where id = :id")
                    .setParameter("dni", "11111A")
                    .setParameter("id", 1L)
                    .executeUpdate();

            // Modo JDBC: llama a java.sql.Connection#commit para finalizar la transacción
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
                HibernateUtil.shutdown();
            }
        }
    }

    @Test
    @DisplayName("Modo JTA CMT: la transacción la gestiona el servidor de aplicaciones")
    void jdbc_jta_cmt() {
        var session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Modo JTA CMT: la transacción la inicia el servidor de aplicaciones
            // session.beginTransaction();
            session.createMutationQuery("update employee set name = :name where id = :id")
                    .setParameter("name", "Juan")
                    .setParameter("id", 1L)
                    .executeUpdate();

            // Modo JTA CMT: la transacción la finaliza el servidor de aplicaciones
            //session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
                HibernateUtil.shutdown();
            }
        }
    }

    @Test
    @DisplayName("Modo JTA BMT: la aplicación inicia la transacción e invoca a JTA")
    void jdbc_jta_bmt() {
        var session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Modo JTA BMT: esta llamada invoca begin en el UserTransaction y TransactionManager
            session.beginTransaction();
            session.createMutationQuery("update employee set name = :name where id = :id")
                    .setParameter("name", "Juan")
                    .setParameter("id", 1L)
                    .executeUpdate();

            // Modo JTA BMT: esta llamada invoca commit en el UserTransaction y TransactionManager
            //session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Modo JTA BMT: esta llamada invoca rollback en el UserTransaction y TransactionManager
            if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
                HibernateUtil.shutdown();
            }
        }
    }
}
