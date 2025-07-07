package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.ClientSubscription;
import ru.ani.subscription.management.service.repository.ClientSubscriptionRepository;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDTO;
import ru.ani.subscription.management.service.service.mapper.ClientSubscriptionMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ClientSubscription}.
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
     * @param clientSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientSubscriptionDTO save(ClientSubscriptionDTO clientSubscriptionDTO) {
        LOG.debug("Request to save ClientSubscription : {}", clientSubscriptionDTO);
        ClientSubscription clientSubscription = clientSubscriptionMapper.toEntity(clientSubscriptionDTO);
        clientSubscription = clientSubscriptionRepository.save(clientSubscription);
        return clientSubscriptionMapper.toDto(clientSubscription);
    }

    /**
     * Update a clientSubscription.
     *
     * @param clientSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientSubscriptionDTO update(ClientSubscriptionDTO clientSubscriptionDTO) {
        LOG.debug("Request to update ClientSubscription : {}", clientSubscriptionDTO);
        ClientSubscription clientSubscription = clientSubscriptionMapper.toEntity(clientSubscriptionDTO);
        clientSubscription = clientSubscriptionRepository.save(clientSubscription);
        return clientSubscriptionMapper.toDto(clientSubscription);
    }

    /**
     * Partially update a clientSubscription.
     *
     * @param clientSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClientSubscriptionDTO> partialUpdate(ClientSubscriptionDTO clientSubscriptionDTO) {
        LOG.debug("Request to partially update ClientSubscription : {}", clientSubscriptionDTO);

        return clientSubscriptionRepository
            .findById(clientSubscriptionDTO.getId())
            .map(existingClientSubscription -> {
                clientSubscriptionMapper.partialUpdate(existingClientSubscription, clientSubscriptionDTO);

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
    public Page<ClientSubscriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ClientSubscriptions");
        return clientSubscriptionRepository.findAll(pageable).map(clientSubscriptionMapper::toDto);
    }

    /**
     *  Get all the clientSubscriptions where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientSubscriptionDTO> findAllWherePaymentIsNull() {
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
    public Optional<ClientSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get ClientSubscription : {}", id);
        return clientSubscriptionRepository.findById(id).map(clientSubscriptionMapper::toDto);
    }

    /**
     * Delete the clientSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ClientSubscription : {}", id);
        clientSubscriptionRepository.deleteById(id);
    }
}
