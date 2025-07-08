package ru.ani.subscription.management.service.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SubscriptionAccessRepositoryWithBagRelationshipsImpl implements SubscriptionAccessRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUBSCRIPTIONACCESSES_PARAMETER = "subscriptionAccesses";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SubscriptionAccessDao> fetchBagRelationships(Optional<SubscriptionAccessDao> subscriptionAccess) {
        return subscriptionAccess.map(this::fetchSubscriptionDetails);
    }

    @Override
    public Page<SubscriptionAccessDao> fetchBagRelationships(Page<SubscriptionAccessDao> subscriptionAccesses) {
        return new PageImpl<>(
            fetchBagRelationships(subscriptionAccesses.getContent()),
            subscriptionAccesses.getPageable(),
            subscriptionAccesses.getTotalElements()
        );
    }

    @Override
    public List<SubscriptionAccessDao> fetchBagRelationships(List<SubscriptionAccessDao> subscriptionAccesses) {
        return Optional.of(subscriptionAccesses).map(this::fetchSubscriptionDetails).orElse(Collections.emptyList());
    }

    SubscriptionAccessDao fetchSubscriptionDetails(SubscriptionAccessDao result) {
        return entityManager
            .createQuery(
                "select subscriptionAccess from SubscriptionAccessDao subscriptionAccess left join fetch subscriptionAccess.subscriptionDetails where subscriptionAccess.id = :id",
                SubscriptionAccessDao.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SubscriptionAccessDao> fetchSubscriptionDetails(List<SubscriptionAccessDao> subscriptionAccesses) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, subscriptionAccesses.size()).forEach(index -> order.put(subscriptionAccesses.get(index).getId(), index));
        List<SubscriptionAccessDao> result = entityManager
            .createQuery(
                "select subscriptionAccess from SubscriptionAccessDao subscriptionAccess left join fetch subscriptionAccess.subscriptionDetails where subscriptionAccess in :subscriptionAccesses",
                SubscriptionAccessDao.class
            )
            .setParameter(SUBSCRIPTIONACCESSES_PARAMETER, subscriptionAccesses)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
