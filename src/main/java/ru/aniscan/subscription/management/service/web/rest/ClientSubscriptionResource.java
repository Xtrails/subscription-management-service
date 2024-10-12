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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.aniscan.subscription.management.service.domain.ClientSubscription;
import ru.aniscan.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.ClientSubscription}.
 */
@RestController
@RequestMapping("/api/client-subscriptions")
@Transactional
public class ClientSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientSubscriptionResource.class);

    private static final String ENTITY_NAME = "clientSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientSubscriptionRepository clientSubscriptionRepository;

    public ClientSubscriptionResource(ClientSubscriptionRepository clientSubscriptionRepository) {
        this.clientSubscriptionRepository = clientSubscriptionRepository;
    }

    /**
     * {@code POST  /client-subscriptions} : Create a new clientSubscription.
     *
     * @param clientSubscription the clientSubscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientSubscription, or with status {@code 400 (Bad Request)} if the clientSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientSubscription> createClientSubscription(@Valid @RequestBody ClientSubscription clientSubscription)
        throws URISyntaxException {
        LOG.debug("REST request to save ClientSubscription : {}", clientSubscription);
        if (clientSubscription.getId() != null) {
            throw new BadRequestAlertException("A new clientSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clientSubscription = clientSubscriptionRepository.save(clientSubscription);
        return ResponseEntity.created(new URI("/api/client-subscriptions/" + clientSubscription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clientSubscription.getId().toString()))
            .body(clientSubscription);
    }

    /**
     * {@code PUT  /client-subscriptions/:id} : Updates an existing clientSubscription.
     *
     * @param id the id of the clientSubscription to save.
     * @param clientSubscription the clientSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientSubscription,
     * or with status {@code 400 (Bad Request)} if the clientSubscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientSubscription> updateClientSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientSubscription clientSubscription
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClientSubscription : {}, {}", id, clientSubscription);
        if (clientSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientSubscription = clientSubscriptionRepository.save(clientSubscription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientSubscription.getId().toString()))
            .body(clientSubscription);
    }

    /**
     * {@code PATCH  /client-subscriptions/:id} : Partial updates given fields of an existing clientSubscription, field will ignore if it is null
     *
     * @param id the id of the clientSubscription to save.
     * @param clientSubscription the clientSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientSubscription,
     * or with status {@code 400 (Bad Request)} if the clientSubscription is not valid,
     * or with status {@code 404 (Not Found)} if the clientSubscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientSubscription> partialUpdateClientSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientSubscription clientSubscription
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClientSubscription partially : {}, {}", id, clientSubscription);
        if (clientSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientSubscription> result = clientSubscriptionRepository
            .findById(clientSubscription.getId())
            .map(existingClientSubscription -> {
                if (clientSubscription.getStartDate() != null) {
                    existingClientSubscription.setStartDate(clientSubscription.getStartDate());
                }
                if (clientSubscription.getEndDate() != null) {
                    existingClientSubscription.setEndDate(clientSubscription.getEndDate());
                }
                if (clientSubscription.getStatus() != null) {
                    existingClientSubscription.setStatus(clientSubscription.getStatus());
                }

                return existingClientSubscription;
            })
            .map(clientSubscriptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientSubscription.getId().toString())
        );
    }

    /**
     * {@code GET  /client-subscriptions} : get all the clientSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClientSubscription>> getAllClientSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ClientSubscriptions");
        Page<ClientSubscription> page = clientSubscriptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-subscriptions/:id} : get the "id" clientSubscription.
     *
     * @param id the id of the clientSubscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientSubscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientSubscription> getClientSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClientSubscription : {}", id);
        Optional<ClientSubscription> clientSubscription = clientSubscriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clientSubscription);
    }

    /**
     * {@code DELETE  /client-subscriptions/:id} : delete the "id" clientSubscription.
     *
     * @param id the id of the clientSubscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClientSubscription : {}", id);
        clientSubscriptionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
