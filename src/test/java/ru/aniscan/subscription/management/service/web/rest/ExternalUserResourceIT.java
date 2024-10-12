package ru.aniscan.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aniscan.subscription.management.service.domain.ExternalUserAsserts.*;
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
import ru.aniscan.subscription.management.service.domain.ExternalUser;
import ru.aniscan.subscription.management.service.repository.ExternalUserRepository;

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalUserMockMvc;

    private ExternalUser externalUser;

    private ExternalUser insertedExternalUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalUser createEntity() {
        return new ExternalUser().externalUserId(DEFAULT_EXTERNAL_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalUser createUpdatedEntity() {
        return new ExternalUser().externalUserId(UPDATED_EXTERNAL_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        externalUser = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExternalUser != null) {
            externalUserRepository.delete(insertedExternalUser);
            insertedExternalUser = null;
        }
    }

    @Test
    @Transactional
    void createExternalUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExternalUser
        var returnedExternalUser = om.readValue(
            restExternalUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExternalUser.class
        );

        // Validate the ExternalUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExternalUserUpdatableFieldsEquals(returnedExternalUser, getPersistedExternalUser(returnedExternalUser));

        insertedExternalUser = returnedExternalUser;
    }

    @Test
    @Transactional
    void createExternalUserWithExistingId() throws Exception {
        // Create the ExternalUser with an existing ID
        externalUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalUser.setExternalUserId(null);

        // Create the ExternalUser, which fails.

        restExternalUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUser)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExternalUsers() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        // Get all the externalUserList
        restExternalUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalUserId").value(hasItem(DEFAULT_EXTERNAL_USER_ID)));
    }

    @Test
    @Transactional
    void getExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        // Get the externalUser
        restExternalUserMockMvc
            .perform(get(ENTITY_API_URL_ID, externalUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalUser.getId().intValue()))
            .andExpect(jsonPath("$.externalUserId").value(DEFAULT_EXTERNAL_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingExternalUser() throws Exception {
        // Get the externalUser
        restExternalUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser
        ExternalUser updatedExternalUser = externalUserRepository.findById(externalUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExternalUser are not directly saved in db
        em.detach(updatedExternalUser);
        updatedExternalUser.externalUserId(UPDATED_EXTERNAL_USER_ID);

        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExternalUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExternalUser))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExternalUserToMatchAllProperties(updatedExternalUser);
    }

    @Test
    @Transactional
    void putNonExistingExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalUserWithPatch() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser using partial update
        ExternalUser partialUpdatedExternalUser = new ExternalUser();
        partialUpdatedExternalUser.setId(externalUser.getId());

        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalUser))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExternalUser, externalUser),
            getPersistedExternalUser(externalUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateExternalUserWithPatch() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalUser using partial update
        ExternalUser partialUpdatedExternalUser = new ExternalUser();
        partialUpdatedExternalUser.setId(externalUser.getId());

        partialUpdatedExternalUser.externalUserId(UPDATED_EXTERNAL_USER_ID);

        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalUser))
            )
            .andExpect(status().isOk());

        // Validate the ExternalUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalUserUpdatableFieldsEquals(partialUpdatedExternalUser, getPersistedExternalUser(partialUpdatedExternalUser));
    }

    @Test
    @Transactional
    void patchNonExistingExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(externalUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExternalUser() throws Exception {
        // Initialize the database
        insertedExternalUser = externalUserRepository.saveAndFlush(externalUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the externalUser
        restExternalUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalUser.getId()).accept(MediaType.APPLICATION_JSON))
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

    protected ExternalUser getPersistedExternalUser(ExternalUser externalUser) {
        return externalUserRepository.findById(externalUser.getId()).orElseThrow();
    }

    protected void assertPersistedExternalUserToMatchAllProperties(ExternalUser expectedExternalUser) {
        assertExternalUserAllPropertiesEquals(expectedExternalUser, getPersistedExternalUser(expectedExternalUser));
    }

    protected void assertPersistedExternalUserToMatchUpdatableProperties(ExternalUser expectedExternalUser) {
        assertExternalUserAllUpdatablePropertiesEquals(expectedExternalUser, getPersistedExternalUser(expectedExternalUser));
    }
}
