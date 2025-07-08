package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.PaymentSystemDao;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDto;
import ru.ani.subscription.management.service.service.mapper.PaymentSystemMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.PaymentSystemDao}.
 */
@Service
@Transactional
public class PaymentSystemService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSystemService.class);

    private final PaymentSystemRepository paymentSystemRepository;

    private final PaymentSystemMapper paymentSystemMapper;

    public PaymentSystemService(PaymentSystemRepository paymentSystemRepository, PaymentSystemMapper paymentSystemMapper) {
        this.paymentSystemRepository = paymentSystemRepository;
        this.paymentSystemMapper = paymentSystemMapper;
    }

    /**
     * Save a paymentSystem.
     *
     * @param paymentSystemDto the entity to save.
     * @return the persisted entity.
     */
    public PaymentSystemDto save(PaymentSystemDto paymentSystemDto) {
        LOG.debug("Request to save PaymentSystem : {}", paymentSystemDto);
        PaymentSystemDao paymentSystemDao = paymentSystemMapper.toEntity(paymentSystemDto);
        paymentSystemDao = paymentSystemRepository.save(paymentSystemDao);
        return paymentSystemMapper.toDto(paymentSystemDao);
    }

    /**
     * Update a paymentSystem.
     *
     * @param paymentSystemDto the entity to save.
     * @return the persisted entity.
     */
    public PaymentSystemDto update(PaymentSystemDto paymentSystemDto) {
        LOG.debug("Request to update PaymentSystem : {}", paymentSystemDto);
        PaymentSystemDao paymentSystemDao = paymentSystemMapper.toEntity(paymentSystemDto);
        paymentSystemDao = paymentSystemRepository.save(paymentSystemDao);
        return paymentSystemMapper.toDto(paymentSystemDao);
    }

    /**
     * Partially update a paymentSystem.
     *
     * @param paymentSystemDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentSystemDto> partialUpdate(PaymentSystemDto paymentSystemDto) {
        LOG.debug("Request to partially update PaymentSystem : {}", paymentSystemDto);

        return paymentSystemRepository
            .findById(paymentSystemDto.getId())
            .map(existingPaymentSystem -> {
                paymentSystemMapper.partialUpdate(existingPaymentSystem, paymentSystemDto);

                return existingPaymentSystem;
            })
            .map(paymentSystemRepository::save)
            .map(paymentSystemMapper::toDto);
    }

    /**
     * Get all the paymentSystems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentSystemDto> findAll() {
        LOG.debug("Request to get all PaymentSystems");
        return paymentSystemRepository.findAll().stream().map(paymentSystemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paymentSystem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentSystemDto> findOne(UUID id) {
        LOG.debug("Request to get PaymentSystem : {}", id);
        return paymentSystemRepository.findById(id).map(paymentSystemMapper::toDto);
    }

    /**
     * Delete the paymentSystem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete PaymentSystem : {}", id);
        paymentSystemRepository.deleteById(id);
    }
}
