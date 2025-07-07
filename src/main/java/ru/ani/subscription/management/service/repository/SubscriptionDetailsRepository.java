package ru.ani.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.SubscriptionDetails;

/**
 * Spring Data JPA repository for the SubscriptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionDetailsRepository extends JpaRepository<SubscriptionDetails, Long> {}
