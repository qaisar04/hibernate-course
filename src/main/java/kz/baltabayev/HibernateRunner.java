package kz.baltabayev;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession();
            Session session1 = sessionFactory.openSession()) {
            session.beginTransaction();
            session1.beginTransaction();
//          session.doWork(connection -> System.out.println(connection.getTransactionIsolation())); // 2

            Payment payment = session.find(Payment.class, 1L, LockModeType.OPTIMISTIC);
            payment.setAmount(payment.getAmount() + 10);

            Payment theSamePayment = session1.find(Payment.class, 1L, LockModeType.OPTIMISTIC);
            theSamePayment.setAmount(theSamePayment.getAmount() + 20);


            session.getTransaction().commit();
            session1.getTransaction().commit(); // OptimisticLockException
        }
    }
}
