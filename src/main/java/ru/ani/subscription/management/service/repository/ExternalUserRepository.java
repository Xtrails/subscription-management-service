package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.ExternalUserDao;

/**
 * Spring Data JPA repository for the ExternalUserDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUserDao, UUID> {}
