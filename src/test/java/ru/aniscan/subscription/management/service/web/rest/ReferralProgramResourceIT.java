package ru.aniscan.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aniscan.subscription.management.service.domain.ReferralProgramAsserts.*;
import static ru.aniscan.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;
import static ru.aniscan.subscription.management.service.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import ru.aniscan.subscription.management.service.domain.ReferralProgram;
import ru.aniscan.subscription.management.service.repository.ReferralProgramRepository;

/**
 * Integration tests for the {@link ReferralProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferralProgramResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REWARD_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REWARD_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/referral-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferralProgramRepository referralProgramRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferralProgramMockMvc;

    private ReferralProgram referralProgram;

    private ReferralProgram insertedReferralProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralProgram createEntity() {
        return new ReferralProgram().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).rewardAmount(DEFAULT_REWARD_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralProgram createUpdatedEntity() {
        return new ReferralProgram().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rewardAmount(UPDATED_REWARD_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        referralProgram = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReferralProgram != null) {
            referralProgramRepository.delete(insertedReferralProgram);
            insertedReferralProgram = null;
        }
    }

    @Test
    @Transactional
    void createReferralProgram() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReferralProgram
        var returnedReferralProgram = om.readValue(
            restReferralProgramMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgram)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReferralProgram.class
        );

        // Validate the ReferralProgram in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReferralProgramUpdatableFieldsEquals(returnedReferralProgram, getPersistedReferralProgram(returnedReferralProgram));

        insertedReferralProgram = returnedReferralProgram;
    }

    @Test
    @Transactional
    void createReferralProgramWithExistingId() throws Exception {
        // Create the ReferralProgram with an existing ID
        referralProgram.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferralProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgram)))
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgram.setName(null);

        // Create the ReferralProgram, which fails.

        restReferralProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgram)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRewardAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgram.setRewardAmount(null);

        // Create the ReferralProgram, which fails.

        restReferralProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgram)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferralPrograms() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        // Get all the referralProgramList
        restReferralProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referralProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rewardAmount").value(hasItem(sameNumber(DEFAULT_REWARD_AMOUNT))));
    }

    @Test
    @Transactional
    void getReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        // Get the referralProgram
        restReferralProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, referralProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referralProgram.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.rewardAmount").value(sameNumber(DEFAULT_REWARD_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingReferralProgram() throws Exception {
        // Get the referralProgram
        restReferralProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram
        ReferralProgram updatedReferralProgram = referralProgramRepository.findById(referralProgram.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReferralProgram are not directly saved in db
        em.detach(updatedReferralProgram);
        updatedReferralProgram.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rewardAmount(UPDATED_REWARD_AMOUNT);

        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferralProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReferralProgram))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferralProgramToMatchAllProperties(updatedReferralProgram);
    }

    @Test
    @Transactional
    void putNonExistingReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referralProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgram)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferralProgramWithPatch() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram using partial update
        ReferralProgram partialUpdatedReferralProgram = new ReferralProgram();
        partialUpdatedReferralProgram.setId(referralProgram.getId());

        partialUpdatedReferralProgram.name(UPDATED_NAME);

        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralProgram))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralProgramUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReferralProgram, referralProgram),
            getPersistedReferralProgram(referralProgram)
        );
    }

    @Test
    @Transactional
    void fullUpdateReferralProgramWithPatch() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram using partial update
        ReferralProgram partialUpdatedReferralProgram = new ReferralProgram();
        partialUpdatedReferralProgram.setId(referralProgram.getId());

        partialUpdatedReferralProgram.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rewardAmount(UPDATED_REWARD_AMOUNT);

        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralProgram))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralProgramUpdatableFieldsEquals(
            partialUpdatedReferralProgram,
            getPersistedReferralProgram(partialUpdatedReferralProgram)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referralProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(referralProgram)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgram = referralProgramRepository.saveAndFlush(referralProgram);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the referralProgram
        restReferralProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, referralProgram.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return referralProgramRepository.count();
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

    protected ReferralProgram getPersistedReferralProgram(ReferralProgram referralProgram) {
        return referralProgramRepository.findById(referralProgram.getId()).orElseThrow();
    }

    protected void assertPersistedReferralProgramToMatchAllProperties(ReferralProgram expectedReferralProgram) {
        assertReferralProgramAllPropertiesEquals(expectedReferralProgram, getPersistedReferralProgram(expectedReferralProgram));
    }

    protected void assertPersistedReferralProgramToMatchUpdatableProperties(ReferralProgram expectedReferralProgram) {
        assertReferralProgramAllUpdatablePropertiesEquals(expectedReferralProgram, getPersistedReferralProgram(expectedReferralProgram));
    }
}
