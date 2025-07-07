package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.PaymentSystemAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

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
import ru.ani.subscription.management.service.IntegrationTest;
import ru.ani.subscription.management.service.domain.PaymentSystem;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDTO;
import ru.ani.subscription.management.service.service.mapper.PaymentSystemMapper;

/**
 * Integration tests for the {@link PaymentSystemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentSystemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-systems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentSystemRepository paymentSystemRepository;

    @Autowired
    private PaymentSystemMapper paymentSystemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentSystemMockMvc;

    private PaymentSystem paymentSystem;

    private PaymentSystem insertedPaymentSystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSystem createEntity() {
        return new PaymentSystem().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSystem createUpdatedEntity() {
        return new PaymentSystem().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        paymentSystem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentSystem != null) {
            paymentSystemRepository.delete(insertedPaymentSystem);
            insertedPaymentSystem = null;
        }
    }

    @Test
    @Transactional
    void createPaymentSystem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);
        var returnedPaymentSystemDTO = om.readValue(
            restPaymentSystemMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(paymentSystemDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentSystemDTO.class
        );

        // Validate the PaymentSystem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentSystem = paymentSystemMapper.toEntity(returnedPaymentSystemDTO);
        assertPaymentSystemUpdatableFieldsEquals(returnedPaymentSystem, getPersistedPaymentSystem(returnedPaymentSystem));

        insertedPaymentSystem = returnedPaymentSystem;
    }

    @Test
    @Transactional
    void createPaymentSystemWithExistingId() throws Exception {
        // Create the PaymentSystem with an existing ID
        paymentSystem.setId(1L);
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentSystemMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentSystem.setName(null);

        // Create the PaymentSystem, which fails.
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        restPaymentSystemMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentSystems() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        // Get all the paymentSystemList
        restPaymentSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        // Get the paymentSystem
        restPaymentSystemMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentSystem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPaymentSystem() throws Exception {
        // Get the paymentSystem
        restPaymentSystemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem
        PaymentSystem updatedPaymentSystem = paymentSystemRepository.findById(paymentSystem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentSystem are not directly saved in db
        em.detach(updatedPaymentSystem);
        updatedPaymentSystem.name(UPDATED_NAME);
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(updatedPaymentSystem);

        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentSystemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentSystemToMatchAllProperties(updatedPaymentSystem);
    }

    @Test
    @Transactional
    void putNonExistingPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentSystemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentSystemWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem using partial update
        PaymentSystem partialUpdatedPaymentSystem = new PaymentSystem();
        partialUpdatedPaymentSystem.setId(paymentSystem.getId());

        partialUpdatedPaymentSystem.name(UPDATED_NAME);

        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSystem))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSystemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentSystem, paymentSystem),
            getPersistedPaymentSystem(paymentSystem)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentSystemWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem using partial update
        PaymentSystem partialUpdatedPaymentSystem = new PaymentSystem();
        partialUpdatedPaymentSystem.setId(paymentSystem.getId());

        partialUpdatedPaymentSystem.name(UPDATED_NAME);

        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSystem))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSystemUpdatableFieldsEquals(partialUpdatedPaymentSystem, getPersistedPaymentSystem(partialUpdatedPaymentSystem));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentSystemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystem.setId(longCount.incrementAndGet());

        // Create the PaymentSystem
        PaymentSystemDTO paymentSystemDTO = paymentSystemMapper.toDto(paymentSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystem = paymentSystemRepository.saveAndFlush(paymentSystem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentSystem
        restPaymentSystemMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentSystem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentSystemRepository.count();
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

    protected PaymentSystem getPersistedPaymentSystem(PaymentSystem paymentSystem) {
        return paymentSystemRepository.findById(paymentSystem.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentSystemToMatchAllProperties(PaymentSystem expectedPaymentSystem) {
        assertPaymentSystemAllPropertiesEquals(expectedPaymentSystem, getPersistedPaymentSystem(expectedPaymentSystem));
    }

    protected void assertPersistedPaymentSystemToMatchUpdatableProperties(PaymentSystem expectedPaymentSystem) {
        assertPaymentSystemAllUpdatablePropertiesEquals(expectedPaymentSystem, getPersistedPaymentSystem(expectedPaymentSystem));
    }
}
