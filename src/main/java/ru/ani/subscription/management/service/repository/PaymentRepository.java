package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.PaymentDao;

/**
 * Spring Data JPA repository for the PaymentDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<PaymentDao, UUID> {}
