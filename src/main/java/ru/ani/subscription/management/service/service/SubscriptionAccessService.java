package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;
import ru.ani.subscription.management.service.repository.SubscriptionAccessRepository;
import ru.ani.subscription.management.service.service.dto.SubscriptionAccessDto;
import ru.ani.subscription.management.service.service.mapper.SubscriptionAccessMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.SubscriptionAccessDao}.
 */
@Service
@Transactional
public class SubscriptionAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAccessService.class);

    private final SubscriptionAccessRepository subscriptionAccessRepository;

    private final SubscriptionAccessMapper subscriptionAccessMapper;

    public SubscriptionAccessService(
        SubscriptionAccessRepository subscriptionAccessRepository,
        SubscriptionAccessMapper subscriptionAccessMapper
    ) {
        this.subscriptionAccessRepository = subscriptionAccessRepository;
        this.subscriptionAccessMapper = subscriptionAccessMapper;
    }

    /**
     * Save a subscriptionAccess.
     *
     * @param subscriptionAccessDto the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionAccessDto save(SubscriptionAccessDto subscriptionAccessDto) {
        LOG.debug("Request to save SubscriptionAccess : {}", subscriptionAccessDto);
        SubscriptionAccessDao subscriptionAccessDao = subscriptionAccessMapper.toEntity(subscriptionAccessDto);
        subscriptionAccessDao = subscriptionAccessRepository.save(subscriptionAccessDao);
        return subscriptionAccessMapper.toDto(subscriptionAccessDao);
    }

    /**
     * Update a subscriptionAccess.
     *
     * @param subscriptionAccessDto the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionAccessDto update(SubscriptionAccessDto subscriptionAccessDto) {
        LOG.debug("Request to update SubscriptionAccess : {}", subscriptionAccessDto);
        SubscriptionAccessDao subscriptionAccessDao = subscriptionAccessMapper.toEntity(subscriptionAccessDto);
        subscriptionAccessDao = subscriptionAccessRepository.save(subscriptionAccessDao);
        return subscriptionAccessMapper.toDto(subscriptionAccessDao);
    }

    /**
     * Partially update a subscriptionAccess.
     *
     * @param subscriptionAccessDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionAccessDto> partialUpdate(SubscriptionAccessDto subscriptionAccessDto) {
        LOG.debug("Request to partially update SubscriptionAccess : {}", subscriptionAccessDto);

        return subscriptionAccessRepository
            .findById(subscriptionAccessDto.getId())
            .map(existingSubscriptionAccess -> {
                subscriptionAccessMapper.partialUpdate(existingSubscriptionAccess, subscriptionAccessDto);

                return existingSubscriptionAccess;
            })
            .map(subscriptionAccessRepository::save)
            .map(subscriptionAccessMapper::toDto);
    }

    /**
     * Get all the subscriptionAccesses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionAccessDto> findAll() {
        LOG.debug("Request to get all SubscriptionAccesses");
        return subscriptionAccessRepository
            .findAll()
            .stream()
            .map(subscriptionAccessMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the subscriptionAccesses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SubscriptionAccessDto> findAllWithEagerRelationships(Pageable pageable) {
        return subscriptionAccessRepository.findAllWithEagerRelationships(pageable).map(subscriptionAccessMapper::toDto);
    }

    /**
     * Get one subscriptionAccess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionAccessDto> findOne(UUID id) {
        LOG.debug("Request to get SubscriptionAccess : {}", id);
        return subscriptionAccessRepository.findOneWithEagerRelationships(id).map(subscriptionAccessMapper::toDto);
    }

    /**
     * Delete the subscriptionAccess by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete SubscriptionAccess : {}", id);
        subscriptionAccessRepository.deleteById(id);
    }
}
