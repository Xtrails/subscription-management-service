package ru.ani.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.SourceApplication;

/**
 * Spring Data JPA repository for the SourceApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceApplicationRepository extends JpaRepository<SourceApplication, Long> {}
