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
import ru.aniscan.subscription.management.service.domain.Referral;
import ru.aniscan.subscription.management.service.repository.ReferralRepository;
import ru.aniscan.subscription.management.service.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.aniscan.subscription.management.service.domain.Referral}.
 */
@RestController
@RequestMapping("/api/referrals")
@Transactional
public class ReferralResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralResource.class);

    private static final String ENTITY_NAME = "referral";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferralRepository referralRepository;

    public ReferralResource(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    /**
     * {@code POST  /referrals} : Create a new referral.
     *
     * @param referral the referral to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referral, or with status {@code 400 (Bad Request)} if the referral has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Referral> createReferral(@Valid @RequestBody Referral referral) throws URISyntaxException {
        LOG.debug("REST request to save Referral : {}", referral);
        if (referral.getId() != null) {
            throw new BadRequestAlertException("A new referral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referral = referralRepository.save(referral);
        return ResponseEntity.created(new URI("/api/referrals/" + referral.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referral.getId().toString()))
            .body(referral);
    }

    /**
     * {@code PUT  /referrals/:id} : Updates an existing referral.
     *
     * @param id the id of the referral to save.
     * @param referral the referral to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referral,
     * or with status {@code 400 (Bad Request)} if the referral is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referral couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Referral> updateReferral(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Referral referral
    ) throws URISyntaxException {
        LOG.debug("REST request to update Referral : {}, {}", id, referral);
        if (referral.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referral.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referral = referralRepository.save(referral);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referral.getId().toString()))
            .body(referral);
    }

    /**
     * {@code PATCH  /referrals/:id} : Partial updates given fields of an existing referral, field will ignore if it is null
     *
     * @param id the id of the referral to save.
     * @param referral the referral to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referral,
     * or with status {@code 400 (Bad Request)} if the referral is not valid,
     * or with status {@code 404 (Not Found)} if the referral is not found,
     * or with status {@code 500 (Internal Server Error)} if the referral couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Referral> partialUpdateReferral(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Referral referral
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Referral partially : {}, {}", id, referral);
        if (referral.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referral.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Referral> result = referralRepository
            .findById(referral.getId())
            .map(existingReferral -> {
                if (referral.getReferralCode() != null) {
                    existingReferral.setReferralCode(referral.getReferralCode());
                }
                if (referral.getStatus() != null) {
                    existingReferral.setStatus(referral.getStatus());
                }

                return existingReferral;
            })
            .map(referralRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referral.getId().toString())
        );
    }

    /**
     * {@code GET  /referrals} : get all the referrals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referrals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Referral>> getAllReferrals(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Referrals");
        Page<Referral> page = referralRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referrals/:id} : get the "id" referral.
     *
     * @param id the id of the referral to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referral, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Referral> getReferral(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Referral : {}", id);
        Optional<Referral> referral = referralRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(referral);
    }

    /**
     * {@code DELETE  /referrals/:id} : delete the "id" referral.
     *
     * @param id the id of the referral to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferral(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Referral : {}", id);
        referralRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
