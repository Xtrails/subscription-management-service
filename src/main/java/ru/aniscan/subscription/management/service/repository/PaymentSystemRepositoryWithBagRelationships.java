package ru.aniscan.subscription.management.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.aniscan.subscription.management.service.domain.PaymentSystem;

public interface PaymentSystemRepositoryWithBagRelationships {
    Optional<PaymentSystem> fetchBagRelationships(Optional<PaymentSystem> paymentSystem);

    List<PaymentSystem> fetchBagRelationships(List<PaymentSystem> paymentSystems);

    Page<PaymentSystem> fetchBagRelationships(Page<PaymentSystem> paymentSystems);
}
