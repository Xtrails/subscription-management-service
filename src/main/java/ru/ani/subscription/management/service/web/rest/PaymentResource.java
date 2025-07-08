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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ani.subscription.management.service.repository.PaymentRepository;
import ru.ani.subscription.management.service.service.PaymentService;
import ru.ani.subscription.management.service.service.dto.PaymentDto;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.PaymentDao}.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentService paymentService;

    private final PaymentRepository paymentRepository;

    public PaymentResource(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    /**
     * {@code POST  /payments} : Create a new payment.
     *
     * @param paymentDto the paymentDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentDto, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentDto paymentDto) throws URISyntaxException {
        LOG.debug("REST request to save Payment : {}", paymentDto);
        if (paymentDto.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentDto = paymentService.save(paymentDto);
        return ResponseEntity.created(new URI("/api/payments/" + paymentDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentDto.getId().toString()))
            .body(paymentDto);
    }

    /**
     * {@code PUT  /payments/:id} : Updates an existing payment.
     *
     * @param id the id of the paymentDto to save.
     * @param paymentDto the paymentDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDto,
     * or with status {@code 400 (Bad Request)} if the paymentDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PaymentDto paymentDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Payment : {}, {}", id, paymentDto);
        if (paymentDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentDto = paymentService.update(paymentDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDto.getId().toString()))
            .body(paymentDto);
    }

    /**
     * {@code PATCH  /payments/:id} : Partial updates given fields of an existing payment, field will ignore if it is null
     *
     * @param id the id of the paymentDto to save.
     * @param paymentDto the paymentDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDto,
     * or with status {@code 400 (Bad Request)} if the paymentDto is not valid,
     * or with status {@code 404 (Not Found)} if the paymentDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentDto> partialUpdatePayment(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PaymentDto paymentDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Payment partially : {}, {}", id, paymentDto);
        if (paymentDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentDto> result = paymentService.partialUpdate(paymentDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDto.getId().toString())
        );
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentDto>> getAllPayments(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Payments");
        Page<PaymentDto> page = paymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the paymentDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Payment : {}", id);
        Optional<PaymentDto> paymentDto = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDto);
    }

    /**
     * {@code DELETE  /payments/:id} : delete the "id" payment.
     *
     * @param id the id of the paymentDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
