package ru.ani.subscription.management.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;

public interface SubscriptionAccessRepositoryWithBagRelationships {
    Optional<SubscriptionAccessDao> fetchBagRelationships(Optional<SubscriptionAccessDao> subscriptionAccess);

    List<SubscriptionAccessDao> fetchBagRelationships(List<SubscriptionAccessDao> subscriptionAccesses);

    Page<SubscriptionAccessDao> fetchBagRelationships(Page<SubscriptionAccessDao> subscriptionAccesses);
}
