package ru.aniscan.subscription.management.service.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.aniscan.subscription.management.service.domain.PaymentSystem;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PaymentSystemRepositoryWithBagRelationshipsImpl implements PaymentSystemRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PAYMENTSYSTEMS_PARAMETER = "paymentSystems";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PaymentSystem> fetchBagRelationships(Optional<PaymentSystem> paymentSystem) {
        return paymentSystem.map(this::fetchSourceApplications);
    }

    @Override
    public Page<PaymentSystem> fetchBagRelationships(Page<PaymentSystem> paymentSystems) {
        return new PageImpl<>(
            fetchBagRelationships(paymentSystems.getContent()),
            paymentSystems.getPageable(),
            paymentSystems.getTotalElements()
        );
    }

    @Override
    public List<PaymentSystem> fetchBagRelationships(List<PaymentSystem> paymentSystems) {
        return Optional.of(paymentSystems).map(this::fetchSourceApplications).orElse(Collections.emptyList());
    }

    PaymentSystem fetchSourceApplications(PaymentSystem result) {
        return entityManager
            .createQuery(
                "select paymentSystem from PaymentSystem paymentSystem left join fetch paymentSystem.sourceApplications where paymentSystem.id = :id",
                PaymentSystem.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PaymentSystem> fetchSourceApplications(List<PaymentSystem> paymentSystems) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, paymentSystems.size()).forEach(index -> order.put(paymentSystems.get(index).getId(), index));
        List<PaymentSystem> result = entityManager
            .createQuery(
                "select paymentSystem from PaymentSystem paymentSystem left join fetch paymentSystem.sourceApplications where paymentSystem in :paymentSystems",
                PaymentSystem.class
            )
            .setParameter(PAYMENTSYSTEMS_PARAMETER, paymentSystems)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
