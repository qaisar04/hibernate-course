package kz.baltabayev;

import kz.baltabayev.entity.*;
import kz.baltabayev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {

//        --- [SessionFactory] ---
//        BlockingQueue<Connection> connectionPool = null;
//        Connection connection = connectionPool.take();

//        --- [Session] ---
//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");

        User user = User.builder()
                .username("qaisar04")
                .personalInfo(PersonalInfo.builder()
                        .firstname("qaisar")
                        .lastname("java")
                        .birthDate(new Birthday(LocalDate.of(2004, 11, 29)))
                        .build())
                .role(Role.USER)
                .info("""
                            {
                                "name":"Qaisar",
                                "id":25
                            }
                            """)
                .build();

        Company company1 = Company.builder()
                .name("Google")
                .build();

        User user1 = User.builder()
                .username("spring04")
                .personalInfo(PersonalInfo.builder()
                        .firstname("qaisar")
                        .lastname("java")
                        .birthDate(new Birthday(LocalDate.of(2004, 11, 29)))
                        .build())
                .role(Role.USER)
                .company(company1)
                .info("""
                            {
                                "location":"Pavlodar",
                                "code":14
                            }
                            """)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();


//            session.save(company1);
//            session.save(user1);

            User user2 = session.get(User.class, 1L);
            System.out.println();

            log.info("RUN METHOD!");

            try(Session session_2 = sessionFactory.openSession()) {
                PersonalInfo key = PersonalInfo.builder()
                        .firstname("qaisar")
                         .lastname("java")
                        .birthDate(new Birthday(LocalDate.of(2004, 11, 29)))
                        .build();

                User retrievedUser = session_2.get(User.class, key);
            }

//          --- [Cache] ---
//          User user_1 = session.get(User.class, 1); // запрос в БД
//          User user_2 = session.get(User.class, 1); // получение обьекта из кэша
//          session.evict(user_1); // удаление обьекта из кэша
//          User user_3 = session.get(User.class, 1); // запрос в БД
//          session1.clear(); // полная чистка кэша

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Exception occurred", e);
        }
    }
}
