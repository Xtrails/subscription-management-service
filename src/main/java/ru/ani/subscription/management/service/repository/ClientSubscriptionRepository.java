package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.ClientSubscriptionDao;

/**
 * Spring Data JPA repository for the ClientSubscriptionDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientSubscriptionRepository extends JpaRepository<ClientSubscriptionDao, UUID> {}
