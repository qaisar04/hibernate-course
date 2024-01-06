package kz.baltabayev;

import jakarta.transaction.Transactional;
import kz.baltabayev.dao.CompanyRepository;
import kz.baltabayev.dao.PaymentRepository;
import kz.baltabayev.dao.UserRepository;
import kz.baltabayev.dto.UserCreateDto;
import kz.baltabayev.entity.Payment;
import kz.baltabayev.entity.PersonalInfo;
import kz.baltabayev.entity.Role;
import kz.baltabayev.entity.User;
import kz.baltabayev.interceptor.GlobalInterceptor;
import kz.baltabayev.interceptor.TransactionInterceptor;
import kz.baltabayev.mapper.CompanyReadMapper;
import kz.baltabayev.mapper.UserCreateMapper;
import kz.baltabayev.mapper.UserReadMapper;
import kz.baltabayev.service.UserService;
import kz.baltabayev.util.HibernateUtil;
import kz.baltabayev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class HibernateRunner {
    //todo: implement spring, mapstruct, properties format -> yml, pom.xml(byte-buddy dependency is incorrect)
    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            session.beginTransaction();

            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            var userRepository = new UserRepository(session);
            var paymentRepository = new PaymentRepository(session);
//            var userService = new UserService(userRepository, userReadMapper, userCreateMapper);
            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            var userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            userService.findById(1L).ifPresent(System.out::println);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Stepanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    "liza2@gmail.com",
                    null,
                    Role.USER,
                    1
            );
            userService.create(userCreateDto);

//            session.getTransaction().commit();
        }
    }


}
