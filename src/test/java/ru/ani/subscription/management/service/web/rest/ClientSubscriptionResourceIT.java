package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import ru.ani.subscription.management.service.domain.ClientSubscription;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;
import ru.ani.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDTO;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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

    private ClientSubscription clientSubscription;

    private ClientSubscription insertedClientSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientSubscription createEntity() {
        return new ClientSubscription().startDttm(DEFAULT_START_DTTM).endDttm(DEFAULT_END_DTTM).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientSubscription createUpdatedEntity() {
        return new ClientSubscription().startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        clientSubscription = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientSubscription != null) {
            clientSubscriptionRepository.delete(insertedClientSubscription);
            insertedClientSubscription = null;
        }
    }

    @Test
    @Transactional
    void createClientSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);
        var returnedClientSubscriptionDTO = om.readValue(
            restClientSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(clientSubscriptionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientSubscriptionDTO.class
        );

        // Validate the ClientSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientSubscription = clientSubscriptionMapper.toEntity(returnedClientSubscriptionDTO);
        assertClientSubscriptionUpdatableFieldsEquals(
            returnedClientSubscription,
            getPersistedClientSubscription(returnedClientSubscription)
        );

        insertedClientSubscription = returnedClientSubscription;
    }

    @Test
    @Transactional
    void createClientSubscriptionWithExistingId() throws Exception {
        // Create the ClientSubscription with an existing ID
        clientSubscription.setId(1L);
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
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
        clientSubscription.setStartDttm(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientSubscription.setEndDttm(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientSubscription.setStatus(null);

        // Create the ClientSubscription, which fails.
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        restClientSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientSubscriptions() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        // Get all the clientSubscriptionList
        restClientSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDttm").value(hasItem(DEFAULT_START_DTTM.toString())))
            .andExpect(jsonPath("$.[*].endDttm").value(hasItem(DEFAULT_END_DTTM.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        // Get the clientSubscription
        restClientSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, clientSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientSubscription.getId().intValue()))
            .andExpect(jsonPath("$.startDttm").value(DEFAULT_START_DTTM.toString()))
            .andExpect(jsonPath("$.endDttm").value(DEFAULT_END_DTTM.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClientSubscription() throws Exception {
        // Get the clientSubscription
        restClientSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription
        ClientSubscription updatedClientSubscription = clientSubscriptionRepository.findById(clientSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientSubscription are not directly saved in db
        em.detach(updatedClientSubscription);
        updatedClientSubscription.startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(updatedClientSubscription);

        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientSubscriptionToMatchAllProperties(updatedClientSubscription);
    }

    @Test
    @Transactional
    void putNonExistingClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription using partial update
        ClientSubscription partialUpdatedClientSubscription = new ClientSubscription();
        partialUpdatedClientSubscription.setId(clientSubscription.getId());

        partialUpdatedClientSubscription.startDttm(UPDATED_START_DTTM);

        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientSubscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientSubscription))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientSubscription, clientSubscription),
            getPersistedClientSubscription(clientSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientSubscription using partial update
        ClientSubscription partialUpdatedClientSubscription = new ClientSubscription();
        partialUpdatedClientSubscription.setId(clientSubscription.getId());

        partialUpdatedClientSubscription.startDttm(UPDATED_START_DTTM).endDttm(UPDATED_END_DTTM).status(UPDATED_STATUS);

        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientSubscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientSubscription))
            )
            .andExpect(status().isOk());

        // Validate the ClientSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientSubscriptionUpdatableFieldsEquals(
            partialUpdatedClientSubscription,
            getPersistedClientSubscription(partialUpdatedClientSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSubscription.setId(longCount.incrementAndGet());

        // Create the ClientSubscription
        ClientSubscriptionDTO clientSubscriptionDTO = clientSubscriptionMapper.toDto(clientSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientSubscription() throws Exception {
        // Initialize the database
        insertedClientSubscription = clientSubscriptionRepository.saveAndFlush(clientSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clientSubscription
        restClientSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientSubscription.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
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

    protected ClientSubscription getPersistedClientSubscription(ClientSubscription clientSubscription) {
        return clientSubscriptionRepository.findById(clientSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedClientSubscriptionToMatchAllProperties(ClientSubscription expectedClientSubscription) {
        assertClientSubscriptionAllPropertiesEquals(expectedClientSubscription, getPersistedClientSubscription(expectedClientSubscription));
    }

    protected void assertPersistedClientSubscriptionToMatchUpdatableProperties(ClientSubscription expectedClientSubscription) {
        assertClientSubscriptionAllUpdatablePropertiesEquals(
            expectedClientSubscription,
            getPersistedClientSubscription(expectedClientSubscription)
        );
    }
}
