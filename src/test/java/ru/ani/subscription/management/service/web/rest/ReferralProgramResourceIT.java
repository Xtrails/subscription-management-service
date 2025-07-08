package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;
import static ru.ani.subscription.management.service.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.IntegrationTest;
import ru.ani.subscription.management.service.domain.ReferralProgramDao;
import ru.ani.subscription.management.service.domain.enumeration.ReferralStatus;
import ru.ani.subscription.management.service.repository.ReferralProgramRepository;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDto;
import ru.ani.subscription.management.service.service.mapper.ReferralProgramMapper;

/**
 * Integration tests for the {@link ReferralProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferralProgramResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REFERRAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERRAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DTTM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DTTM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DTTM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DTTM = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_REWARD_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REWARD_AMOUNT = new BigDecimal(2);

    private static final ReferralStatus DEFAULT_STATUS = ReferralStatus.PENDING;
    private static final ReferralStatus UPDATED_STATUS = ReferralStatus.COMPLETED;

    private static final String ENTITY_API_URL = "/api/referral-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferralProgramRepository referralProgramRepository;

    @Autowired
    private ReferralProgramMapper referralProgramMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferralProgramMockMvc;

    private ReferralProgramDao referralProgramDao;

    private ReferralProgramDao insertedReferralProgramDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralProgramDao createEntity() {
        return new ReferralProgramDao()
            .name(DEFAULT_NAME)
            .referralCode(DEFAULT_REFERRAL_CODE)
            .description(DEFAULT_DESCRIPTION)
            .startDttm(DEFAULT_START_DTTM)
            .endDttm(DEFAULT_END_DTTM)
            .rewardAmount(DEFAULT_REWARD_AMOUNT)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralProgramDao createUpdatedEntity() {
        return new ReferralProgramDao()
            .name(UPDATED_NAME)
            .referralCode(UPDATED_REFERRAL_CODE)
            .description(UPDATED_DESCRIPTION)
            .startDttm(UPDATED_START_DTTM)
            .endDttm(UPDATED_END_DTTM)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        referralProgramDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReferralProgramDao != null) {
            referralProgramRepository.delete(insertedReferralProgramDao);
            insertedReferralProgramDao = null;
        }
    }

    @Test
    @Transactional
    void createReferralProgram() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);
        var returnedReferralProgramDto = om.readValue(
            restReferralProgramMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(referralProgramDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReferralProgramDto.class
        );

        // Validate the ReferralProgram in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReferralProgramDao = referralProgramMapper.toEntity(returnedReferralProgramDto);
        assertReferralProgramDaoUpdatableFieldsEquals(
            returnedReferralProgramDao,
            getPersistedReferralProgramDao(returnedReferralProgramDao)
        );

        insertedReferralProgramDao = returnedReferralProgramDao;
    }

    @Test
    @Transactional
    void createReferralProgramWithExistingId() throws Exception {
        // Create the ReferralProgram with an existing ID
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setName(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferralCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setReferralCode(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setStartDttm(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setEndDttm(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRewardAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setRewardAmount(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralProgramDao.setStatus(null);

        // Create the ReferralProgram, which fails.
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        restReferralProgramMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferralPrograms() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        // Get all the referralProgramList
        restReferralProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referralProgramDao.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].referralCode").value(hasItem(DEFAULT_REFERRAL_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDttm").value(hasItem(DEFAULT_START_DTTM.toString())))
            .andExpect(jsonPath("$.[*].endDttm").value(hasItem(DEFAULT_END_DTTM.toString())))
            .andExpect(jsonPath("$.[*].rewardAmount").value(hasItem(sameNumber(DEFAULT_REWARD_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        // Get the referralProgram
        restReferralProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, referralProgramDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referralProgramDao.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.referralCode").value(DEFAULT_REFERRAL_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDttm").value(DEFAULT_START_DTTM.toString()))
            .andExpect(jsonPath("$.endDttm").value(DEFAULT_END_DTTM.toString()))
            .andExpect(jsonPath("$.rewardAmount").value(sameNumber(DEFAULT_REWARD_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReferralProgram() throws Exception {
        // Get the referralProgram
        restReferralProgramMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram
        ReferralProgramDao updatedReferralProgramDao = referralProgramRepository.findById(referralProgramDao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReferralProgramDao are not directly saved in db
        em.detach(updatedReferralProgramDao);
        updatedReferralProgramDao
            .name(UPDATED_NAME)
            .referralCode(UPDATED_REFERRAL_CODE)
            .description(UPDATED_DESCRIPTION)
            .startDttm(UPDATED_START_DTTM)
            .endDttm(UPDATED_END_DTTM)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .status(UPDATED_STATUS);
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(updatedReferralProgramDao);

        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referralProgramDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferralProgramDaoToMatchAllProperties(updatedReferralProgramDao);
    }

    @Test
    @Transactional
    void putNonExistingReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referralProgramDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferralProgramWithPatch() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram using partial update
        ReferralProgramDao partialUpdatedReferralProgramDao = new ReferralProgramDao();
        partialUpdatedReferralProgramDao.setId(referralProgramDao.getId());

        partialUpdatedReferralProgramDao.referralCode(UPDATED_REFERRAL_CODE);

        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralProgramDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralProgramDao))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralProgramDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReferralProgramDao, referralProgramDao),
            getPersistedReferralProgramDao(referralProgramDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateReferralProgramWithPatch() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralProgram using partial update
        ReferralProgramDao partialUpdatedReferralProgramDao = new ReferralProgramDao();
        partialUpdatedReferralProgramDao.setId(referralProgramDao.getId());

        partialUpdatedReferralProgramDao
            .name(UPDATED_NAME)
            .referralCode(UPDATED_REFERRAL_CODE)
            .description(UPDATED_DESCRIPTION)
            .startDttm(UPDATED_START_DTTM)
            .endDttm(UPDATED_END_DTTM)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .status(UPDATED_STATUS);

        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralProgramDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralProgramDao))
            )
            .andExpect(status().isOk());

        // Validate the ReferralProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralProgramDaoUpdatableFieldsEquals(
            partialUpdatedReferralProgramDao,
            getPersistedReferralProgramDao(partialUpdatedReferralProgramDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referralProgramDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferralProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralProgramDao.setId(UUID.randomUUID());

        // Create the ReferralProgram
        ReferralProgramDto referralProgramDto = referralProgramMapper.toDto(referralProgramDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralProgramMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralProgramDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferralProgram() throws Exception {
        // Initialize the database
        insertedReferralProgramDao = referralProgramRepository.saveAndFlush(referralProgramDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the referralProgram
        restReferralProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, referralProgramDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
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

    protected ReferralProgramDao getPersistedReferralProgramDao(ReferralProgramDao referralProgram) {
        return referralProgramRepository.findById(referralProgram.getId()).orElseThrow();
    }

    protected void assertPersistedReferralProgramDaoToMatchAllProperties(ReferralProgramDao expectedReferralProgramDao) {
        assertReferralProgramDaoAllPropertiesEquals(expectedReferralProgramDao, getPersistedReferralProgramDao(expectedReferralProgramDao));
    }

    protected void assertPersistedReferralProgramDaoToMatchUpdatableProperties(ReferralProgramDao expectedReferralProgramDao) {
        assertReferralProgramDaoAllUpdatablePropertiesEquals(
            expectedReferralProgramDao,
            getPersistedReferralProgramDao(expectedReferralProgramDao)
        );
    }
}
