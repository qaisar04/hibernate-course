package kz.baltabayev.dao;

import jakarta.persistence.EntityManager;
import kz.baltabayev.entity.User;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
