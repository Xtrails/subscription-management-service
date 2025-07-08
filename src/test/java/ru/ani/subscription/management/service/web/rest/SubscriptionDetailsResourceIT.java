package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;
import static ru.ani.subscription.management.service.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.repository.SubscriptionDetailsRepository;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;
import ru.ani.subscription.management.service.service.mapper.SubscriptionDetailsMapper;

/**
 * Integration tests for the {@link SubscriptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String ENTITY_API_URL = "/api/subscription-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionDetailsRepository subscriptionDetailsRepository;

    @Autowired
    private SubscriptionDetailsMapper subscriptionDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionDetailsMockMvc;

    private SubscriptionDetailsDao subscriptionDetailsDao;

    private SubscriptionDetailsDao insertedSubscriptionDetailsDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDetailsDao createEntity() {
        return new SubscriptionDetailsDao()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDetailsDao createUpdatedEntity() {
        return new SubscriptionDetailsDao()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION);
    }

    @BeforeEach
    void initTest() {
        subscriptionDetailsDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubscriptionDetailsDao != null) {
            subscriptionDetailsRepository.delete(insertedSubscriptionDetailsDao);
            insertedSubscriptionDetailsDao = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);
        var returnedSubscriptionDetailsDto = om.readValue(
            restSubscriptionDetailsMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(subscriptionDetailsDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionDetailsDto.class
        );

        // Validate the SubscriptionDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscriptionDetailsDao = subscriptionDetailsMapper.toEntity(returnedSubscriptionDetailsDto);
        assertSubscriptionDetailsDaoUpdatableFieldsEquals(
            returnedSubscriptionDetailsDao,
            getPersistedSubscriptionDetailsDao(returnedSubscriptionDetailsDao)
        );

        insertedSubscriptionDetailsDao = returnedSubscriptionDetailsDao;
    }

    @Test
    @Transactional
    void createSubscriptionDetailsWithExistingId() throws Exception {
        // Create the SubscriptionDetails with an existing ID
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetailsDao.setName(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetailsDao.setPrice(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetailsDao.setDuration(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        // Get all the subscriptionDetailsList
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionDetailsDao.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    void getSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionDetailsDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionDetailsDao.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionDetails() throws Exception {
        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails
        SubscriptionDetailsDao updatedSubscriptionDetailsDao = subscriptionDetailsRepository
            .findById(subscriptionDetailsDao.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionDetailsDao are not directly saved in db
        em.detach(updatedSubscriptionDetailsDao);
        updatedSubscriptionDetailsDao.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).duration(UPDATED_DURATION);
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(updatedSubscriptionDetailsDao);

        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionDetailsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionDetailsDaoToMatchAllProperties(updatedSubscriptionDetailsDao);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionDetailsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetailsDao partialUpdatedSubscriptionDetailsDao = new SubscriptionDetailsDao();
        partialUpdatedSubscriptionDetailsDao.setId(subscriptionDetailsDao.getId());

        partialUpdatedSubscriptionDetailsDao.name(UPDATED_NAME);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetailsDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetailsDao))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionDetailsDao, subscriptionDetailsDao),
            getPersistedSubscriptionDetailsDao(subscriptionDetailsDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetailsDao partialUpdatedSubscriptionDetailsDao = new SubscriptionDetailsDao();
        partialUpdatedSubscriptionDetailsDao.setId(subscriptionDetailsDao.getId());

        partialUpdatedSubscriptionDetailsDao
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetailsDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetailsDao))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsDaoUpdatableFieldsEquals(
            partialUpdatedSubscriptionDetailsDao,
            getPersistedSubscriptionDetailsDao(partialUpdatedSubscriptionDetailsDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionDetailsDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetailsDao.setId(UUID.randomUUID());

        // Create the SubscriptionDetails
        SubscriptionDetailsDto subscriptionDetailsDto = subscriptionDetailsMapper.toDto(subscriptionDetailsDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetailsDao = subscriptionDetailsRepository.saveAndFlush(subscriptionDetailsDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionDetailsDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionDetailsRepository.count();
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

    protected SubscriptionDetailsDao getPersistedSubscriptionDetailsDao(SubscriptionDetailsDao subscriptionDetails) {
        return subscriptionDetailsRepository.findById(subscriptionDetails.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionDetailsDaoToMatchAllProperties(SubscriptionDetailsDao expectedSubscriptionDetailsDao) {
        assertSubscriptionDetailsDaoAllPropertiesEquals(
            expectedSubscriptionDetailsDao,
            getPersistedSubscriptionDetailsDao(expectedSubscriptionDetailsDao)
        );
    }

    protected void assertPersistedSubscriptionDetailsDaoToMatchUpdatableProperties(SubscriptionDetailsDao expectedSubscriptionDetailsDao) {
        assertSubscriptionDetailsDaoAllUpdatablePropertiesEquals(
            expectedSubscriptionDetailsDao,
            getPersistedSubscriptionDetailsDao(expectedSubscriptionDetailsDao)
        );
    }
}
