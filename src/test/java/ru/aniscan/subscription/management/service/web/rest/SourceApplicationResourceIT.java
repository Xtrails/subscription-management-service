package ru.aniscan.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationAsserts.*;
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
import ru.aniscan.subscription.management.service.domain.SourceApplication;
import ru.aniscan.subscription.management.service.repository.SourceApplicationRepository;

/**
 * Integration tests for the {@link SourceApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourceApplicationResourceIT {

    private static final String DEFAULT_APPLICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/source-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SourceApplicationRepository sourceApplicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceApplicationMockMvc;

    private SourceApplication sourceApplication;

    private SourceApplication insertedSourceApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApplication createEntity() {
        return new SourceApplication().applicationName(DEFAULT_APPLICATION_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApplication createUpdatedEntity() {
        return new SourceApplication().applicationName(UPDATED_APPLICATION_NAME);
    }

    @BeforeEach
    public void initTest() {
        sourceApplication = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSourceApplication != null) {
            sourceApplicationRepository.delete(insertedSourceApplication);
            insertedSourceApplication = null;
        }
    }

    @Test
    @Transactional
    void createSourceApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SourceApplication
        var returnedSourceApplication = om.readValue(
            restSourceApplicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceApplication)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SourceApplication.class
        );

        // Validate the SourceApplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSourceApplicationUpdatableFieldsEquals(returnedSourceApplication, getPersistedSourceApplication(returnedSourceApplication));

        insertedSourceApplication = returnedSourceApplication;
    }

    @Test
    @Transactional
    void createSourceApplicationWithExistingId() throws Exception {
        // Create the SourceApplication with an existing ID
        sourceApplication.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceApplication)))
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sourceApplication.setApplicationName(null);

        // Create the SourceApplication, which fails.

        restSourceApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceApplication)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourceApplications() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        // Get all the sourceApplicationList
        restSourceApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)));
    }

    @Test
    @Transactional
    void getSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        // Get the sourceApplication
        restSourceApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, sourceApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceApplication.getId().intValue()))
            .andExpect(jsonPath("$.applicationName").value(DEFAULT_APPLICATION_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSourceApplication() throws Exception {
        // Get the sourceApplication
        restSourceApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication
        SourceApplication updatedSourceApplication = sourceApplicationRepository.findById(sourceApplication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSourceApplication are not directly saved in db
        em.detach(updatedSourceApplication);
        updatedSourceApplication.applicationName(UPDATED_APPLICATION_NAME);

        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSourceApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSourceApplication))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSourceApplicationToMatchAllProperties(updatedSourceApplication);
    }

    @Test
    @Transactional
    void putNonExistingSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceApplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication using partial update
        SourceApplication partialUpdatedSourceApplication = new SourceApplication();
        partialUpdatedSourceApplication.setId(sourceApplication.getId());

        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceApplication))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSourceApplication, sourceApplication),
            getPersistedSourceApplication(sourceApplication)
        );
    }

    @Test
    @Transactional
    void fullUpdateSourceApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication using partial update
        SourceApplication partialUpdatedSourceApplication = new SourceApplication();
        partialUpdatedSourceApplication.setId(sourceApplication.getId());

        partialUpdatedSourceApplication.applicationName(UPDATED_APPLICATION_NAME);

        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceApplication))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceApplicationUpdatableFieldsEquals(
            partialUpdatedSourceApplication,
            getPersistedSourceApplication(partialUpdatedSourceApplication)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sourceApplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplication = sourceApplicationRepository.saveAndFlush(sourceApplication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sourceApplication
        restSourceApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourceApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sourceApplicationRepository.count();
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

    protected SourceApplication getPersistedSourceApplication(SourceApplication sourceApplication) {
        return sourceApplicationRepository.findById(sourceApplication.getId()).orElseThrow();
    }

    protected void assertPersistedSourceApplicationToMatchAllProperties(SourceApplication expectedSourceApplication) {
        assertSourceApplicationAllPropertiesEquals(expectedSourceApplication, getPersistedSourceApplication(expectedSourceApplication));
    }

    protected void assertPersistedSourceApplicationToMatchUpdatableProperties(SourceApplication expectedSourceApplication) {
        assertSourceApplicationAllUpdatablePropertiesEquals(
            expectedSourceApplication,
            getPersistedSourceApplication(expectedSourceApplication)
        );
    }
}
