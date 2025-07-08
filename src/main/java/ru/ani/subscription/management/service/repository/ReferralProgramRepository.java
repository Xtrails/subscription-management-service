package ru.ani.subscription.management.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.ReferralProgramDao;

/**
 * Spring Data JPA repository for the ReferralProgramDao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferralProgramRepository extends JpaRepository<ReferralProgramDao, UUID> {}
