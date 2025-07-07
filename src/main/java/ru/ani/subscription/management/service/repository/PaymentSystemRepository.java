package ru.ani.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.PaymentSystem;

/**
 * Spring Data JPA repository for the PaymentSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {}
