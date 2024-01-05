package kz.baltabayev;

import jakarta.transaction.Transactional;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.entity.User;
import kz.baltabayev.interceptor.GlobalInterceptor;
import kz.baltabayev.util.HibernateUtil;
import kz.baltabayev.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);

            User user = null;
            try(Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                user = session1.find(User.class, 1L); // запрос в базу данных
                User user1 = session1.find(User.class, 1L); // PersistenceContext

                session1.getTransaction().commit();
            }

            try(Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                User user2 = session2.find(User.class, 1L); // запрос в базу данных так как PersistenceContext разные для каждой сессии



                session2.getTransaction().commit();
            }
        }
    }
}
