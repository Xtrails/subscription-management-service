package ru.ani.subscription.management.service.web.rest;

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
import org.springframework.web.bind.annotation.*;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.PaymentSystemService;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.PaymentSystem}.
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
     * @param paymentSystemDTO the paymentSystemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentSystemDTO, or with status {@code 400 (Bad Request)} if the paymentSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentSystemDTO> createPaymentSystem(@Valid @RequestBody PaymentSystemDTO paymentSystemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentSystem : {}", paymentSystemDTO);
        if (paymentSystemDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentSystemDTO = paymentSystemService.save(paymentSystemDTO);
        return ResponseEntity.created(new URI("/api/payment-systems/" + paymentSystemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentSystemDTO.getId().toString()))
            .body(paymentSystemDTO);
    }

    /**
     * {@code PUT  /payment-systems/:id} : Updates an existing paymentSystem.
     *
     * @param id the id of the paymentSystemDTO to save.
     * @param paymentSystemDTO the paymentSystemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentSystemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentSystemDTO> updatePaymentSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentSystemDTO paymentSystemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentSystem : {}, {}", id, paymentSystemDTO);
        if (paymentSystemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentSystemDTO = paymentSystemService.update(paymentSystemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystemDTO.getId().toString()))
            .body(paymentSystemDTO);
    }

    /**
     * {@code PATCH  /payment-systems/:id} : Partial updates given fields of an existing paymentSystem, field will ignore if it is null
     *
     * @param id the id of the paymentSystemDTO to save.
     * @param paymentSystemDTO the paymentSystemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSystemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentSystemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentSystemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentSystemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentSystemDTO> partialUpdatePaymentSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentSystemDTO paymentSystemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentSystem partially : {}, {}", id, paymentSystemDTO);
        if (paymentSystemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSystemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentSystemDTO> result = paymentSystemService.partialUpdate(paymentSystemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSystemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-systems} : get all the paymentSystems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentSystems in body.
     */
    @GetMapping("")
    public List<PaymentSystemDTO> getAllPaymentSystems() {
        LOG.debug("REST request to get all PaymentSystems");
        return paymentSystemService.findAll();
    }

    /**
     * {@code GET  /payment-systems/:id} : get the "id" paymentSystem.
     *
     * @param id the id of the paymentSystemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentSystemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystemDTO> getPaymentSystem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentSystem : {}", id);
        Optional<PaymentSystemDTO> paymentSystemDTO = paymentSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentSystemDTO);
    }

    /**
     * {@code DELETE  /payment-systems/:id} : delete the "id" paymentSystem.
     *
     * @param id the id of the paymentSystemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentSystem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentSystem : {}", id);
        paymentSystemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
