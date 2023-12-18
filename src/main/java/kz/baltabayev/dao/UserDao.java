package kz.baltabayev.dao;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import kz.baltabayev.dto.CompanyDto;
import kz.baltabayev.entity.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import org.hibernate.query.criteria.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> user = criteria.from(User.class);

//        criteria.select(user).where(
//                cb.equal(user.get("personalInfo").get("firstname"), firstName));

        criteria.select(user).where(
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName)); // hibernate-jpamodelgen

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class); // то что return в методе
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate)));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        JpaRoot<Company> company = criteria.from(Company.class);
        JpaMapJoin<Company, String, User> users = company.join(Company_.users);

        criteria.select(users).where(
                cb.equal(company.get(Company_.name), companyName)
        );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);

        JpaRoot<Payment> payment = criteria.from(Payment.class);
        JpaJoin<Payment, User> user = payment.join(Payment_.receiver);
        JpaJoin<User, Company> company = user.join(User_.company);

        criteria.select(payment).where(
                        cb.equal(company.get(Company_.name), companyName)
                )
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount))
                );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        JpaRoot<Payment> payment = criteria.from(Payment.class);
        JpaJoin<Payment, User> user = payment.join(Payment_.receiver);

//        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName)
//        );

        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
                predicates.toArray(Predicate[]::new)
        );

        return session.createQuery(criteria)
                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);

        JpaRoot<Company> company = criteria.from(Company.class);
        JpaMapJoin<Company, String, User> user = company.join(Company_.users, JoinType.INNER);
        JpaListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.select(
                        cb.construct(CompanyDto.class,
                                company.get(Company_.name),
                                cb.avg(payment.get(Payment_.amount)))
                )
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria)
                .list();

    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);

        JpaRoot<User> user = criteria.from(User.class);
        JpaListJoin<User, Payment> payment = user.join(User_.payments);

        JpaSubQuery<Double> subquery = criteria.subquery(Double.class);
        JpaRoot<Payment> paymentSubquery = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(
                        user,
                        cb.avg(payment.get(Payment_.amount))
                )
        ).groupBy(
                user.get((User_.id))
        ).having(
                cb.gt(
                        cb.avg(payment.get(Payment_.amount)), subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
                        )
        ).orderBy(
                cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname))
        );


        return session.createQuery(criteria)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
