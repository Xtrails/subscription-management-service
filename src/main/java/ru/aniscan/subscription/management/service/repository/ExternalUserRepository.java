package ru.aniscan.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.aniscan.subscription.management.service.domain.ExternalUser;

/**
 * Spring Data JPA repository for the ExternalUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {}
