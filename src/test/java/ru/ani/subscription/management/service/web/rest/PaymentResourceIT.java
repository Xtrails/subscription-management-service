package ru.ani.subscription.management.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ani.subscription.management.service.domain.PaymentDaoAsserts.*;
import static ru.ani.subscription.management.service.web.rest.TestUtil.createUpdateProxyForBean;
import static ru.ani.subscription.management.service.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import ru.ani.subscription.management.service.domain.PaymentDao;
import ru.ani.subscription.management.service.domain.enumeration.PaymentStatus;
import ru.ani.subscription.management.service.repository.PaymentRepository;
import ru.ani.subscription.management.service.service.dto.PaymentDto;
import ru.ani.subscription.management.service.service.mapper.PaymentMapper;

/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final PaymentStatus DEFAULT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_STATUS = PaymentStatus.COMPLETED;

    private static final LocalDate DEFAULT_PAYMENT_DTTM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DTTM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HASH_SUM = "AAAAAAAAAA";
    private static final String UPDATED_HASH_SUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private PaymentDao paymentDao;

    private PaymentDao insertedPaymentDao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentDao createEntity() {
        return new PaymentDao().amount(DEFAULT_AMOUNT).status(DEFAULT_STATUS).paymentDttm(DEFAULT_PAYMENT_DTTM).hashSum(DEFAULT_HASH_SUM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentDao createUpdatedEntity() {
        return new PaymentDao().amount(UPDATED_AMOUNT).status(UPDATED_STATUS).paymentDttm(UPDATED_PAYMENT_DTTM).hashSum(UPDATED_HASH_SUM);
    }

    @BeforeEach
    void initTest() {
        paymentDao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentDao != null) {
            paymentRepository.delete(insertedPaymentDao);
            insertedPaymentDao = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);
        var returnedPaymentDto = om.readValue(
            restPaymentMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDto.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentDao = paymentMapper.toEntity(returnedPaymentDto);
        assertPaymentDaoUpdatableFieldsEquals(returnedPaymentDao, getPersistedPaymentDao(returnedPaymentDao));

        insertedPaymentDao = returnedPaymentDao;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDao.setAmount(null);

        // Create the Payment, which fails.
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDao.setStatus(null);

        // Create the Payment, which fails.
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDttmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDao.setPaymentDttm(null);

        // Create the Payment, which fails.
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashSumIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDao.setHashSum(null);

        // Create the Payment, which fails.
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentDao.getId().toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentDttm").value(hasItem(DEFAULT_PAYMENT_DTTM.toString())))
            .andExpect(jsonPath("$.[*].hashSum").value(hasItem(DEFAULT_HASH_SUM)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentDao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentDao.getId().toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentDttm").value(DEFAULT_PAYMENT_DTTM.toString()))
            .andExpect(jsonPath("$.hashSum").value(DEFAULT_HASH_SUM));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        PaymentDao updatedPaymentDao = paymentRepository.findById(paymentDao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentDao are not directly saved in db
        em.detach(updatedPaymentDao);
        updatedPaymentDao.amount(UPDATED_AMOUNT).status(UPDATED_STATUS).paymentDttm(UPDATED_PAYMENT_DTTM).hashSum(UPDATED_HASH_SUM);
        PaymentDto paymentDto = paymentMapper.toDto(updatedPaymentDao);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentDaoToMatchAllProperties(updatedPaymentDao);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        PaymentDao partialUpdatedPaymentDao = new PaymentDao();
        partialUpdatedPaymentDao.setId(paymentDao.getId());

        partialUpdatedPaymentDao.amount(UPDATED_AMOUNT).status(UPDATED_STATUS).hashSum(UPDATED_HASH_SUM);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentDao))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentDaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentDao, paymentDao),
            getPersistedPaymentDao(paymentDao)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        PaymentDao partialUpdatedPaymentDao = new PaymentDao();
        partialUpdatedPaymentDao.setId(paymentDao.getId());

        partialUpdatedPaymentDao.amount(UPDATED_AMOUNT).status(UPDATED_STATUS).paymentDttm(UPDATED_PAYMENT_DTTM).hashSum(UPDATED_HASH_SUM);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentDao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentDao))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentDaoUpdatableFieldsEquals(partialUpdatedPaymentDao, getPersistedPaymentDao(partialUpdatedPaymentDao));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDao.setId(UUID.randomUUID());

        // Create the Payment
        PaymentDto paymentDto = paymentMapper.toDto(paymentDao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPaymentDao = paymentRepository.saveAndFlush(paymentDao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentDao.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected PaymentDao getPersistedPaymentDao(PaymentDao payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentDaoToMatchAllProperties(PaymentDao expectedPaymentDao) {
        assertPaymentDaoAllPropertiesEquals(expectedPaymentDao, getPersistedPaymentDao(expectedPaymentDao));
    }

    protected void assertPersistedPaymentDaoToMatchUpdatableProperties(PaymentDao expectedPaymentDao) {
        assertPaymentDaoAllUpdatablePropertiesEquals(expectedPaymentDao, getPersistedPaymentDao(expectedPaymentDao));
    }
}
