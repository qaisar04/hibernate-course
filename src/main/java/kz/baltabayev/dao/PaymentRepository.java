package kz.baltabayev.dao;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import kz.baltabayev.entity.Payment;
import org.hibernate.SessionFactory;

import java.util.List;

import static kz.baltabayev.entity.QPayment.payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }

    public List<Payment> findAllByReceiverId(Long receiverId) {
        return new JPAQuery<Payment>()
                .select(payment)
                .from(payment)
                .where(payment.receiver.id.eq(receiverId))
                .fetch();
    }


}
