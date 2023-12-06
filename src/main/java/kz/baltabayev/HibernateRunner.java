package kz.baltabayev;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import kz.baltabayev.converter.BirthdayConverter;
import kz.baltabayev.entity.Birthday;
import kz.baltabayev.entity.Role;
import kz.baltabayev.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException {
//        --- [SessionFactory] ---
//        BlockingQueue<Connection> connectionPool = null;
//        Connection connection = connectionPool.take();

//        --- [Session] ---
//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class); // либо в xml файле
//        configuration.addAttributeConverter(new BirthdayConverter(), true);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = User.builder()
                    .username("qaisar06")
                    .firstname("Qaisar")
                    .birthDate(new Birthday(LocalDate.of(2004, 11, 29)))
                    .role(Role.USER)
                    .info("""
                            {
                                "name":"Qaisar",
                                "id":25
                            }
                            """)
                    .build();
            session.save(user);

            session.getTransaction().commit();
        }
    }
}
