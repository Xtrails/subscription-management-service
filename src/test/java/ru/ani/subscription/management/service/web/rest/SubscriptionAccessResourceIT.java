package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.SubscriptionAccessDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.IntegrationTest;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.repository.SubscriptionAccessRepository;
import ru.ani.subscription.management.service.service.SubscriptionAccessService;
import ru.ani.subscription.management.service.service.dto.SubscriptionAccessDto;
import ru.ani.subscription.management.service.service.mapper.SubscriptionAccessMapper;

/**
 * Integration tests for the {@link SubscriptionAccessResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionAccessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_GROUP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/subscription-accesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionAccessRepository subscriptionAccessRepository;

    @Mock
    private SubscriptionAccessRepository subscriptionAccessRepositoryMock;

    @Autowired
    private SubscriptionAccessMapper subscriptionAccessMapper;

    @Mock
    private SubscriptionAccessService subscriptionAccessServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionAccessMockMvc;

    private SubscriptionAccessDao subscriptionAccessDao;

    private SubscriptionAccessDao insertedSubscriptionAccessDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionAccessDao createEntity(EntityManager em) {
        SubscriptionAccessDao subscriptionAccessDao = new SubscriptionAccessDao()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .order(DEFAULT_ORDER)
            .role(DEFAULT_ROLE)
            .roleGroup(DEFAULT_ROLE_GROUP)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        SubscriptionDetailsDao subscriptionDetails;
        if (TestUtil.findAll(em, SubscriptionDetailsDao.class).isEmpty()) {
            subscriptionDetails = SubscriptionDetailsResourceIT.createEntity();
            em.persist(subscriptionDetails);
            em.flush();
        } else {
            subscriptionDetails = TestUtil.findAll(em, SubscriptionDetailsDao.class).get(0);
        }
        subscriptionAccessDao.getSubscriptionDetails().add(subscriptionDetails);
        return subscriptionAccessDao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionAccessDao createUpdatedEntity(EntityManager em) {
        SubscriptionAccessDao updatedSubscriptionAccessDao = new SubscriptionAccessDao()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .role(UPDATED_ROLE)
            .roleGroup(UPDATED_ROLE_GROUP)
            .active(UPDATED_ACTIVE);
        // Add required entity
        SubscriptionDetailsDao subscriptionDetails;
        if (TestUtil.findAll(em, SubscriptionDetailsDao.class).isEmpty()) {
            subscriptionDetails = SubscriptionDetailsResourceIT.createUpdatedEntity();
            em.persist(subscriptionDetails);
            em.flush();
        } else {
            subscriptionDetails = TestUtil.findAll(em, SubscriptionDetailsDao.class).get(0);
        }
        updatedSubscriptionAccessDao.getSubscriptionDetails().add(subscriptionDetails);
        return updatedSubscriptionAccessDao;
    }

    @BeforeEach
    void initTest() {
        subscriptionAccessDao = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSubscriptionAccessDao != null) {
            subscriptionAccessRepository.delete(insertedSubscriptionAccessDao);
            insertedSubscriptionAccessDao = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionAccess() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);
        var returnedSubscriptionAccessDto = om.readValue(
            restSubscriptionAccessMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(subscriptionAccessDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionAccessDto.class
        );

        // Validate the SubscriptionAccess in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscriptionAccessDao = subscriptionAccessMapper.toEntity(returnedSubscriptionAccessDto);
        assertSubscriptionAccessDaoUpdatableFieldsEquals(
            returnedSubscriptionAccessDao,
            getPersistedSubscriptionAccessDao(returnedSubscriptionAccessDao)
        );

        insertedSubscriptionAccessDao = returnedSubscriptionAccessDao;
    }

    @Test
    @Transactional
    void createSubscriptionAccessWithExistingId() throws Exception {
        // Create the SubscriptionAccess with an existing ID
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionAccessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionAccessDao.setName(null);

        // Create the SubscriptionAccess, which fails.
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        restSubscriptionAccessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionAccessDao.setActive(null);

        // Create the SubscriptionAccess, which fails.
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        restSubscriptionAccessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionAccesses() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        // Get all the subscriptionAccessList
        restSubscriptionAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionAccessDao.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].roleGroup").value(hasItem(DEFAULT_ROLE_GROUP)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscriptionAccessesWithEagerRelationshipsIsEnabled() throws Exception {
        when(subscriptionAccessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscriptionAccessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subscriptionAccessServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscriptionAccessesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subscriptionAccessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscriptionAccessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subscriptionAccessRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubscriptionAccess() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        // Get the subscriptionAccess
        restSubscriptionAccessMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionAccessDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionAccessDao.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.roleGroup").value(DEFAULT_ROLE_GROUP))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionAccess() throws Exception {
        // Get the subscriptionAccess
        restSubscriptionAccessMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionAccess() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionAccess
        SubscriptionAccessDao updatedSubscriptionAccessDao = subscriptionAccessRepository
            .findById(subscriptionAccessDao.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionAccessDao are not directly saved in db
        em.detach(updatedSubscriptionAccessDao);
        updatedSubscriptionAccessDao
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .role(UPDATED_ROLE)
            .roleGroup(UPDATED_ROLE_GROUP)
            .active(UPDATED_ACTIVE);
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(updatedSubscriptionAccessDao);

        restSubscriptionAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionAccessDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionAccessDaoToMatchAllProperties(updatedSubscriptionAccessDao);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionAccessDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionAccessWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionAccess using partial update
        SubscriptionAccessDao partialUpdatedSubscriptionAccessDao = new SubscriptionAccessDao();
        partialUpdatedSubscriptionAccessDao.setId(subscriptionAccessDao.getId());

        partialUpdatedSubscriptionAccessDao.name(UPDATED_NAME).order(UPDATED_ORDER);

        restSubscriptionAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionAccessDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionAccessDao))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionAccess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionAccessDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionAccessDao, subscriptionAccessDao),
            getPersistedSubscriptionAccessDao(subscriptionAccessDao)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionAccessWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionAccess using partial update
        SubscriptionAccessDao partialUpdatedSubscriptionAccessDao = new SubscriptionAccessDao();
        partialUpdatedSubscriptionAccessDao.setId(subscriptionAccessDao.getId());

        partialUpdatedSubscriptionAccessDao
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .role(UPDATED_ROLE)
            .roleGroup(UPDATED_ROLE_GROUP)
            .active(UPDATED_ACTIVE);

        restSubscriptionAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionAccessDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionAccessDao))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionAccess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionAccessDaoUpdatableFieldsEquals(
            partialUpdatedSubscriptionAccessDao,
            getPersistedSubscriptionAccessDao(partialUpdatedSubscriptionAccessDao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionAccessDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionAccessDao.setId(UUID.randomUUID());

        // Create the SubscriptionAccess
        SubscriptionAccessDto subscriptionAccessDto = subscriptionAccessMapper.toDto(subscriptionAccessDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionAccessMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionAccessDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionAccess() throws Exception {
        // Initialize the database
        insertedSubscriptionAccessDao = subscriptionAccessRepository.saveAndFlush(subscriptionAccessDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionAccess
        restSubscriptionAccessMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionAccessDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionAccessRepository.count();
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

    protected SubscriptionAccessDao getPersistedSubscriptionAccessDao(SubscriptionAccessDao subscriptionAccess) {
        return subscriptionAccessRepository.findById(subscriptionAccess.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionAccessDaoToMatchAllProperties(SubscriptionAccessDao expectedSubscriptionAccessDao) {
        assertSubscriptionAccessDaoAllPropertiesEquals(
            expectedSubscriptionAccessDao,
            getPersistedSubscriptionAccessDao(expectedSubscriptionAccessDao)
        );
    }

    protected void assertPersistedSubscriptionAccessDaoToMatchUpdatableProperties(SubscriptionAccessDao expectedSubscriptionAccessDao) {
        assertSubscriptionAccessDaoAllUpdatablePropertiesEquals(
            expectedSubscriptionAccessDao,
            getPersistedSubscriptionAccessDao(expectedSubscriptionAccessDao)
        );
    }
}
