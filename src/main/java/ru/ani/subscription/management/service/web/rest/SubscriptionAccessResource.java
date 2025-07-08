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
import ru.ani.subscription.management.service.repository.SubscriptionAccessRepository;
import ru.ani.subscription.management.service.service.SubscriptionAccessService;
import ru.ani.subscription.management.service.service.dto.SubscriptionAccessDto;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.SubscriptionAccessDao}.
 */
@RestController
@RequestMapping("/api/subscription-accesses")
public class SubscriptionAccessResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAccessResource.class);

    private static final String ENTITY_NAME = "subscriptionAccess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionAccessService subscriptionAccessService;

    private final SubscriptionAccessRepository subscriptionAccessRepository;

    public SubscriptionAccessResource(
        SubscriptionAccessService subscriptionAccessService,
        SubscriptionAccessRepository subscriptionAccessRepository
    ) {
        this.subscriptionAccessService = subscriptionAccessService;
        this.subscriptionAccessRepository = subscriptionAccessRepository;
    }

    /**
     * {@code POST  /subscription-accesses} : Create a new subscriptionAccess.
     *
     * @param subscriptionAccessDto the subscriptionAccessDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionAccessDto, or with status {@code 400 (Bad Request)} if the subscriptionAccess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionAccessDto> createSubscriptionAccess(@Valid @RequestBody SubscriptionAccessDto subscriptionAccessDto)
        throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionAccess : {}", subscriptionAccessDto);
        if (subscriptionAccessDto.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionAccess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionAccessDto = subscriptionAccessService.save(subscriptionAccessDto);
        return ResponseEntity.created(new URI("/api/subscription-accesses/" + subscriptionAccessDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionAccessDto.getId().toString()))
            .body(subscriptionAccessDto);
    }

    /**
     * {@code PUT  /subscription-accesses/:id} : Updates an existing subscriptionAccess.
     *
     * @param id the id of the subscriptionAccessDto to save.
     * @param subscriptionAccessDto the subscriptionAccessDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionAccessDto,
     * or with status {@code 400 (Bad Request)} if the subscriptionAccessDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionAccessDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionAccessDto> updateSubscriptionAccess(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SubscriptionAccessDto subscriptionAccessDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionAccess : {}, {}", id, subscriptionAccessDto);
        if (subscriptionAccessDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionAccessDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionAccessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionAccessDto = subscriptionAccessService.update(subscriptionAccessDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionAccessDto.getId().toString()))
            .body(subscriptionAccessDto);
    }

    /**
     * {@code PATCH  /subscription-accesses/:id} : Partial updates given fields of an existing subscriptionAccess, field will ignore if it is null
     *
     * @param id the id of the subscriptionAccessDto to save.
     * @param subscriptionAccessDto the subscriptionAccessDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionAccessDto,
     * or with status {@code 400 (Bad Request)} if the subscriptionAccessDto is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionAccessDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionAccessDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionAccessDto> partialUpdateSubscriptionAccess(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SubscriptionAccessDto subscriptionAccessDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionAccess partially : {}, {}", id, subscriptionAccessDto);
        if (subscriptionAccessDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionAccessDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionAccessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionAccessDto> result = subscriptionAccessService.partialUpdate(subscriptionAccessDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionAccessDto.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-accesses} : get all the subscriptionAccesses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionAccesses in body.
     */
    @GetMapping("")
    public List<SubscriptionAccessDto> getAllSubscriptionAccesses(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SubscriptionAccesses");
        return subscriptionAccessService.findAll();
    }

    /**
     * {@code GET  /subscription-accesses/:id} : get the "id" subscriptionAccess.
     *
     * @param id the id of the subscriptionAccessDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionAccessDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionAccessDto> getSubscriptionAccess(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SubscriptionAccess : {}", id);
        Optional<SubscriptionAccessDto> subscriptionAccessDto = subscriptionAccessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionAccessDto);
    }

    /**
     * {@code DELETE  /subscription-accesses/:id} : delete the "id" subscriptionAccess.
     *
     * @param id the id of the subscriptionAccessDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionAccess(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SubscriptionAccess : {}", id);
        subscriptionAccessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
