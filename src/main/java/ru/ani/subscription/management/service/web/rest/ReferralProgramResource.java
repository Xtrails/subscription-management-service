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
import ru.ani.subscription.management.service.repository.ReferralProgramRepository;
import ru.ani.subscription.management.service.service.ReferralProgramService;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDTO;
import ru.ani.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ani.subscription.management.service.domain.ReferralProgram}.
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
     * @param referralProgramDTO the referralProgramDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referralProgramDTO, or with status {@code 400 (Bad Request)} if the referralProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferralProgramDTO> createReferralProgram(@Valid @RequestBody ReferralProgramDTO referralProgramDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReferralProgram : {}", referralProgramDTO);
        if (referralProgramDTO.getId() != null) {
            throw new BadRequestAlertException("A new referralProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referralProgramDTO = referralProgramService.save(referralProgramDTO);
        return ResponseEntity.created(new URI("/api/referral-programs/" + referralProgramDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referralProgramDTO.getId().toString()))
            .body(referralProgramDTO);
    }

    /**
     * {@code PUT  /referral-programs/:id} : Updates an existing referralProgram.
     *
     * @param id the id of the referralProgramDTO to save.
     * @param referralProgramDTO the referralProgramDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgramDTO,
     * or with status {@code 400 (Bad Request)} if the referralProgramDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referralProgramDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferralProgramDTO> updateReferralProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferralProgramDTO referralProgramDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReferralProgram : {}, {}", id, referralProgramDTO);
        if (referralProgramDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgramDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referralProgramDTO = referralProgramService.update(referralProgramDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgramDTO.getId().toString()))
            .body(referralProgramDTO);
    }

    /**
     * {@code PATCH  /referral-programs/:id} : Partial updates given fields of an existing referralProgram, field will ignore if it is null
     *
     * @param id the id of the referralProgramDTO to save.
     * @param referralProgramDTO the referralProgramDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgramDTO,
     * or with status {@code 400 (Bad Request)} if the referralProgramDTO is not valid,
     * or with status {@code 404 (Not Found)} if the referralProgramDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the referralProgramDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferralProgramDTO> partialUpdateReferralProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferralProgramDTO referralProgramDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReferralProgram partially : {}, {}", id, referralProgramDTO);
        if (referralProgramDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgramDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferralProgramDTO> result = referralProgramService.partialUpdate(referralProgramDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgramDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /referral-programs} : get all the referralPrograms.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referralPrograms in body.
     */
    @GetMapping("")
    public List<ReferralProgramDTO> getAllReferralPrograms(@RequestParam(name = "filter", required = false) String filter) {
        if ("externaluser-is-null".equals(filter)) {
            LOG.debug("REST request to get all ReferralPrograms where externalUser is null");
            return referralProgramService.findAllWhereExternalUserIsNull();
        }
        LOG.debug("REST request to get all ReferralPrograms");
        return referralProgramService.findAll();
    }

    /**
     * {@code GET  /referral-programs/:id} : get the "id" referralProgram.
     *
     * @param id the id of the referralProgramDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referralProgramDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferralProgramDTO> getReferralProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReferralProgram : {}", id);
        Optional<ReferralProgramDTO> referralProgramDTO = referralProgramService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referralProgramDTO);
    }

    /**
     * {@code DELETE  /referral-programs/:id} : delete the "id" referralProgram.
     *
     * @param id the id of the referralProgramDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferralProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReferralProgram : {}", id);
        referralProgramService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
