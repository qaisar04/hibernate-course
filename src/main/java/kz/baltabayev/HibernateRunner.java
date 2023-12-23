package kz.baltabayev;

import kz.baltabayev.entity.User;
import kz.baltabayev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HibernateRunner {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<User> list = session.createQuery("select u from User u" +
                                                  " join fetch u.payments " +
                                                  " join fetch u.company" +
                                                  " where 1 = 1", User.class)
                    .list(); // в данном случае мы достали все в одном запросе.

            list.forEach(user -> System.out.println(user.getPayments().size()));
            list.forEach(user -> System.out.println(user.getCompany().getName()));


            session.getTransaction().commit();
        }
    }
}
