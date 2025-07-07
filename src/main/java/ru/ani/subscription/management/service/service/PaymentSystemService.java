package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.PaymentSystem;
import ru.ani.subscription.management.service.repository.PaymentSystemRepository;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDTO;
import ru.ani.subscription.management.service.service.mapper.PaymentSystemMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.PaymentSystem}.
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
     * @param paymentSystemDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentSystemDTO save(PaymentSystemDTO paymentSystemDTO) {
        LOG.debug("Request to save PaymentSystem : {}", paymentSystemDTO);
        PaymentSystem paymentSystem = paymentSystemMapper.toEntity(paymentSystemDTO);
        paymentSystem = paymentSystemRepository.save(paymentSystem);
        return paymentSystemMapper.toDto(paymentSystem);
    }

    /**
     * Update a paymentSystem.
     *
     * @param paymentSystemDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentSystemDTO update(PaymentSystemDTO paymentSystemDTO) {
        LOG.debug("Request to update PaymentSystem : {}", paymentSystemDTO);
        PaymentSystem paymentSystem = paymentSystemMapper.toEntity(paymentSystemDTO);
        paymentSystem = paymentSystemRepository.save(paymentSystem);
        return paymentSystemMapper.toDto(paymentSystem);
    }

    /**
     * Partially update a paymentSystem.
     *
     * @param paymentSystemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentSystemDTO> partialUpdate(PaymentSystemDTO paymentSystemDTO) {
        LOG.debug("Request to partially update PaymentSystem : {}", paymentSystemDTO);

        return paymentSystemRepository
            .findById(paymentSystemDTO.getId())
            .map(existingPaymentSystem -> {
                paymentSystemMapper.partialUpdate(existingPaymentSystem, paymentSystemDTO);

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
    public List<PaymentSystemDTO> findAll() {
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
    public Optional<PaymentSystemDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentSystem : {}", id);
        return paymentSystemRepository.findById(id).map(paymentSystemMapper::toDto);
    }

    /**
     * Delete the paymentSystem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentSystem : {}", id);
        paymentSystemRepository.deleteById(id);
    }
}
