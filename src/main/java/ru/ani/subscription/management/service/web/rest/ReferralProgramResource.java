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
import ru.ani.subscription.management.service.repository.ReferralProgramRepository;
import ru.ani.subscription.management.service.service.ReferralProgramService;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDto;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.ReferralProgramDao}.
 */
@RestController
@RequestMapping("/api/referral-programs")
public class ReferralProgramResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralProgramResource.class);

    private static final String ENTITY_NAME = "referralProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferralProgramService referralProgramService;

    private final ReferralProgramRepository referralProgramRepository;

    public ReferralProgramResource(ReferralProgramService referralProgramService, ReferralProgramRepository referralProgramRepository) {
        this.referralProgramService = referralProgramService;
        this.referralProgramRepository = referralProgramRepository;
    }

    /**
     * {@code POST  /referral-programs} : Create a new referralProgram.
     *
     * @param referralProgramDto the referralProgramDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referralProgramDto, or with status {@code 400 (Bad Request)} if the referralProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferralProgramDto> createReferralProgram(@Valid @RequestBody ReferralProgramDto referralProgramDto)
        throws URISyntaxException {
        LOG.debug("REST request to save ReferralProgram : {}", referralProgramDto);
        if (referralProgramDto.getId() != null) {
            throw new BadRequestAlertException("A new referralProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referralProgramDto = referralProgramService.save(referralProgramDto);
        return ResponseEntity.created(new URI("/api/referral-programs/" + referralProgramDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referralProgramDto.getId().toString()))
            .body(referralProgramDto);
    }

    /**
     * {@code PUT  /referral-programs/:id} : Updates an existing referralProgram.
     *
     * @param id the id of the referralProgramDto to save.
     * @param referralProgramDto the referralProgramDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgramDto,
     * or with status {@code 400 (Bad Request)} if the referralProgramDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referralProgramDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferralProgramDto> updateReferralProgram(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ReferralProgramDto referralProgramDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReferralProgram : {}, {}", id, referralProgramDto);
        if (referralProgramDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgramDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referralProgramDto = referralProgramService.update(referralProgramDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgramDto.getId().toString()))
            .body(referralProgramDto);
    }

    /**
     * {@code PATCH  /referral-programs/:id} : Partial updates given fields of an existing referralProgram, field will ignore if it is null
     *
     * @param id the id of the referralProgramDto to save.
     * @param referralProgramDto the referralProgramDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgramDto,
     * or with status {@code 400 (Bad Request)} if the referralProgramDto is not valid,
     * or with status {@code 404 (Not Found)} if the referralProgramDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the referralProgramDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferralProgramDto> partialUpdateReferralProgram(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ReferralProgramDto referralProgramDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReferralProgram partially : {}, {}", id, referralProgramDto);
        if (referralProgramDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgramDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferralProgramDto> result = referralProgramService.partialUpdate(referralProgramDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgramDto.getId().toString())
        );
    }

    /**
     * {@code GET  /referral-programs} : get all the referralPrograms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referralPrograms in body.
     */
    @GetMapping("")
    public List<ReferralProgramDto> getAllReferralPrograms() {
        LOG.debug("REST request to get all ReferralPrograms");
        return referralProgramService.findAll();
    }

    /**
     * {@code GET  /referral-programs/:id} : get the "id" referralProgram.
     *
     * @param id the id of the referralProgramDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referralProgramDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferralProgramDto> getReferralProgram(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get ReferralProgram : {}", id);
        Optional<ReferralProgramDto> referralProgramDto = referralProgramService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referralProgramDto);
    }

    /**
     * {@code DELETE  /referral-programs/:id} : delete the "id" referralProgram.
     *
     * @param id the id of the referralProgramDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferralProgram(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete ReferralProgram : {}", id);
        referralProgramService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
