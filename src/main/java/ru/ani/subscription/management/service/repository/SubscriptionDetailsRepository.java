package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;

/**
 * Spring Data JPA repository for the SubscriptionDetailsDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionDetailsRepository extends JpaRepository<SubscriptionDetailsDao, UUID> {}
