package kz.baltabayev.dao;

import jakarta.persistence.EntityManager;
import kz.baltabayev.entity.Company;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}
