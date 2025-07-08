package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.SourceApplicationDao;

/**
 * Spring Data JPA repository for the SourceApplicationDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceApplicationRepository extends JpaRepository<SourceApplicationDao, UUID> {}
