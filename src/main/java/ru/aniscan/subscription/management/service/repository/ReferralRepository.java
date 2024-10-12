package ru.aniscan.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.aniscan.subscription.management.service.domain.Referral;

/**
 * Spring Data JPA repository for the Referral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {}
