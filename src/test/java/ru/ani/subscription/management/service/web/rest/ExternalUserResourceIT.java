package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoAsserts.*;
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
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.repository.ExternalUserRepository;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.mapper.ExternalUserMapper;

/**
 * Integration tests for the {@link ExternalUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExternalUserResourceIT {

    private static final String DEFAULT_EXTERNAL_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/external-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private ExternalUserMapper externalUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalUserMockMvc;

    private ExternalUserDao externalUserDao;

    private ExternalUserDao insertedExternalUserDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalUserDao createEntity() {
        return new ExternalUserDao().externalUserId(DEFAULT_EXTERNAL_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalUserDao createUpdatedEntity() {
        return new ExternalUserDao().externalUserId(UPDATED_EXTERNAL_USER_ID);
    }

    @BeforeEach
    void initTest() {
        externalUserDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExternalUserDao != null) {
            externalUserRepository.delete(insertedExternalUserDao);
            insertedExternalUserDao = null;
        }
    }

    @Test
    @Transactional
    void createExternalUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);
        var returnedExternalUserDto = om.readValue(
            restExternalUserMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUserDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExternalUserDto.class
        );

        // Validate the ExternalUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExternalUserDao = externalUserMapper.toEntity(returnedExternalUserDto);
        assertExternalUserDaoUpdatableFieldsEquals(returnedExternalUserDao, getPersistedExternalUserDao(returnedExternalUserDao));

        insertedExternalUserDao = returnedExternalUserDao;
    }

    @Test
    @Transactional
    void createExternalUserWithExistingId() throws Exception {
        // Create the ExternalUser with an existing ID
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalUserDao.setExternalUserId(null);

        // Create the ExternalUser, which fails.
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        restExternalUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExternalUsers() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        // Get all the externalUserList
        restExternalUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalUserDao.getId().toString())))
            .andExpect(jsonPath("$.[*].externalUserId").value(hasItem(DEFAULT_EXTERNAL_USER_ID)));
    }

    @Test
    @Transactional
    void getExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        // Get the externalUser
        restExternalUserMockMvc
            .perform(get(ENTITY_API_URL_ID, externalUserDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalUserDao.getId().toString()))
            .andExpect(jsonPath("$.externalUserId").value(DEFAULT_EXTERNAL_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingExternalUser() throws Exception {
        // Get the externalUser
        restExternalUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser
        ExternalUserDao updatedExternalUserDao = externalUserRepository.findById(externalUserDao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExternalUserDao are not directly saved in db
        em.detach(updatedExternalUserDao);
        updatedExternalUserDao.externalUserId(UPDATED_EXTERNAL_USER_ID);
        ExternalUserDto externalUserDto = externalUserMapper.toDto(updatedExternalUserDao);

        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalUserDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExternalUserDaoToMatchAllProperties(updatedExternalUserDao);
    }

    @Test
    @Transactional
    void putNonExistingExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalUserDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalUserWithPatch() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser using partial update
        ExternalUserDao partialUpdatedExternalUserDao = new ExternalUserDao();
        partialUpdatedExternalUserDao.setId(externalUserDao.getId());

        partialUpdatedExternalUserDao.externalUserId(UPDATED_EXTERNAL_USER_ID);

        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalUserDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalUserDao))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalUserDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExternalUserDao, externalUserDao),
            getPersistedExternalUserDao(externalUserDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateExternalUserWithPatch() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser using partial update
        ExternalUserDao partialUpdatedExternalUserDao = new ExternalUserDao();
        partialUpdatedExternalUserDao.setId(externalUserDao.getId());

        partialUpdatedExternalUserDao.externalUserId(UPDATED_EXTERNAL_USER_ID);

        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalUserDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalUserDao))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalUserDaoUpdatableFieldsEquals(
            partialUpdatedExternalUserDao,
            getPersistedExternalUserDao(partialUpdatedExternalUserDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalUserDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUserDao.setId(UUID.randomUUID());

        // Create the ExternalUser
        ExternalUserDto externalUserDto = externalUserMapper.toDto(externalUserDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalUserDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUserDao = externalUserRepository.saveAndFlush(externalUserDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the externalUser
        restExternalUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalUserDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return externalUserRepository.count();
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

    protected ExternalUserDao getPersistedExternalUserDao(ExternalUserDao externalUser) {
        return externalUserRepository.findById(externalUser.getId()).orElseThrow();
    }

    protected void assertPersistedExternalUserDaoToMatchAllProperties(ExternalUserDao expectedExternalUserDao) {
        assertExternalUserDaoAllPropertiesEquals(expectedExternalUserDao, getPersistedExternalUserDao(expectedExternalUserDao));
    }

    protected void assertPersistedExternalUserDaoToMatchUpdatableProperties(ExternalUserDao expectedExternalUserDao) {
        assertExternalUserDaoAllUpdatablePropertiesEquals(expectedExternalUserDao, getPersistedExternalUserDao(expectedExternalUserDao));
    }
}
