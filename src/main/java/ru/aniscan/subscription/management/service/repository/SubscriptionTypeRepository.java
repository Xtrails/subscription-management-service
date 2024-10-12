package ru.aniscan.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.aniscan.subscription.management.service.domain.SubscriptionType;

/**
 * Spring Data JPA repository for the SubscriptionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, Long> {}
