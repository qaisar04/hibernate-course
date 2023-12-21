package kz.baltabayev.util;

import kz.baltabayev.converter.BirthdayConverter;
import kz.baltabayev.entity.*;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass // private constructor and final class
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAttributeConverter(new BirthdayConverter(), true);
        return configuration;
    }
}
