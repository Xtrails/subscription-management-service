package ru.aniscan.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aniscan.subscription.management.service.domain.SubscriptionTypeAsserts.*;
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
import ru.aniscan.subscription.management.service.domain.SubscriptionType;
import ru.aniscan.subscription.management.service.repository.SubscriptionTypeRepository;

/**
 * Integration tests for the {@link SubscriptionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final String ENTITY_API_URL = "/api/subscription-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionTypeMockMvc;

    private SubscriptionType subscriptionType;

    private SubscriptionType insertedSubscriptionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionType createEntity() {
        return new SubscriptionType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION)
            .visible(DEFAULT_VISIBLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionType createUpdatedEntity() {
        return new SubscriptionType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION)
            .visible(UPDATED_VISIBLE);
    }

    @BeforeEach
    public void initTest() {
        subscriptionType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubscriptionType != null) {
            subscriptionTypeRepository.delete(insertedSubscriptionType);
            insertedSubscriptionType = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionType
        var returnedSubscriptionType = om.readValue(
            restSubscriptionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionType.class
        );

        // Validate the SubscriptionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubscriptionTypeUpdatableFieldsEquals(returnedSubscriptionType, getPersistedSubscriptionType(returnedSubscriptionType));

        insertedSubscriptionType = returnedSubscriptionType;
    }

    @Test
    @Transactional
    void createSubscriptionTypeWithExistingId() throws Exception {
        // Create the SubscriptionType with an existing ID
        subscriptionType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionType.setName(null);

        // Create the SubscriptionType, which fails.

        restSubscriptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionType.setPrice(null);

        // Create the SubscriptionType, which fails.

        restSubscriptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionType.setDuration(null);

        // Create the SubscriptionType, which fails.

        restSubscriptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionType.setVisible(null);

        // Create the SubscriptionType, which fails.

        restSubscriptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionTypes() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        // Get all the subscriptionTypeList
        restSubscriptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE.booleanValue())));
    }

    @Test
    @Transactional
    void getSubscriptionType() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        // Get the subscriptionType
        restSubscriptionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionType() throws Exception {
        // Get the subscriptionType
        restSubscriptionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionType() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionType
        SubscriptionType updatedSubscriptionType = subscriptionTypeRepository.findById(subscriptionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionType are not directly saved in db
        em.detach(updatedSubscriptionType);
        updatedSubscriptionType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION)
            .visible(UPDATED_VISIBLE);

        restSubscriptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubscriptionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubscriptionType))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionTypeToMatchAllProperties(updatedSubscriptionType);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionType using partial update
        SubscriptionType partialUpdatedSubscriptionType = new SubscriptionType();
        partialUpdatedSubscriptionType.setId(subscriptionType.getId());

        partialUpdatedSubscriptionType.name(UPDATED_NAME).price(UPDATED_PRICE).duration(UPDATED_DURATION).visible(UPDATED_VISIBLE);

        restSubscriptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionType))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionType, subscriptionType),
            getPersistedSubscriptionType(subscriptionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionType using partial update
        SubscriptionType partialUpdatedSubscriptionType = new SubscriptionType();
        partialUpdatedSubscriptionType.setId(subscriptionType.getId());

        partialUpdatedSubscriptionType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .duration(UPDATED_DURATION)
            .visible(UPDATED_VISIBLE);

        restSubscriptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionType))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionTypeUpdatableFieldsEquals(
            partialUpdatedSubscriptionType,
            getPersistedSubscriptionType(partialUpdatedSubscriptionType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscriptionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionType() throws Exception {
        // Initialize the database
        insertedSubscriptionType = subscriptionTypeRepository.saveAndFlush(subscriptionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionType
        restSubscriptionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionTypeRepository.count();
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

    protected SubscriptionType getPersistedSubscriptionType(SubscriptionType subscriptionType) {
        return subscriptionTypeRepository.findById(subscriptionType.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionTypeToMatchAllProperties(SubscriptionType expectedSubscriptionType) {
        assertSubscriptionTypeAllPropertiesEquals(expectedSubscriptionType, getPersistedSubscriptionType(expectedSubscriptionType));
    }

    protected void assertPersistedSubscriptionTypeToMatchUpdatableProperties(SubscriptionType expectedSubscriptionType) {
        assertSubscriptionTypeAllUpdatablePropertiesEquals(
            expectedSubscriptionType,
            getPersistedSubscriptionType(expectedSubscriptionType)
        );
    }
}
