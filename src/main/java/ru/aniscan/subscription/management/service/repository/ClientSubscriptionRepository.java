package ru.aniscan.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.aniscan.subscription.management.service.domain.ClientSubscription;

/**
 * Spring Data JPA repository for the ClientSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientSubscriptionRepository extends JpaRepository<ClientSubscription, Long> {}
