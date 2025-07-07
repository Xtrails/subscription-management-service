package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.SourceApplication;
import ru.ani.subscription.management.service.repository.SourceApplicationRepository;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDTO;
import ru.ani.subscription.management.service.service.mapper.SourceApplicationMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.SourceApplication}.
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
     * @param sourceApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceApplicationDTO save(SourceApplicationDTO sourceApplicationDTO) {
        LOG.debug("Request to save SourceApplication : {}", sourceApplicationDTO);
        SourceApplication sourceApplication = sourceApplicationMapper.toEntity(sourceApplicationDTO);
        sourceApplication = sourceApplicationRepository.save(sourceApplication);
        return sourceApplicationMapper.toDto(sourceApplication);
    }

    /**
     * Update a sourceApplication.
     *
     * @param sourceApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceApplicationDTO update(SourceApplicationDTO sourceApplicationDTO) {
        LOG.debug("Request to update SourceApplication : {}", sourceApplicationDTO);
        SourceApplication sourceApplication = sourceApplicationMapper.toEntity(sourceApplicationDTO);
        sourceApplication = sourceApplicationRepository.save(sourceApplication);
        return sourceApplicationMapper.toDto(sourceApplication);
    }

    /**
     * Partially update a sourceApplication.
     *
     * @param sourceApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SourceApplicationDTO> partialUpdate(SourceApplicationDTO sourceApplicationDTO) {
        LOG.debug("Request to partially update SourceApplication : {}", sourceApplicationDTO);

        return sourceApplicationRepository
            .findById(sourceApplicationDTO.getId())
            .map(existingSourceApplication -> {
                sourceApplicationMapper.partialUpdate(existingSourceApplication, sourceApplicationDTO);

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
    public List<SourceApplicationDTO> findAll() {
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
    public Optional<SourceApplicationDTO> findOne(Long id) {
        LOG.debug("Request to get SourceApplication : {}", id);
        return sourceApplicationRepository.findById(id).map(sourceApplicationMapper::toDto);
    }

    /**
     * Delete the sourceApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SourceApplication : {}", id);
        sourceApplicationRepository.deleteById(id);
    }
}
