package kz.baltabayev;

import kz.baltabayev.entity.Chat;
import kz.baltabayev.entity.Company;
import kz.baltabayev.entity.User;
import kz.baltabayev.entity.UserChat;
import kz.baltabayev.util.HibernateUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void localeInfo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
//            company.getLocales().add(LocaleInfo.of("ru", "Описание на русском"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));

            company.getUsers().forEach(System.out::println);


            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 1L);
            Chat chat = session.get(Chat.class, 1L);
            UserChat userChat = UserChat.builder()
                    .user(user)
                    .chat(chat)
                    .createdAt(Instant.now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);

            session.getTransaction().commit();
        }
    }


    @Test
    void checkOneToOne() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 4);
            System.out.println();

//            User user = User.builder()
//                    .username("qaisar_test 004")
//                    .build();
//            Profile userProfile = Profile.builder()
//                    .language("kz")
//                    .street("Lenina 18")
//                    .build();
//            userProfile.setUser(user);
//
//            session.save(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
            company.getUsers().removeIf(user -> user.getId().equals(2L));


            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 1);


            session.getTransaction().commit();
        }
        Set<User> users = company.getUsers();
//        System.out.println(users.size()); // LazyInitializationException
    }


    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 2);
        session.delete(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Amazon")
                .build();

        User user = User.builder()
                .username("Bob")
                .build();

//        user.setCompany(company);
//        company.getUsers().add(user);

        company.addUser(user);

        session.save(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);

        session.getTransaction().commit();
    }

    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.getString("firstname");
        resultSet.getString("lastname");
        resultSet.getString("username");

        Class<User> clazz = User.class;

        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("firstname"));
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .build();

        String sql = """
                insert
                into 
                %s
                (%s)
                values
                ($s) 
                """;

        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for (Field declaredField : declaredFields) {
            preparedStatement.setObject(1, declaredField.get(user));
        }


    }
}