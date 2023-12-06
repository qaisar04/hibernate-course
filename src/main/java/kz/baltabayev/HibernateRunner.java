package kz.baltabayev;

import kz.baltabayev.entity.Birthday;
import kz.baltabayev.entity.Role;
import kz.baltabayev.entity.User;
import kz.baltabayev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = User.builder()
                    .username("qaisar9")
                    .firstname("qaisar")
                    .birthDate(new Birthday(LocalDate.of(2000, 11, 29)))
                    .role(Role.USER)
                    .info("""
                            {
                                "name":"Qaisar",
                                "id":25
                            }
                            """)
                    .build();

            session.update(user);
            log.info("UPDATE METHOD!");

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
