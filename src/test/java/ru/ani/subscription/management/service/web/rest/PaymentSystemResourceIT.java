package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.PaymentSystemDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import ru.ani.subscription.management.service.domain.PaymentSystemDao;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDto;
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

    private PaymentSystemDao paymentSystemDao;

    private PaymentSystemDao insertedPaymentSystemDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSystemDao createEntity() {
        return new PaymentSystemDao().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSystemDao createUpdatedEntity() {
        return new PaymentSystemDao().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        paymentSystemDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentSystemDao != null) {
            paymentSystemRepository.delete(insertedPaymentSystemDao);
            insertedPaymentSystemDao = null;
        }
    }

    @Test
    @Transactional
    void createPaymentSystem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);
        var returnedPaymentSystemDto = om.readValue(
            restPaymentSystemMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(paymentSystemDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentSystemDto.class
        );

        // Validate the PaymentSystem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentSystemDao = paymentSystemMapper.toEntity(returnedPaymentSystemDto);
        assertPaymentSystemDaoUpdatableFieldsEquals(returnedPaymentSystemDao, getPersistedPaymentSystemDao(returnedPaymentSystemDao));

        insertedPaymentSystemDao = returnedPaymentSystemDao;
    }

    @Test
    @Transactional
    void createPaymentSystemWithExistingId() throws Exception {
        // Create the PaymentSystem with an existing ID
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentSystemMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDto))
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
        paymentSystemDao.setName(null);

        // Create the PaymentSystem, which fails.
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        restPaymentSystemMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentSystems() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        // Get all the paymentSystemList
        restPaymentSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentSystemDao.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        // Get the paymentSystem
        restPaymentSystemMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentSystemDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentSystemDao.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPaymentSystem() throws Exception {
        // Get the paymentSystem
        restPaymentSystemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem
        PaymentSystemDao updatedPaymentSystemDao = paymentSystemRepository.findById(paymentSystemDao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentSystemDao are not directly saved in db
        em.detach(updatedPaymentSystemDao);
        updatedPaymentSystemDao.name(UPDATED_NAME);
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(updatedPaymentSystemDao);

        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentSystemDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentSystemDaoToMatchAllProperties(updatedPaymentSystemDao);
    }

    @Test
    @Transactional
    void putNonExistingPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentSystemDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentSystemWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem using partial update
        PaymentSystemDao partialUpdatedPaymentSystemDao = new PaymentSystemDao();
        partialUpdatedPaymentSystemDao.setId(paymentSystemDao.getId());

        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSystemDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSystemDao))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSystemDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentSystemDao, paymentSystemDao),
            getPersistedPaymentSystemDao(paymentSystemDao)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentSystemWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSystem using partial update
        PaymentSystemDao partialUpdatedPaymentSystemDao = new PaymentSystemDao();
        partialUpdatedPaymentSystemDao.setId(paymentSystemDao.getId());

        partialUpdatedPaymentSystemDao.name(UPDATED_NAME);

        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSystemDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSystemDao))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSystemDaoUpdatableFieldsEquals(
            partialUpdatedPaymentSystemDao,
            getPersistedPaymentSystemDao(partialUpdatedPaymentSystemDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentSystemDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSystemDao.setId(UUID.randomUUID());

        // Create the PaymentSystem
        PaymentSystemDto paymentSystemDto = paymentSystemMapper.toDto(paymentSystemDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSystemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSystemDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentSystem() throws Exception {
        // Initialize the database
        insertedPaymentSystemDao = paymentSystemRepository.saveAndFlush(paymentSystemDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentSystem
        restPaymentSystemMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentSystemDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
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

    protected PaymentSystemDao getPersistedPaymentSystemDao(PaymentSystemDao paymentSystem) {
        return paymentSystemRepository.findById(paymentSystem.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentSystemDaoToMatchAllProperties(PaymentSystemDao expectedPaymentSystemDao) {
        assertPaymentSystemDaoAllPropertiesEquals(expectedPaymentSystemDao, getPersistedPaymentSystemDao(expectedPaymentSystemDao));
    }

    protected void assertPersistedPaymentSystemDaoToMatchUpdatableProperties(PaymentSystemDao expectedPaymentSystemDao) {
        assertPaymentSystemDaoAllUpdatablePropertiesEquals(
            expectedPaymentSystemDao,
            getPersistedPaymentSystemDao(expectedPaymentSystemDao)
        );
    }
}
