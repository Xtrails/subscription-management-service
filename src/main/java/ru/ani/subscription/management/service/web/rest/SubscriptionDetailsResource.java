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
import ru.ani.subscription.management.service.repository.SubscriptionDetailsRepository;
import ru.ani.subscription.management.service.service.SubscriptionDetailsService;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.SubscriptionDetails}.
 */
@RestController
@RequestMapping("/api/subscription-details")
public class SubscriptionDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionDetailsResource.class);

    private static final String ENTITY_NAME = "subscriptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionDetailsService subscriptionDetailsService;

    private final SubscriptionDetailsRepository subscriptionDetailsRepository;

    public SubscriptionDetailsResource(
        SubscriptionDetailsService subscriptionDetailsService,
        SubscriptionDetailsRepository subscriptionDetailsRepository
    ) {
        this.subscriptionDetailsService = subscriptionDetailsService;
        this.subscriptionDetailsRepository = subscriptionDetailsRepository;
    }

    /**
     * {@code POST  /subscription-details} : Create a new subscriptionDetails.
     *
     * @param subscriptionDetailsDTO the subscriptionDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionDetailsDTO, or with status {@code 400 (Bad Request)} if the subscriptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionDetailsDTO> createSubscriptionDetails(
        @Valid @RequestBody SubscriptionDetailsDTO subscriptionDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionDetails : {}", subscriptionDetailsDTO);
        if (subscriptionDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionDetailsDTO = subscriptionDetailsService.save(subscriptionDetailsDTO);
        return ResponseEntity.created(new URI("/api/subscription-details/" + subscriptionDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionDetailsDTO.getId().toString()))
            .body(subscriptionDetailsDTO);
    }

    /**
     * {@code PUT  /subscription-details/:id} : Updates an existing subscriptionDetails.
     *
     * @param id the id of the subscriptionDetailsDTO to save.
     * @param subscriptionDetailsDTO the subscriptionDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDetailsDTO> updateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionDetailsDTO subscriptionDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionDetails : {}, {}", id, subscriptionDetailsDTO);
        if (subscriptionDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionDetailsDTO = subscriptionDetailsService.update(subscriptionDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionDetailsDTO.getId().toString()))
            .body(subscriptionDetailsDTO);
    }

    /**
     * {@code PATCH  /subscription-details/:id} : Partial updates given fields of an existing subscriptionDetails, field will ignore if it is null
     *
     * @param id the id of the subscriptionDetailsDTO to save.
     * @param subscriptionDetailsDTO the subscriptionDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionDetailsDTO> partialUpdateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionDetailsDTO subscriptionDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionDetails partially : {}, {}", id, subscriptionDetailsDTO);
        if (subscriptionDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionDetailsDTO> result = subscriptionDetailsService.partialUpdate(subscriptionDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-details} : get all the subscriptionDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionDetails in body.
     */
    @GetMapping("")
    public List<SubscriptionDetailsDTO> getAllSubscriptionDetails() {
        LOG.debug("REST request to get all SubscriptionDetails");
        return subscriptionDetailsService.findAll();
    }

    /**
     * {@code GET  /subscription-details/:id} : get the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDetailsDTO> getSubscriptionDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscriptionDetails : {}", id);
        Optional<SubscriptionDetailsDTO> subscriptionDetailsDTO = subscriptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionDetailsDTO);
    }

    /**
     * {@code DELETE  /subscription-details/:id} : delete the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscriptionDetails : {}", id);
        subscriptionDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
