package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;
import static ru.ani.subscription.management.service.web.rest.TestUtil.sameNumber;

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
import ru.ani.subscription.management.service.IntegrationTest;
import ru.ani.subscription.management.service.domain.SubscriptionDetails;
import ru.ani.subscription.management.service.repository.SubscriptionDetailsRepository;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDTO;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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

    private SubscriptionDetails subscriptionDetails;

    private SubscriptionDetails insertedSubscriptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionDetails createEntity() {
        return new SubscriptionDetails()
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
    public static SubscriptionDetails createUpdatedEntity() {
        return new SubscriptionDetails()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION);
    }

    @BeforeEach
    void initTest() {
        subscriptionDetails = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubscriptionDetails != null) {
            subscriptionDetailsRepository.delete(insertedSubscriptionDetails);
            insertedSubscriptionDetails = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);
        var returnedSubscriptionDetailsDTO = om.readValue(
            restSubscriptionDetailsMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(subscriptionDetailsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionDetailsDTO.class
        );

        // Validate the SubscriptionDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscriptionDetails = subscriptionDetailsMapper.toEntity(returnedSubscriptionDetailsDTO);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            returnedSubscriptionDetails,
            getPersistedSubscriptionDetails(returnedSubscriptionDetails)
        );

        insertedSubscriptionDetails = returnedSubscriptionDetails;
    }

    @Test
    @Transactional
    void createSubscriptionDetailsWithExistingId() throws Exception {
        // Create the SubscriptionDetails with an existing ID
        subscriptionDetails.setId(1L);
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
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
        subscriptionDetails.setName(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setPrice(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionDetails.setDuration(null);

        // Create the SubscriptionDetails, which fails.
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        restSubscriptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        // Get all the subscriptionDetailsList
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    void getSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionDetails() throws Exception {
        // Get the subscriptionDetails
        restSubscriptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails
        SubscriptionDetails updatedSubscriptionDetails = subscriptionDetailsRepository.findById(subscriptionDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionDetails are not directly saved in db
        em.detach(updatedSubscriptionDetails);
        updatedSubscriptionDetails.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).duration(UPDATED_DURATION);
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(updatedSubscriptionDetails);

        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionDetailsToMatchAllProperties(updatedSubscriptionDetails);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetails partialUpdatedSubscriptionDetails = new SubscriptionDetails();
        partialUpdatedSubscriptionDetails.setId(subscriptionDetails.getId());

        partialUpdatedSubscriptionDetails.name(UPDATED_NAME);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionDetails, subscriptionDetails),
            getPersistedSubscriptionDetails(subscriptionDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionDetails using partial update
        SubscriptionDetails partialUpdatedSubscriptionDetails = new SubscriptionDetails();
        partialUpdatedSubscriptionDetails.setId(subscriptionDetails.getId());

        partialUpdatedSubscriptionDetails
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION);

        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionDetailsUpdatableFieldsEquals(
            partialUpdatedSubscriptionDetails,
            getPersistedSubscriptionDetails(partialUpdatedSubscriptionDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionDetailsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionDetails.setId(longCount.incrementAndGet());

        // Create the SubscriptionDetails
        SubscriptionDetailsDTO subscriptionDetailsDTO = subscriptionDetailsMapper.toDto(subscriptionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionDetails() throws Exception {
        // Initialize the database
        insertedSubscriptionDetails = subscriptionDetailsRepository.saveAndFlush(subscriptionDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionDetails
        restSubscriptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionDetails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
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

    protected SubscriptionDetails getPersistedSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
        return subscriptionDetailsRepository.findById(subscriptionDetails.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionDetailsToMatchAllProperties(SubscriptionDetails expectedSubscriptionDetails) {
        assertSubscriptionDetailsAllPropertiesEquals(
            expectedSubscriptionDetails,
            getPersistedSubscriptionDetails(expectedSubscriptionDetails)
        );
    }

    protected void assertPersistedSubscriptionDetailsToMatchUpdatableProperties(SubscriptionDetails expectedSubscriptionDetails) {
        assertSubscriptionDetailsAllUpdatablePropertiesEquals(
            expectedSubscriptionDetails,
            getPersistedSubscriptionDetails(expectedSubscriptionDetails)
        );
    }
}
