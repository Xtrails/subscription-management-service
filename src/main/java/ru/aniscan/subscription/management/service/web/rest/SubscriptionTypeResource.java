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
import ru.aniscan.subscription.management.service.domain.SubscriptionType;
import ru.aniscan.subscription.management.service.repository.SubscriptionTypeRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.SubscriptionType}.
 */
@RestController
@RequestMapping("/api/subscription-types")
@Transactional
public class SubscriptionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionTypeResource.class);

    private static final String ENTITY_NAME = "subscriptionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public SubscriptionTypeResource(SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    /**
     * {@code POST  /subscription-types} : Create a new subscriptionType.
     *
     * @param subscriptionType the subscriptionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionType, or with status {@code 400 (Bad Request)} if the subscriptionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionType> createSubscriptionType(@Valid @RequestBody SubscriptionType subscriptionType)
        throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionType : {}", subscriptionType);
        if (subscriptionType.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionType = subscriptionTypeRepository.save(subscriptionType);
        return ResponseEntity.created(new URI("/api/subscription-types/" + subscriptionType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionType.getId().toString()))
            .body(subscriptionType);
    }

    /**
     * {@code PUT  /subscription-types/:id} : Updates an existing subscriptionType.
     *
     * @param id the id of the subscriptionType to save.
     * @param subscriptionType the subscriptionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionType,
     * or with status {@code 400 (Bad Request)} if the subscriptionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionType> updateSubscriptionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionType subscriptionType
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionType : {}, {}", id, subscriptionType);
        if (subscriptionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionType = subscriptionTypeRepository.save(subscriptionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionType.getId().toString()))
            .body(subscriptionType);
    }

    /**
     * {@code PATCH  /subscription-types/:id} : Partial updates given fields of an existing subscriptionType, field will ignore if it is null
     *
     * @param id the id of the subscriptionType to save.
     * @param subscriptionType the subscriptionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionType,
     * or with status {@code 400 (Bad Request)} if the subscriptionType is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionType is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionType> partialUpdateSubscriptionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionType subscriptionType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionType partially : {}, {}", id, subscriptionType);
        if (subscriptionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionType> result = subscriptionTypeRepository
            .findById(subscriptionType.getId())
            .map(existingSubscriptionType -> {
                if (subscriptionType.getName() != null) {
                    existingSubscriptionType.setName(subscriptionType.getName());
                }
                if (subscriptionType.getDescription() != null) {
                    existingSubscriptionType.setDescription(subscriptionType.getDescription());
                }
                if (subscriptionType.getPrice() != null) {
                    existingSubscriptionType.setPrice(subscriptionType.getPrice());
                }
                if (subscriptionType.getDuration() != null) {
                    existingSubscriptionType.setDuration(subscriptionType.getDuration());
                }

                return existingSubscriptionType;
            })
            .map(subscriptionTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionType.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-types} : get all the subscriptionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionTypes in body.
     */
    @GetMapping("")
    public List<SubscriptionType> getAllSubscriptionTypes() {
        LOG.debug("REST request to get all SubscriptionTypes");
        return subscriptionTypeRepository.findAll();
    }

    /**
     * {@code GET  /subscription-types/:id} : get the "id" subscriptionType.
     *
     * @param id the id of the subscriptionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionType> getSubscriptionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscriptionType : {}", id);
        Optional<SubscriptionType> subscriptionType = subscriptionTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subscriptionType);
    }

    /**
     * {@code DELETE  /subscription-types/:id} : delete the "id" subscriptionType.
     *
     * @param id the id of the subscriptionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscriptionType : {}", id);
        subscriptionTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
