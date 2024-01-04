package kz.baltabayev;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.util.HibernateUtil;
import kz.baltabayev.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession();
            Session session1 = sessionFactory.openSession()) {
            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();
            session1.beginTransaction();

            session.createQuery("select p from Payment p", Payment.class)
                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
                    .list();

            Payment payment = session.find(Payment.class, 1L, LockModeType.PESSIMISTIC_FORCE_INCREMENT); // where p1_0.id=? for no key update nowait (and update version in table)
            payment.setAmount(payment.getAmount() + 10);

            Payment theSamePayment = session1.find(Payment.class, 1L);
            theSamePayment.setAmount(theSamePayment.getAmount() + 15);

            session1.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}
