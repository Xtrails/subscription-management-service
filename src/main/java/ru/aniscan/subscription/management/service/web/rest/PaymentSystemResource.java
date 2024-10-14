package ru.aniscan.subscription.management.service.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.aniscan.subscription.management.service.domain.PaymentSystem;
import ru.aniscan.subscription.management.service.repository.PaymentSystemRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.PaymentSystem}.
 */
@RestController
@RequestMapping("/api/payment-systems")
@Transactional
public class PaymentSystemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSystemResource.class);

    private static final String ENTITY_NAME = "paymentSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentSystemRepository paymentSystemRepository;

    public PaymentSystemResource(PaymentSystemRepository paymentSystemRepository) {
        this.paymentSystemRepository = paymentSystemRepository;
    }

    /**
     * {@code POST  /payment-systems} : Create a new paymentSystem.
     *
     * @param paymentSystem the paymentSystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentSystem, or with status {@code 400 (Bad Request)} if the paymentSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentSystem> createPaymentSystem(@Valid @RequestBody PaymentSystem paymentSystem) throws URISyntaxException {
        LOG.debug("REST request to save PaymentSystem : {}", paymentSystem);
        if (paymentSystem.getId() != null) {
            throw new BadRequestAlertException("A new paymentSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentSystem = paymentSystemRepository.save(paymentSystem);
        return ResponseEntity.created(new URI("/api/payment-systems/" + paymentSystem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentSystem.getId().toString()))
            .body(paymentSystem);
    }

    /**
     * {@code PUT  /payment-systems/:id} : Updates an existing paymentSystem.
     *
     * @param id the id of the paymentSystem to save.
     * @param paymentSystem the paymentSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystem,
     * or with status {@code 400 (Bad Request)} if the paymentSystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentSystem> updatePaymentSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentSystem paymentSystem
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentSystem : {}, {}", id, paymentSystem);
        if (paymentSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentSystem = paymentSystemRepository.save(paymentSystem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystem.getId().toString()))
            .body(paymentSystem);
    }

    /**
     * {@code PATCH  /payment-systems/:id} : Partial updates given fields of an existing paymentSystem, field will ignore if it is null
     *
     * @param id the id of the paymentSystem to save.
     * @param paymentSystem the paymentSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystem,
     * or with status {@code 400 (Bad Request)} if the paymentSystem is not valid,
     * or with status {@code 404 (Not Found)} if the paymentSystem is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentSystem> partialUpdatePaymentSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentSystem paymentSystem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentSystem partially : {}, {}", id, paymentSystem);
        if (paymentSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentSystem> result = paymentSystemRepository
            .findById(paymentSystem.getId())
            .map(existingPaymentSystem -> {
                if (paymentSystem.getName() != null) {
                    existingPaymentSystem.setName(paymentSystem.getName());
                }

                return existingPaymentSystem;
            })
            .map(paymentSystemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystem.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-systems} : get all the paymentSystems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentSystems in body.
     */
    @GetMapping("")
    public List<PaymentSystem> getAllPaymentSystems(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PaymentSystems");
        if (eagerload) {
            return paymentSystemRepository.findAllWithEagerRelationships();
        } else {
            return paymentSystemRepository.findAll();
        }
    }

    /**
     * {@code GET  /payment-systems/:id} : get the "id" paymentSystem.
     *
     * @param id the id of the paymentSystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentSystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystem> getPaymentSystem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentSystem : {}", id);
        Optional<PaymentSystem> paymentSystem = paymentSystemRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(paymentSystem);
    }

    /**
     * {@code DELETE  /payment-systems/:id} : delete the "id" paymentSystem.
     *
     * @param id the id of the paymentSystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentSystem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentSystem : {}", id);
        paymentSystemRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
