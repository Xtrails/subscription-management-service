package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationDaoAsserts.*;
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
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.repository.SourceApplicationRepository;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;
import ru.ani.subscription.management.service.service.mapper.SourceApplicationMapper;

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

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SourceApplicationRepository sourceApplicationRepository;

    @Autowired
    private SourceApplicationMapper sourceApplicationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceApplicationMockMvc;

    private SourceApplicationDao sourceApplicationDao;

    private SourceApplicationDao insertedSourceApplicationDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApplicationDao createEntity() {
        return new SourceApplicationDao().applicationName(DEFAULT_APPLICATION_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApplicationDao createUpdatedEntity() {
        return new SourceApplicationDao().applicationName(UPDATED_APPLICATION_NAME);
    }

    @BeforeEach
    void initTest() {
        sourceApplicationDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSourceApplicationDao != null) {
            sourceApplicationRepository.delete(insertedSourceApplicationDao);
            insertedSourceApplicationDao = null;
        }
    }

    @Test
    @Transactional
    void createSourceApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);
        var returnedSourceApplicationDto = om.readValue(
            restSourceApplicationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(sourceApplicationDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SourceApplicationDto.class
        );

        // Validate the SourceApplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSourceApplicationDao = sourceApplicationMapper.toEntity(returnedSourceApplicationDto);
        assertSourceApplicationDaoUpdatableFieldsEquals(
            returnedSourceApplicationDao,
            getPersistedSourceApplicationDao(returnedSourceApplicationDao)
        );

        insertedSourceApplicationDao = returnedSourceApplicationDao;
    }

    @Test
    @Transactional
    void createSourceApplicationWithExistingId() throws Exception {
        // Create the SourceApplication with an existing ID
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sourceApplicationDao.setApplicationName(null);

        // Create the SourceApplication, which fails.
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        restSourceApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourceApplications() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        // Get all the sourceApplicationList
        restSourceApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceApplicationDao.getId().toString())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)));
    }

    @Test
    @Transactional
    void getSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        // Get the sourceApplication
        restSourceApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, sourceApplicationDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceApplicationDao.getId().toString()))
            .andExpect(jsonPath("$.applicationName").value(DEFAULT_APPLICATION_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSourceApplication() throws Exception {
        // Get the sourceApplication
        restSourceApplicationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication
        SourceApplicationDao updatedSourceApplicationDao = sourceApplicationRepository.findById(sourceApplicationDao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSourceApplicationDao are not directly saved in db
        em.detach(updatedSourceApplicationDao);
        updatedSourceApplicationDao.applicationName(UPDATED_APPLICATION_NAME);
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(updatedSourceApplicationDao);

        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceApplicationDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSourceApplicationDaoToMatchAllProperties(updatedSourceApplicationDao);
    }

    @Test
    @Transactional
    void putNonExistingSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceApplicationDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication using partial update
        SourceApplicationDao partialUpdatedSourceApplicationDao = new SourceApplicationDao();
        partialUpdatedSourceApplicationDao.setId(sourceApplicationDao.getId());

        partialUpdatedSourceApplicationDao.applicationName(UPDATED_APPLICATION_NAME);

        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApplicationDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceApplicationDao))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceApplicationDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSourceApplicationDao, sourceApplicationDao),
            getPersistedSourceApplicationDao(sourceApplicationDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateSourceApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceApplication using partial update
        SourceApplicationDao partialUpdatedSourceApplicationDao = new SourceApplicationDao();
        partialUpdatedSourceApplicationDao.setId(sourceApplicationDao.getId());

        partialUpdatedSourceApplicationDao.applicationName(UPDATED_APPLICATION_NAME);

        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApplicationDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceApplicationDao))
            )
            .andExpect(status().isOk());

        // Validate the SourceApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceApplicationDaoUpdatableFieldsEquals(
            partialUpdatedSourceApplicationDao,
            getPersistedSourceApplicationDao(partialUpdatedSourceApplicationDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceApplicationDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourceApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceApplicationDao.setId(UUID.randomUUID());

        // Create the SourceApplication
        SourceApplicationDto sourceApplicationDto = sourceApplicationMapper.toDto(sourceApplicationDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceApplicationDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSourceApplication() throws Exception {
        // Initialize the database
        insertedSourceApplicationDao = sourceApplicationRepository.saveAndFlush(sourceApplicationDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sourceApplication
        restSourceApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourceApplicationDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
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

    protected SourceApplicationDao getPersistedSourceApplicationDao(SourceApplicationDao sourceApplication) {
        return sourceApplicationRepository.findById(sourceApplication.getId()).orElseThrow();
    }

    protected void assertPersistedSourceApplicationDaoToMatchAllProperties(SourceApplicationDao expectedSourceApplicationDao) {
        assertSourceApplicationDaoAllPropertiesEquals(
            expectedSourceApplicationDao,
            getPersistedSourceApplicationDao(expectedSourceApplicationDao)
        );
    }

    protected void assertPersistedSourceApplicationDaoToMatchUpdatableProperties(SourceApplicationDao expectedSourceApplicationDao) {
        assertSourceApplicationDaoAllUpdatablePropertiesEquals(
            expectedSourceApplicationDao,
            getPersistedSourceApplicationDao(expectedSourceApplicationDao)
        );
    }
}
