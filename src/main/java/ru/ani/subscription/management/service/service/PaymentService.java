package ru.ani.subscription.management.service.service;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.PaymentDao;
import ru.ani.subscription.management.service.repository.PaymentRepository;
import ru.ani.subscription.management.service.service.dto.PaymentDto;
import ru.ani.subscription.management.service.service.mapper.PaymentMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.PaymentDao}.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Save a payment.
     *
     * @param paymentDto the entity to save.
     * @return the persisted entity.
     */
    public PaymentDto save(PaymentDto paymentDto) {
        LOG.debug("Request to save Payment : {}", paymentDto);
        PaymentDao paymentDao = paymentMapper.toEntity(paymentDto);
        paymentDao = paymentRepository.save(paymentDao);
        return paymentMapper.toDto(paymentDao);
    }

    /**
     * Update a payment.
     *
     * @param paymentDto the entity to save.
     * @return the persisted entity.
     */
    public PaymentDto update(PaymentDto paymentDto) {
        LOG.debug("Request to update Payment : {}", paymentDto);
        PaymentDao paymentDao = paymentMapper.toEntity(paymentDto);
        paymentDao = paymentRepository.save(paymentDao);
        return paymentMapper.toDto(paymentDao);
    }

    /**
     * Partially update a payment.
     *
     * @param paymentDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentDto> partialUpdate(PaymentDto paymentDto) {
        LOG.debug("Request to partially update Payment : {}", paymentDto);

        return paymentRepository
            .findById(paymentDto.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDto);

                return existingPayment;
            })
            .map(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDto> findAll(Pageable pageable) {
        LOG.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentDto> findOne(UUID id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
