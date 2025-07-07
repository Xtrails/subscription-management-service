package ru.ani.subscription.management.service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.ReferralProgram;

/**
 * Spring Data JPA repository for the ReferralProgram entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferralProgramRepository extends JpaRepository<ReferralProgram, Long> {}
