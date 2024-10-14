package ru.aniscan.subscription.management.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.aniscan.subscription.management.service.domain.PaymentSystem;

/**
 * Spring Data JPA repository for the PaymentSystem entity.
 *
 * When extending this class, extend PaymentSystemRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PaymentSystemRepository extends PaymentSystemRepositoryWithBagRelationships, JpaRepository<PaymentSystem, Long> {
    default Optional<PaymentSystem> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PaymentSystem> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PaymentSystem> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
