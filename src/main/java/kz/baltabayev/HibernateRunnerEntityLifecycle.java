package kz.baltabayev;

import kz.baltabayev.entity.Role;
import kz.baltabayev.entity.User;
import kz.baltabayev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class HibernateRunnerEntityLifecycle {
    public static void main(String[] args) {

//        --- [ EntityLifecycle ] ---
//        ---  [ refresh/merge ] ---


       /*
         Если сущность (например, объект или запись в базе данных) не связана с какой-либо сессией
         и ее существование не сохраняется между различными сеансами взаимодействия, то можно сказать,
         что ее состояние - TRANSIENT. (строка 27)
        */

        User user = User.builder()
                .username("qwerty123@spring.com")
                .role(Role.ADMIN)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try(Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                session1.saveOrUpdate(user);
                // Теперь user PERSISTENT относительно session1, но все еще TRANSIENT относительно session2

                session1.getTransaction().commit();
            }

            /*
             После завершения транзакции в session1, объект user больше не связан с этой сессией,
             и он становится DETACHED (отсоединенным).
            */

            try(Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                user.setRole(Role.USER);
                session2.refresh(user); // refresh используется для обновления состояния объекта из базы данных.

                System.out.println(user.getRole()); // ADMIN

                session2.merge(user); // Метод merge в Hibernate используется для сохранения или обновления состояния объекта в базе данных.

//                session2.delete(user); // состояние REMOVED

                session2.getTransaction().commit();
            }
        }
    }
}
