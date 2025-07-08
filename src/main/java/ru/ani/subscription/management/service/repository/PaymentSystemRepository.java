package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.PaymentSystemDao;

/**
 * Spring Data JPA repository for the PaymentSystemDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystemDao, UUID> {}
