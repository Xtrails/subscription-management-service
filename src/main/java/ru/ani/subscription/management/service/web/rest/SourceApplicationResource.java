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
import ru.ani.subscription.management.service.repository.SourceApplicationRepository;
import ru.ani.subscription.management.service.service.SourceApplicationService;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.SourceApplication}.
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
     * @param sourceApplicationDTO the sourceApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceApplicationDTO, or with status {@code 400 (Bad Request)} if the sourceApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SourceApplicationDTO> createSourceApplication(@Valid @RequestBody SourceApplicationDTO sourceApplicationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SourceApplication : {}", sourceApplicationDTO);
        if (sourceApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourceApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceApplicationDTO = sourceApplicationService.save(sourceApplicationDTO);
        return ResponseEntity.created(new URI("/api/source-applications/" + sourceApplicationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sourceApplicationDTO.getId().toString()))
            .body(sourceApplicationDTO);
    }

    /**
     * {@code PUT  /source-applications/:id} : Updates an existing sourceApplication.
     *
     * @param id the id of the sourceApplicationDTO to save.
     * @param sourceApplicationDTO the sourceApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the sourceApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SourceApplicationDTO> updateSourceApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceApplicationDTO sourceApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SourceApplication : {}, {}", id, sourceApplicationDTO);
        if (sourceApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sourceApplicationDTO = sourceApplicationService.update(sourceApplicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplicationDTO.getId().toString()))
            .body(sourceApplicationDTO);
    }

    /**
     * {@code PATCH  /source-applications/:id} : Partial updates given fields of an existing sourceApplication, field will ignore if it is null
     *
     * @param id the id of the sourceApplicationDTO to save.
     * @param sourceApplicationDTO the sourceApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the sourceApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceApplicationDTO> partialUpdateSourceApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceApplicationDTO sourceApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SourceApplication partially : {}, {}", id, sourceApplicationDTO);
        if (sourceApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceApplicationDTO> result = sourceApplicationService.partialUpdate(sourceApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /source-applications} : get all the sourceApplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceApplications in body.
     */
    @GetMapping("")
    public List<SourceApplicationDTO> getAllSourceApplications() {
        LOG.debug("REST request to get all SourceApplications");
        return sourceApplicationService.findAll();
    }

    /**
     * {@code GET  /source-applications/:id} : get the "id" sourceApplication.
     *
     * @param id the id of the sourceApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SourceApplicationDTO> getSourceApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SourceApplication : {}", id);
        Optional<SourceApplicationDTO> sourceApplicationDTO = sourceApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceApplicationDTO);
    }

    /**
     * {@code DELETE  /source-applications/:id} : delete the "id" sourceApplication.
     *
     * @param id the id of the sourceApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourceApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SourceApplication : {}", id);
        sourceApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
