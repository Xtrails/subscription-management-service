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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ani.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.ani.subscription.management.service.service.ClientSubscriptionService;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.ClientSubscription}.
 */
@RestController
@RequestMapping("/api/client-subscriptions")
public class ClientSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientSubscriptionResource.class);

    private static final String ENTITY_NAME = "clientSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientSubscriptionService clientSubscriptionService;

    private final ClientSubscriptionRepository clientSubscriptionRepository;

    public ClientSubscriptionResource(
        ClientSubscriptionService clientSubscriptionService,
        ClientSubscriptionRepository clientSubscriptionRepository
    ) {
        this.clientSubscriptionService = clientSubscriptionService;
        this.clientSubscriptionRepository = clientSubscriptionRepository;
    }

    /**
     * {@code POST  /client-subscriptions} : Create a new clientSubscription.
     *
     * @param clientSubscriptionDTO the clientSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientSubscriptionDTO, or with status {@code 400 (Bad Request)} if the clientSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientSubscriptionDTO> createClientSubscription(@Valid @RequestBody ClientSubscriptionDTO clientSubscriptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ClientSubscription : {}", clientSubscriptionDTO);
        if (clientSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clientSubscriptionDTO = clientSubscriptionService.save(clientSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/client-subscriptions/" + clientSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clientSubscriptionDTO.getId().toString()))
            .body(clientSubscriptionDTO);
    }

    /**
     * {@code PUT  /client-subscriptions/:id} : Updates an existing clientSubscription.
     *
     * @param id the id of the clientSubscriptionDTO to save.
     * @param clientSubscriptionDTO the clientSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the clientSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientSubscriptionDTO> updateClientSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientSubscriptionDTO clientSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClientSubscription : {}, {}", id, clientSubscriptionDTO);
        if (clientSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientSubscriptionDTO = clientSubscriptionService.update(clientSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientSubscriptionDTO.getId().toString()))
            .body(clientSubscriptionDTO);
    }

    /**
     * {@code PATCH  /client-subscriptions/:id} : Partial updates given fields of an existing clientSubscription, field will ignore if it is null
     *
     * @param id the id of the clientSubscriptionDTO to save.
     * @param clientSubscriptionDTO the clientSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the clientSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientSubscriptionDTO> partialUpdateClientSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientSubscriptionDTO clientSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClientSubscription partially : {}, {}", id, clientSubscriptionDTO);
        if (clientSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientSubscriptionDTO> result = clientSubscriptionService.partialUpdate(clientSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /client-subscriptions} : get all the clientSubscriptions.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClientSubscriptionDTO>> getAllClientSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("payment-is-null".equals(filter)) {
            LOG.debug("REST request to get all ClientSubscriptions where payment is null");
            return new ResponseEntity<>(clientSubscriptionService.findAllWherePaymentIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of ClientSubscriptions");
        Page<ClientSubscriptionDTO> page = clientSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-subscriptions/:id} : get the "id" clientSubscription.
     *
     * @param id the id of the clientSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientSubscriptionDTO> getClientSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClientSubscription : {}", id);
        Optional<ClientSubscriptionDTO> clientSubscriptionDTO = clientSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientSubscriptionDTO);
    }

    /**
     * {@code DELETE  /client-subscriptions/:id} : delete the "id" clientSubscription.
     *
     * @param id the id of the clientSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClientSubscription : {}", id);
        clientSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
