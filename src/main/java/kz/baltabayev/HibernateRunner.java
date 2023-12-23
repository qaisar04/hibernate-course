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

//            User user = session.get(User.class, 1);
//            System.out.println(user.getPayments().size());
//            System.out.println(user.getCompany().getName());

            List<User> list = session.createQuery("select u from User u", User.class)
                    .list();
            list.forEach(user -> System.out.println(user.getPayments().size()));
            list.forEach(user -> System.out.println(user.getCompany().getName()));


            session.getTransaction().commit();
        }
    }
}
