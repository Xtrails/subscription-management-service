package ru.ani.subscription.management.service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;

/**
 * Spring Data JPA repository for the SubscriptionAccessDao entity.
 *
 * When extending this class, extend SubscriptionAccessRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SubscriptionAccessRepository
    extends SubscriptionAccessRepositoryWithBagRelationships, JpaRepository<SubscriptionAccessDao, UUID> {
    default Optional<SubscriptionAccessDao> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SubscriptionAccessDao> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SubscriptionAccessDao> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
