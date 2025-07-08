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
import ru.ani.subscription.management.service.repository.SourceApplicationRepository;
import ru.ani.subscription.management.service.service.SourceApplicationService;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.SourceApplicationDao}.
 */
@RestController
@RequestMapping("/api/source-applications")
public class SourceApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceApplicationResource.class);

    private static final String ENTITY_NAME = "sourceApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceApplicationService sourceApplicationService;

    private final SourceApplicationRepository sourceApplicationRepository;

    public SourceApplicationResource(
        SourceApplicationService sourceApplicationService,
        SourceApplicationRepository sourceApplicationRepository
    ) {
        this.sourceApplicationService = sourceApplicationService;
        this.sourceApplicationRepository = sourceApplicationRepository;
    }

    /**
     * {@code POST  /source-applications} : Create a new sourceApplication.
     *
     * @param sourceApplicationDto the sourceApplicationDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceApplicationDto, or with status {@code 400 (Bad Request)} if the sourceApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SourceApplicationDto> createSourceApplication(@Valid @RequestBody SourceApplicationDto sourceApplicationDto)
        throws URISyntaxException {
        LOG.debug("REST request to save SourceApplication : {}", sourceApplicationDto);
        if (sourceApplicationDto.getId() != null) {
            throw new BadRequestAlertException("A new sourceApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceApplicationDto = sourceApplicationService.save(sourceApplicationDto);
        return ResponseEntity.created(new URI("/api/source-applications/" + sourceApplicationDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sourceApplicationDto.getId().toString()))
            .body(sourceApplicationDto);
    }

    /**
     * {@code PUT  /source-applications/:id} : Updates an existing sourceApplication.
     *
     * @param id the id of the sourceApplicationDto to save.
     * @param sourceApplicationDto the sourceApplicationDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplicationDto,
     * or with status {@code 400 (Bad Request)} if the sourceApplicationDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplicationDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SourceApplicationDto> updateSourceApplication(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SourceApplicationDto sourceApplicationDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update SourceApplication : {}, {}", id, sourceApplicationDto);
        if (sourceApplicationDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplicationDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sourceApplicationDto = sourceApplicationService.update(sourceApplicationDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplicationDto.getId().toString()))
            .body(sourceApplicationDto);
    }

    /**
     * {@code PATCH  /source-applications/:id} : Partial updates given fields of an existing sourceApplication, field will ignore if it is null
     *
     * @param id the id of the sourceApplicationDto to save.
     * @param sourceApplicationDto the sourceApplicationDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplicationDto,
     * or with status {@code 400 (Bad Request)} if the sourceApplicationDto is not valid,
     * or with status {@code 404 (Not Found)} if the sourceApplicationDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplicationDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceApplicationDto> partialUpdateSourceApplication(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SourceApplicationDto sourceApplicationDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SourceApplication partially : {}, {}", id, sourceApplicationDto);
        if (sourceApplicationDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplicationDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceApplicationDto> result = sourceApplicationService.partialUpdate(sourceApplicationDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplicationDto.getId().toString())
        );
    }

    /**
     * {@code GET  /source-applications} : get all the sourceApplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceApplications in body.
     */
    @GetMapping("")
    public List<SourceApplicationDto> getAllSourceApplications() {
        LOG.debug("REST request to get all SourceApplications");
        return sourceApplicationService.findAll();
    }

    /**
     * {@code GET  /source-applications/:id} : get the "id" sourceApplication.
     *
     * @param id the id of the sourceApplicationDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceApplicationDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SourceApplicationDto> getSourceApplication(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SourceApplication : {}", id);
        Optional<SourceApplicationDto> sourceApplicationDto = sourceApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceApplicationDto);
    }

    /**
     * {@code DELETE  /source-applications/:id} : delete the "id" sourceApplication.
     *
     * @param id the id of the sourceApplicationDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourceApplication(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SourceApplication : {}", id);
        sourceApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
