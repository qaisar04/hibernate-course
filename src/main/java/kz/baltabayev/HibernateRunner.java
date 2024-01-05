package kz.baltabayev;

import jakarta.transaction.Transactional;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.interceptor.GlobalInterceptor;
import kz.baltabayev.util.HibernateUtil;
import kz.baltabayev.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory
                    .withOptions()
                    .interceptor(new GlobalInterceptor())
                    .openSession()) {
//            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();




            session.getTransaction().commit();
        }
    }
}
