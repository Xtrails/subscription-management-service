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
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.repository.SourceApplicationRepository;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;
import ru.ani.subscription.management.service.service.mapper.SourceApplicationMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.SourceApplicationDao}.
 */
@Service
@Transactional
public class SourceApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceApplicationService.class);

    private final SourceApplicationRepository sourceApplicationRepository;

    private final SourceApplicationMapper sourceApplicationMapper;

    public SourceApplicationService(
        SourceApplicationRepository sourceApplicationRepository,
        SourceApplicationMapper sourceApplicationMapper
    ) {
        this.sourceApplicationRepository = sourceApplicationRepository;
        this.sourceApplicationMapper = sourceApplicationMapper;
    }

    /**
     * Save a sourceApplication.
     *
     * @param sourceApplicationDto the entity to save.
     * @return the persisted entity.
     */
    public SourceApplicationDto save(SourceApplicationDto sourceApplicationDto) {
        LOG.debug("Request to save SourceApplication : {}", sourceApplicationDto);
        SourceApplicationDao sourceApplicationDao = sourceApplicationMapper.toEntity(sourceApplicationDto);
        sourceApplicationDao = sourceApplicationRepository.save(sourceApplicationDao);
        return sourceApplicationMapper.toDto(sourceApplicationDao);
    }

    /**
     * Update a sourceApplication.
     *
     * @param sourceApplicationDto the entity to save.
     * @return the persisted entity.
     */
    public SourceApplicationDto update(SourceApplicationDto sourceApplicationDto) {
        LOG.debug("Request to update SourceApplication : {}", sourceApplicationDto);
        SourceApplicationDao sourceApplicationDao = sourceApplicationMapper.toEntity(sourceApplicationDto);
        sourceApplicationDao = sourceApplicationRepository.save(sourceApplicationDao);
        return sourceApplicationMapper.toDto(sourceApplicationDao);
    }

    /**
     * Partially update a sourceApplication.
     *
     * @param sourceApplicationDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SourceApplicationDto> partialUpdate(SourceApplicationDto sourceApplicationDto) {
        LOG.debug("Request to partially update SourceApplication : {}", sourceApplicationDto);

        return sourceApplicationRepository
            .findById(sourceApplicationDto.getId())
            .map(existingSourceApplication -> {
                sourceApplicationMapper.partialUpdate(existingSourceApplication, sourceApplicationDto);

                return existingSourceApplication;
            })
            .map(sourceApplicationRepository::save)
            .map(sourceApplicationMapper::toDto);
    }

    /**
     * Get all the sourceApplications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SourceApplicationDto> findAll() {
        LOG.debug("Request to get all SourceApplications");
        return sourceApplicationRepository
            .findAll()
            .stream()
            .map(sourceApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sourceApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SourceApplicationDto> findOne(UUID id) {
        LOG.debug("Request to get SourceApplication : {}", id);
        return sourceApplicationRepository.findById(id).map(sourceApplicationMapper::toDto);
    }

    /**
     * Delete the sourceApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete SourceApplication : {}", id);
        sourceApplicationRepository.deleteById(id);
    }
}
