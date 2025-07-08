package ru.ani.subscription.management.service.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.PaymentSystemService;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDto;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.PaymentSystemDao}.
 */
@RestController
@RequestMapping("/api/payment-systems")
public class PaymentSystemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSystemResource.class);

    private static final String ENTITY_NAME = "paymentSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentSystemService paymentSystemService;

    private final PaymentSystemRepository paymentSystemRepository;

    public PaymentSystemResource(PaymentSystemService paymentSystemService, PaymentSystemRepository paymentSystemRepository) {
        this.paymentSystemService = paymentSystemService;
        this.paymentSystemRepository = paymentSystemRepository;
    }

    /**
     * {@code POST  /payment-systems} : Create a new paymentSystem.
     *
     * @param paymentSystemDto the paymentSystemDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentSystemDto, or with status {@code 400 (Bad Request)} if the paymentSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentSystemDto> createPaymentSystem(@Valid @RequestBody PaymentSystemDto paymentSystemDto)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentSystem : {}", paymentSystemDto);
        if (paymentSystemDto.getId() != null) {
            throw new BadRequestAlertException("A new paymentSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentSystemDto = paymentSystemService.save(paymentSystemDto);
        return ResponseEntity.created(new URI("/api/payment-systems/" + paymentSystemDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentSystemDto.getId().toString()))
            .body(paymentSystemDto);
    }

    /**
     * {@code PUT  /payment-systems/:id} : Updates an existing paymentSystem.
     *
     * @param id the id of the paymentSystemDto to save.
     * @param paymentSystemDto the paymentSystemDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystemDto,
     * or with status {@code 400 (Bad Request)} if the paymentSystemDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystemDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentSystemDto> updatePaymentSystem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PaymentSystemDto paymentSystemDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentSystem : {}, {}", id, paymentSystemDto);
        if (paymentSystemDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystemDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentSystemDto = paymentSystemService.update(paymentSystemDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystemDto.getId().toString()))
            .body(paymentSystemDto);
    }

    /**
     * {@code PATCH  /payment-systems/:id} : Partial updates given fields of an existing paymentSystem, field will ignore if it is null
     *
     * @param id the id of the paymentSystemDto to save.
     * @param paymentSystemDto the paymentSystemDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystemDto,
     * or with status {@code 400 (Bad Request)} if the paymentSystemDto is not valid,
     * or with status {@code 404 (Not Found)} if the paymentSystemDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystemDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentSystemDto> partialUpdatePaymentSystem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PaymentSystemDto paymentSystemDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentSystem partially : {}, {}", id, paymentSystemDto);
        if (paymentSystemDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystemDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentSystemDto> result = paymentSystemService.partialUpdate(paymentSystemDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystemDto.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-systems} : get all the paymentSystems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentSystems in body.
     */
    @GetMapping("")
    public List<PaymentSystemDto> getAllPaymentSystems() {
        LOG.debug("REST request to get all PaymentSystems");
        return paymentSystemService.findAll();
    }

    /**
     * {@code GET  /payment-systems/:id} : get the "id" paymentSystem.
     *
     * @param id the id of the paymentSystemDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentSystemDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystemDto> getPaymentSystem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get PaymentSystem : {}", id);
        Optional<PaymentSystemDto> paymentSystemDto = paymentSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentSystemDto);
    }

    /**
     * {@code DELETE  /payment-systems/:id} : delete the "id" paymentSystem.
     *
     * @param id the id of the paymentSystemDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentSystem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete PaymentSystem : {}", id);
        paymentSystemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
