package ru.aniscan.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aniscan.subscription.management.service.domain.ReferralAsserts.*;
import static ru.aniscan.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.aniscan.subscription.management.service.IntegrationTest;
import ru.aniscan.subscription.management.service.domain.Referral;
import ru.aniscan.subscription.management.service.domain.enumeration.ReferralStatus;
import ru.aniscan.subscription.management.service.repository.ReferralRepository;

/**
 * Integration tests for the {@link ReferralResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferralResourceIT {

    private static final String DEFAULT_REFERRAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERRAL_CODE = "BBBBBBBBBB";

    private static final ReferralStatus DEFAULT_STATUS = ReferralStatus.PENDING;
    private static final ReferralStatus UPDATED_STATUS = ReferralStatus.COMPLETED;

    private static final String ENTITY_API_URL = "/api/referrals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferralMockMvc;

    private Referral referral;

    private Referral insertedReferral;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referral createEntity() {
        return new Referral().referralCode(DEFAULT_REFERRAL_CODE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referral createUpdatedEntity() {
        return new Referral().referralCode(UPDATED_REFERRAL_CODE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        referral = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReferral != null) {
            referralRepository.delete(insertedReferral);
            insertedReferral = null;
        }
    }

    @Test
    @Transactional
    void createReferral() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Referral
        var returnedReferral = om.readValue(
            restReferralMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Referral.class
        );

        // Validate the Referral in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReferralUpdatableFieldsEquals(returnedReferral, getPersistedReferral(returnedReferral));

        insertedReferral = returnedReferral;
    }

    @Test
    @Transactional
    void createReferralWithExistingId() throws Exception {
        // Create the Referral with an existing ID
        referral.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral)))
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferralCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referral.setReferralCode(null);

        // Create the Referral, which fails.

        restReferralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referral.setStatus(null);

        // Create the Referral, which fails.

        restReferralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferrals() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        // Get all the referralList
        restReferralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referral.getId().intValue())))
            .andExpect(jsonPath("$.[*].referralCode").value(hasItem(DEFAULT_REFERRAL_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getReferral() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        // Get the referral
        restReferralMockMvc
            .perform(get(ENTITY_API_URL_ID, referral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referral.getId().intValue()))
            .andExpect(jsonPath("$.referralCode").value(DEFAULT_REFERRAL_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReferral() throws Exception {
        // Get the referral
        restReferralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferral() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referral
        Referral updatedReferral = referralRepository.findById(referral.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReferral are not directly saved in db
        em.detach(updatedReferral);
        updatedReferral.referralCode(UPDATED_REFERRAL_CODE).status(UPDATED_STATUS);

        restReferralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferral.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReferral))
            )
            .andExpect(status().isOk());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferralToMatchAllProperties(updatedReferral);
    }

    @Test
    @Transactional
    void putNonExistingReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referral.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referral))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referral)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferralWithPatch() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referral using partial update
        Referral partialUpdatedReferral = new Referral();
        partialUpdatedReferral.setId(referral.getId());

        partialUpdatedReferral.referralCode(UPDATED_REFERRAL_CODE).status(UPDATED_STATUS);

        restReferralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferral))
            )
            .andExpect(status().isOk());

        // Validate the Referral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReferral, referral), getPersistedReferral(referral));
    }

    @Test
    @Transactional
    void fullUpdateReferralWithPatch() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referral using partial update
        Referral partialUpdatedReferral = new Referral();
        partialUpdatedReferral.setId(referral.getId());

        partialUpdatedReferral.referralCode(UPDATED_REFERRAL_CODE).status(UPDATED_STATUS);

        restReferralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferral))
            )
            .andExpect(status().isOk());

        // Validate the Referral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralUpdatableFieldsEquals(partialUpdatedReferral, getPersistedReferral(partialUpdatedReferral));
    }

    @Test
    @Transactional
    void patchNonExistingReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referral))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referral))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referral.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(referral)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferral() throws Exception {
        // Initialize the database
        insertedReferral = referralRepository.saveAndFlush(referral);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the referral
        restReferralMockMvc
            .perform(delete(ENTITY_API_URL_ID, referral.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return referralRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Referral getPersistedReferral(Referral referral) {
        return referralRepository.findById(referral.getId()).orElseThrow();
    }

    protected void assertPersistedReferralToMatchAllProperties(Referral expectedReferral) {
        assertReferralAllPropertiesEquals(expectedReferral, getPersistedReferral(expectedReferral));
    }

    protected void assertPersistedReferralToMatchUpdatableProperties(Referral expectedReferral) {
        assertReferralAllUpdatablePropertiesEquals(expectedReferral, getPersistedReferral(expectedReferral));
    }
}
