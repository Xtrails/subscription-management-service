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
import ru.aniscan.subscription.management.service.domain.SourceApplication;
import ru.aniscan.subscription.management.service.repository.SourceApplicationRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.SourceApplication}.
 */
@RestController
@RequestMapping("/api/source-applications")
@Transactional
public class SourceApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceApplicationResource.class);

    private static final String ENTITY_NAME = "sourceApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceApplicationRepository sourceApplicationRepository;

    public SourceApplicationResource(SourceApplicationRepository sourceApplicationRepository) {
        this.sourceApplicationRepository = sourceApplicationRepository;
    }

    /**
     * {@code POST  /source-applications} : Create a new sourceApplication.
     *
     * @param sourceApplication the sourceApplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceApplication, or with status {@code 400 (Bad Request)} if the sourceApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SourceApplication> createSourceApplication(@Valid @RequestBody SourceApplication sourceApplication)
        throws URISyntaxException {
        LOG.debug("REST request to save SourceApplication : {}", sourceApplication);
        if (sourceApplication.getId() != null) {
            throw new BadRequestAlertException("A new sourceApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceApplication = sourceApplicationRepository.save(sourceApplication);
        return ResponseEntity.created(new URI("/api/source-applications/" + sourceApplication.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sourceApplication.getId().toString()))
            .body(sourceApplication);
    }

    /**
     * {@code PUT  /source-applications/:id} : Updates an existing sourceApplication.
     *
     * @param id the id of the sourceApplication to save.
     * @param sourceApplication the sourceApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplication,
     * or with status {@code 400 (Bad Request)} if the sourceApplication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SourceApplication> updateSourceApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceApplication sourceApplication
    ) throws URISyntaxException {
        LOG.debug("REST request to update SourceApplication : {}, {}", id, sourceApplication);
        if (sourceApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sourceApplication = sourceApplicationRepository.save(sourceApplication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplication.getId().toString()))
            .body(sourceApplication);
    }

    /**
     * {@code PATCH  /source-applications/:id} : Partial updates given fields of an existing sourceApplication, field will ignore if it is null
     *
     * @param id the id of the sourceApplication to save.
     * @param sourceApplication the sourceApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApplication,
     * or with status {@code 400 (Bad Request)} if the sourceApplication is not valid,
     * or with status {@code 404 (Not Found)} if the sourceApplication is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceApplication> partialUpdateSourceApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceApplication sourceApplication
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SourceApplication partially : {}, {}", id, sourceApplication);
        if (sourceApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceApplication> result = sourceApplicationRepository
            .findById(sourceApplication.getId())
            .map(existingSourceApplication -> {
                if (sourceApplication.getApplicationName() != null) {
                    existingSourceApplication.setApplicationName(sourceApplication.getApplicationName());
                }

                return existingSourceApplication;
            })
            .map(sourceApplicationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApplication.getId().toString())
        );
    }

    /**
     * {@code GET  /source-applications} : get all the sourceApplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceApplications in body.
     */
    @GetMapping("")
    public List<SourceApplication> getAllSourceApplications() {
        LOG.debug("REST request to get all SourceApplications");
        return sourceApplicationRepository.findAll();
    }

    /**
     * {@code GET  /source-applications/:id} : get the "id" sourceApplication.
     *
     * @param id the id of the sourceApplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceApplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SourceApplication> getSourceApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SourceApplication : {}", id);
        Optional<SourceApplication> sourceApplication = sourceApplicationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sourceApplication);
    }

    /**
     * {@code DELETE  /source-applications/:id} : delete the "id" sourceApplication.
     *
     * @param id the id of the sourceApplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourceApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SourceApplication : {}", id);
        sourceApplicationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
