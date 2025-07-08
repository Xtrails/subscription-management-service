package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import ru.ani.subscription.management.service.domain.ClientSubscriptionDao;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;
import ru.ani.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDto;
import ru.ani.subscription.management.service.service.mapper.ClientSubscriptionMapper;

/**
 * Integration tests for the {@link ClientSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientSubscriptionResourceIT {

    private static final LocalDate DEFAULT_START_DTTM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DTTM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DTTM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DTTM = LocalDate.now(ZoneId.systemDefault());

    private static final SubscriptionStatus DEFAULT_STATUS = SubscriptionStatus.ACTIVE;
    private static final SubscriptionStatus UPDATED_STATUS = SubscriptionStatus.EXPIRED;

    private static final String ENTITY_API_URL = "/api/client-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientSubscriptionRepository clientSubscriptionRepository;

    @Autowired
    private ClientSubscriptionMapper clientSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientSubscriptionMockMvc;

    private ClientSubscriptionDao clientSubscriptionDao;

    private ClientSubscriptionDao insertedClientSubscriptionDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientSubscriptionDao createEntity() {
        return new ClientSubscriptionDao().startDttm(DEFAULT_START_DTTM).endDttm(DEFAULT_END_DTTM).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientSubscriptionDao createUpdatedEntity() {
        return new ClientSubscriptionDao().startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        clientSubscriptionDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientSubscriptionDao != null) {
            clientSubscriptionRepository.delete(insertedClientSubscriptionDao);
            insertedClientSubscriptionDao = null;
        }
    }

    @Test
    @Transactional
    void createClientSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);
        var returnedClientSubscriptionDto = om.readValue(
            restClientSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(clientSubscriptionDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientSubscriptionDto.class
        );

        // Validate the ClientSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientSubscriptionDao = clientSubscriptionMapper.toEntity(returnedClientSubscriptionDto);
        assertClientSubscriptionDaoUpdatableFieldsEquals(
            returnedClientSubscriptionDao,
            getPersistedClientSubscriptionDao(returnedClientSubscriptionDao)
        );

        insertedClientSubscriptionDao = returnedClientSubscriptionDao;
    }

    @Test
    @Transactional
    void createClientSubscriptionWithExistingId() throws Exception {
        // Create the ClientSubscription with an existing ID
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientSubscriptionDao.setStartDttm(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientSubscriptionDao.setEndDttm(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientSubscriptionDao.setStatus(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientSubscriptions() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        // Get all the clientSubscriptionList
        restClientSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientSubscriptionDao.getId().toString())))
            .andExpect(jsonPath("$.[*].startDttm").value(hasItem(DEFAULT_START_DTTM.toString())))
            .andExpect(jsonPath("$.[*].endDttm").value(hasItem(DEFAULT_END_DTTM.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        // Get the clientSubscription
        restClientSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, clientSubscriptionDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientSubscriptionDao.getId().toString()))
            .andExpect(jsonPath("$.startDttm").value(DEFAULT_START_DTTM.toString()))
            .andExpect(jsonPath("$.endDttm").value(DEFAULT_END_DTTM.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClientSubscription() throws Exception {
        // Get the clientSubscription
        restClientSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription
        ClientSubscriptionDao updatedClientSubscriptionDao = clientSubscriptionRepository
            .findById(clientSubscriptionDao.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedClientSubscriptionDao are not directly saved in db
        em.detach(updatedClientSubscriptionDao);
        updatedClientSubscriptionDao.startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(updatedClientSubscriptionDao);

        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientSubscriptionDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientSubscriptionDaoToMatchAllProperties(updatedClientSubscriptionDao);
    }

    @Test
    @Transactional
    void putNonExistingClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientSubscriptionDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription using partial update
        ClientSubscriptionDao partialUpdatedClientSubscriptionDao = new ClientSubscriptionDao();
        partialUpdatedClientSubscriptionDao.setId(clientSubscriptionDao.getId());

        partialUpdatedClientSubscriptionDao.endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);

        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientSubscriptionDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientSubscriptionDao))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientSubscriptionDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientSubscriptionDao, clientSubscriptionDao),
            getPersistedClientSubscriptionDao(clientSubscriptionDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription using partial update
        ClientSubscriptionDao partialUpdatedClientSubscriptionDao = new ClientSubscriptionDao();
        partialUpdatedClientSubscriptionDao.setId(clientSubscriptionDao.getId());

        partialUpdatedClientSubscriptionDao.startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);

        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientSubscriptionDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientSubscriptionDao))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientSubscriptionDaoUpdatableFieldsEquals(
            partialUpdatedClientSubscriptionDao,
            getPersistedClientSubscriptionDao(partialUpdatedClientSubscriptionDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientSubscriptionDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscriptionDao.setId(UUID.randomUUID());

        // Create the ClientSubscription
        ClientSubscriptionDto clientSubscriptionDto = clientSubscriptionMapper.toDto(clientSubscriptionDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscriptionDao = clientSubscriptionRepository.saveAndFlush(clientSubscriptionDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clientSubscription
        restClientSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientSubscriptionDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientSubscriptionRepository.count();
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

    protected ClientSubscriptionDao getPersistedClientSubscriptionDao(ClientSubscriptionDao clientSubscription) {
        return clientSubscriptionRepository.findById(clientSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedClientSubscriptionDaoToMatchAllProperties(ClientSubscriptionDao expectedClientSubscriptionDao) {
        assertClientSubscriptionDaoAllPropertiesEquals(
            expectedClientSubscriptionDao,
            getPersistedClientSubscriptionDao(expectedClientSubscriptionDao)
        );
    }

    protected void assertPersistedClientSubscriptionDaoToMatchUpdatableProperties(ClientSubscriptionDao expectedClientSubscriptionDao) {
        assertClientSubscriptionDaoAllUpdatablePropertiesEquals(
            expectedClientSubscriptionDao,
            getPersistedClientSubscriptionDao(expectedClientSubscriptionDao)
        );
    }
}
