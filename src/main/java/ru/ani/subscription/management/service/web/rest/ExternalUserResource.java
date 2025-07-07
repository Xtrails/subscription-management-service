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
import ru.ani.subscription.management.service.repository.ExternalUserRepository;
import ru.ani.subscription.management.service.service.ExternalUserService;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.ExternalUser}.
 */
@RestController
@RequestMapping("/api/external-users")
public class ExternalUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalUserResource.class);

    private static final String ENTITY_NAME = "externalUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExternalUserService externalUserService;

    private final ExternalUserRepository externalUserRepository;

    public ExternalUserResource(ExternalUserService externalUserService, ExternalUserRepository externalUserRepository) {
        this.externalUserService = externalUserService;
        this.externalUserRepository = externalUserRepository;
    }

    /**
     * {@code POST  /external-users} : Create a new externalUser.
     *
     * @param externalUserDTO the externalUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalUserDTO, or with status {@code 400 (Bad Request)} if the externalUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExternalUserDTO> createExternalUser(@Valid @RequestBody ExternalUserDTO externalUserDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExternalUser : {}", externalUserDTO);
        if (externalUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new externalUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        externalUserDTO = externalUserService.save(externalUserDTO);
        return ResponseEntity.created(new URI("/api/external-users/" + externalUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, externalUserDTO.getId().toString()))
            .body(externalUserDTO);
    }

    /**
     * {@code PUT  /external-users/:id} : Updates an existing externalUser.
     *
     * @param id the id of the externalUserDTO to save.
     * @param externalUserDTO the externalUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalUserDTO,
     * or with status {@code 400 (Bad Request)} if the externalUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExternalUserDTO> updateExternalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExternalUserDTO externalUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExternalUser : {}, {}", id, externalUserDTO);
        if (externalUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        externalUserDTO = externalUserService.update(externalUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, externalUserDTO.getId().toString()))
            .body(externalUserDTO);
    }

    /**
     * {@code PATCH  /external-users/:id} : Partial updates given fields of an existing externalUser, field will ignore if it is null
     *
     * @param id the id of the externalUserDTO to save.
     * @param externalUserDTO the externalUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalUserDTO,
     * or with status {@code 400 (Bad Request)} if the externalUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the externalUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExternalUserDTO> partialUpdateExternalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExternalUserDTO externalUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExternalUser partially : {}, {}", id, externalUserDTO);
        if (externalUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExternalUserDTO> result = externalUserService.partialUpdate(externalUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, externalUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /external-users} : get all the externalUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of externalUsers in body.
     */
    @GetMapping("")
    public List<ExternalUserDTO> getAllExternalUsers() {
        LOG.debug("REST request to get all ExternalUsers");
        return externalUserService.findAll();
    }

    /**
     * {@code GET  /external-users/:id} : get the "id" externalUser.
     *
     * @param id the id of the externalUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExternalUserDTO> getExternalUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExternalUser : {}", id);
        Optional<ExternalUserDTO> externalUserDTO = externalUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(externalUserDTO);
    }

    /**
     * {@code DELETE  /external-users/:id} : delete the "id" externalUser.
     *
     * @param id the id of the externalUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExternalUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExternalUser : {}", id);
        externalUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
