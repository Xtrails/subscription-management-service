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
import ru.aniscan.subscription.management.service.domain.ReferralProgram;
import ru.aniscan.subscription.management.service.repository.ReferralProgramRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.ReferralProgram}.
 */
@RestController
@RequestMapping("/api/referral-programs")
@Transactional
public class ReferralProgramResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralProgramResource.class);

    private static final String ENTITY_NAME = "referralProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferralProgramRepository referralProgramRepository;

    public ReferralProgramResource(ReferralProgramRepository referralProgramRepository) {
        this.referralProgramRepository = referralProgramRepository;
    }

    /**
     * {@code POST  /referral-programs} : Create a new referralProgram.
     *
     * @param referralProgram the referralProgram to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referralProgram, or with status {@code 400 (Bad Request)} if the referralProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferralProgram> createReferralProgram(@Valid @RequestBody ReferralProgram referralProgram)
        throws URISyntaxException {
        LOG.debug("REST request to save ReferralProgram : {}", referralProgram);
        if (referralProgram.getId() != null) {
            throw new BadRequestAlertException("A new referralProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referralProgram = referralProgramRepository.save(referralProgram);
        return ResponseEntity.created(new URI("/api/referral-programs/" + referralProgram.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referralProgram.getId().toString()))
            .body(referralProgram);
    }

    /**
     * {@code PUT  /referral-programs/:id} : Updates an existing referralProgram.
     *
     * @param id the id of the referralProgram to save.
     * @param referralProgram the referralProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgram,
     * or with status {@code 400 (Bad Request)} if the referralProgram is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referralProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferralProgram> updateReferralProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferralProgram referralProgram
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReferralProgram : {}, {}", id, referralProgram);
        if (referralProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referralProgram = referralProgramRepository.save(referralProgram);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgram.getId().toString()))
            .body(referralProgram);
    }

    /**
     * {@code PATCH  /referral-programs/:id} : Partial updates given fields of an existing referralProgram, field will ignore if it is null
     *
     * @param id the id of the referralProgram to save.
     * @param referralProgram the referralProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralProgram,
     * or with status {@code 400 (Bad Request)} if the referralProgram is not valid,
     * or with status {@code 404 (Not Found)} if the referralProgram is not found,
     * or with status {@code 500 (Internal Server Error)} if the referralProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferralProgram> partialUpdateReferralProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferralProgram referralProgram
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReferralProgram partially : {}, {}", id, referralProgram);
        if (referralProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferralProgram> result = referralProgramRepository
            .findById(referralProgram.getId())
            .map(existingReferralProgram -> {
                if (referralProgram.getName() != null) {
                    existingReferralProgram.setName(referralProgram.getName());
                }
                if (referralProgram.getDescription() != null) {
                    existingReferralProgram.setDescription(referralProgram.getDescription());
                }
                if (referralProgram.getRewardAmount() != null) {
                    existingReferralProgram.setRewardAmount(referralProgram.getRewardAmount());
                }

                return existingReferralProgram;
            })
            .map(referralProgramRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referralProgram.getId().toString())
        );
    }

    /**
     * {@code GET  /referral-programs} : get all the referralPrograms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referralPrograms in body.
     */
    @GetMapping("")
    public List<ReferralProgram> getAllReferralPrograms() {
        LOG.debug("REST request to get all ReferralPrograms");
        return referralProgramRepository.findAll();
    }

    /**
     * {@code GET  /referral-programs/:id} : get the "id" referralProgram.
     *
     * @param id the id of the referralProgram to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referralProgram, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferralProgram> getReferralProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReferralProgram : {}", id);
        Optional<ReferralProgram> referralProgram = referralProgramRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(referralProgram);
    }

    /**
     * {@code DELETE  /referral-programs/:id} : delete the "id" referralProgram.
     *
     * @param id the id of the referralProgram to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferralProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReferralProgram : {}", id);
        referralProgramRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
