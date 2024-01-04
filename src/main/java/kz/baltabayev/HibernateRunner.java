package kz.baltabayev;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.util.HibernateUtil;
import kz.baltabayev.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);
            session.setDefaultReadOnly(true);
            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10); // данный update не будет действовать так как стоит read only мод

            List<Payment> payments = session.createQuery("select p from Payment p", Payment.class)
                    .setReadOnly(true)
                    .list();

            session.getTransaction().commit();
        }
    }
}
