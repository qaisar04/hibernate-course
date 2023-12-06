package kz.baltabayev.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import kz.baltabayev.converter.BirthdayConverter;
import kz.baltabayev.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass // private constructor and final class
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class); // либо в xml файле
//        configuration.addAttributeConverter(new BirthdayConverter(), true);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
