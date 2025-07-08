package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.ClientSubscriptionDao;
import ru.ani.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDto;
import ru.ani.subscription.management.service.service.mapper.ClientSubscriptionMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ClientSubscriptionDao}.
 */
@Service
@Transactional
public class ClientSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientSubscriptionService.class);

    private final ClientSubscriptionRepository clientSubscriptionRepository;

    private final ClientSubscriptionMapper clientSubscriptionMapper;

    public ClientSubscriptionService(
        ClientSubscriptionRepository clientSubscriptionRepository,
        ClientSubscriptionMapper clientSubscriptionMapper
    ) {
        this.clientSubscriptionRepository = clientSubscriptionRepository;
        this.clientSubscriptionMapper = clientSubscriptionMapper;
    }

    /**
     * Save a clientSubscription.
     *
     * @param clientSubscriptionDto the entity to save.
     * @return the persisted entity.
     */
    public ClientSubscriptionDto save(ClientSubscriptionDto clientSubscriptionDto) {
        LOG.debug("Request to save ClientSubscription : {}", clientSubscriptionDto);
        ClientSubscriptionDao clientSubscriptionDao = clientSubscriptionMapper.toEntity(clientSubscriptionDto);
        clientSubscriptionDao = clientSubscriptionRepository.save(clientSubscriptionDao);
        return clientSubscriptionMapper.toDto(clientSubscriptionDao);
    }

    /**
     * Update a clientSubscription.
     *
     * @param clientSubscriptionDto the entity to save.
     * @return the persisted entity.
     */
    public ClientSubscriptionDto update(ClientSubscriptionDto clientSubscriptionDto) {
        LOG.debug("Request to update ClientSubscription : {}", clientSubscriptionDto);
        ClientSubscriptionDao clientSubscriptionDao = clientSubscriptionMapper.toEntity(clientSubscriptionDto);
        clientSubscriptionDao = clientSubscriptionRepository.save(clientSubscriptionDao);
        return clientSubscriptionMapper.toDto(clientSubscriptionDao);
    }

    /**
     * Partially update a clientSubscription.
     *
     * @param clientSubscriptionDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClientSubscriptionDto> partialUpdate(ClientSubscriptionDto clientSubscriptionDto) {
        LOG.debug("Request to partially update ClientSubscription : {}", clientSubscriptionDto);

        return clientSubscriptionRepository
            .findById(clientSubscriptionDto.getId())
            .map(existingClientSubscription -> {
                clientSubscriptionMapper.partialUpdate(existingClientSubscription, clientSubscriptionDto);

                return existingClientSubscription;
            })
            .map(clientSubscriptionRepository::save)
            .map(clientSubscriptionMapper::toDto);
    }

    /**
     * Get all the clientSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientSubscriptionDto> findAll(Pageable pageable) {
        LOG.debug("Request to get all ClientSubscriptions");
        return clientSubscriptionRepository.findAll(pageable).map(clientSubscriptionMapper::toDto);
    }

    /**
     *  Get all the clientSubscriptions where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientSubscriptionDto> findAllWherePaymentIsNull() {
        LOG.debug("Request to get all clientSubscriptions where Payment is null");
        return StreamSupport.stream(clientSubscriptionRepository.findAll().spliterator(), false)
            .filter(clientSubscription -> clientSubscription.getPayment() == null)
            .map(clientSubscriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one clientSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientSubscriptionDto> findOne(UUID id) {
        LOG.debug("Request to get ClientSubscription : {}", id);
        return clientSubscriptionRepository.findById(id).map(clientSubscriptionMapper::toDto);
    }

    /**
     * Delete the clientSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete ClientSubscription : {}", id);
        clientSubscriptionRepository.deleteById(id);
    }
}
