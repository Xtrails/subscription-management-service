package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.SubscriptionDetails;
import ru.ani.subscription.management.service.repository.SubscriptionDetailsRepository;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDTO;
import ru.ani.subscription.management.service.service.mapper.SubscriptionDetailsMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.SubscriptionDetails}.
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
     * @param subscriptionDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDetailsDTO save(SubscriptionDetailsDTO subscriptionDetailsDTO) {
        LOG.debug("Request to save SubscriptionDetails : {}", subscriptionDetailsDTO);
        SubscriptionDetails subscriptionDetails = subscriptionDetailsMapper.toEntity(subscriptionDetailsDTO);
        subscriptionDetails = subscriptionDetailsRepository.save(subscriptionDetails);
        return subscriptionDetailsMapper.toDto(subscriptionDetails);
    }

    /**
     * Update a subscriptionDetails.
     *
     * @param subscriptionDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDetailsDTO update(SubscriptionDetailsDTO subscriptionDetailsDTO) {
        LOG.debug("Request to update SubscriptionDetails : {}", subscriptionDetailsDTO);
        SubscriptionDetails subscriptionDetails = subscriptionDetailsMapper.toEntity(subscriptionDetailsDTO);
        subscriptionDetails = subscriptionDetailsRepository.save(subscriptionDetails);
        return subscriptionDetailsMapper.toDto(subscriptionDetails);
    }

    /**
     * Partially update a subscriptionDetails.
     *
     * @param subscriptionDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionDetailsDTO> partialUpdate(SubscriptionDetailsDTO subscriptionDetailsDTO) {
        LOG.debug("Request to partially update SubscriptionDetails : {}", subscriptionDetailsDTO);

        return subscriptionDetailsRepository
            .findById(subscriptionDetailsDTO.getId())
            .map(existingSubscriptionDetails -> {
                subscriptionDetailsMapper.partialUpdate(existingSubscriptionDetails, subscriptionDetailsDTO);

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
    public List<SubscriptionDetailsDTO> findAll() {
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
    public Optional<SubscriptionDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get SubscriptionDetails : {}", id);
        return subscriptionDetailsRepository.findById(id).map(subscriptionDetailsMapper::toDto);
    }

    /**
     * Delete the subscriptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SubscriptionDetails : {}", id);
        subscriptionDetailsRepository.deleteById(id);
    }
}
