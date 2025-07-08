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
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.repository.SubscriptionDetailsRepository;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;
import ru.ani.subscription.management.service.service.mapper.SubscriptionDetailsMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.SubscriptionDetailsDao}.
 */
@Service
@Transactional
public class SubscriptionDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionDetailsService.class);

    private final SubscriptionDetailsRepository subscriptionDetailsRepository;

    private final SubscriptionDetailsMapper subscriptionDetailsMapper;

    public SubscriptionDetailsService(
        SubscriptionDetailsRepository subscriptionDetailsRepository,
        SubscriptionDetailsMapper subscriptionDetailsMapper
    ) {
        this.subscriptionDetailsRepository = subscriptionDetailsRepository;
        this.subscriptionDetailsMapper = subscriptionDetailsMapper;
    }

    /**
     * Save a subscriptionDetails.
     *
     * @param subscriptionDetailsDto the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDetailsDto save(SubscriptionDetailsDto subscriptionDetailsDto) {
        LOG.debug("Request to save SubscriptionDetails : {}", subscriptionDetailsDto);
        SubscriptionDetailsDao subscriptionDetailsDao = subscriptionDetailsMapper.toEntity(subscriptionDetailsDto);
        subscriptionDetailsDao = subscriptionDetailsRepository.save(subscriptionDetailsDao);
        return subscriptionDetailsMapper.toDto(subscriptionDetailsDao);
    }

    /**
     * Update a subscriptionDetails.
     *
     * @param subscriptionDetailsDto the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDetailsDto update(SubscriptionDetailsDto subscriptionDetailsDto) {
        LOG.debug("Request to update SubscriptionDetails : {}", subscriptionDetailsDto);
        SubscriptionDetailsDao subscriptionDetailsDao = subscriptionDetailsMapper.toEntity(subscriptionDetailsDto);
        subscriptionDetailsDao = subscriptionDetailsRepository.save(subscriptionDetailsDao);
        return subscriptionDetailsMapper.toDto(subscriptionDetailsDao);
    }

    /**
     * Partially update a subscriptionDetails.
     *
     * @param subscriptionDetailsDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionDetailsDto> partialUpdate(SubscriptionDetailsDto subscriptionDetailsDto) {
        LOG.debug("Request to partially update SubscriptionDetails : {}", subscriptionDetailsDto);

        return subscriptionDetailsRepository
            .findById(subscriptionDetailsDto.getId())
            .map(existingSubscriptionDetails -> {
                subscriptionDetailsMapper.partialUpdate(existingSubscriptionDetails, subscriptionDetailsDto);

                return existingSubscriptionDetails;
            })
            .map(subscriptionDetailsRepository::save)
            .map(subscriptionDetailsMapper::toDto);
    }

    /**
     * Get all the subscriptionDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDetailsDto> findAll() {
        LOG.debug("Request to get all SubscriptionDetails");
        return subscriptionDetailsRepository
            .findAll()
            .stream()
            .map(subscriptionDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one subscriptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionDetailsDto> findOne(UUID id) {
        LOG.debug("Request to get SubscriptionDetails : {}", id);
        return subscriptionDetailsRepository.findById(id).map(subscriptionDetailsMapper::toDto);
    }

    /**
     * Delete the subscriptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete SubscriptionDetails : {}", id);
        subscriptionDetailsRepository.deleteById(id);
    }
}
