package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.ExternalUser;
import ru.ani.subscription.management.service.repository.ExternalUserRepository;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.service.mapper.ExternalUserMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ExternalUser}.
 */
@Service
@Transactional
public class ExternalUserService {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalUserService.class);

    private final ExternalUserRepository externalUserRepository;

    private final ExternalUserMapper externalUserMapper;

    public ExternalUserService(ExternalUserRepository externalUserRepository, ExternalUserMapper externalUserMapper) {
        this.externalUserRepository = externalUserRepository;
        this.externalUserMapper = externalUserMapper;
    }

    /**
     * Save a externalUser.
     *
     * @param externalUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalUserDTO save(ExternalUserDTO externalUserDTO) {
        LOG.debug("Request to save ExternalUser : {}", externalUserDTO);
        ExternalUser externalUser = externalUserMapper.toEntity(externalUserDTO);
        externalUser = externalUserRepository.save(externalUser);
        return externalUserMapper.toDto(externalUser);
    }

    /**
     * Update a externalUser.
     *
     * @param externalUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalUserDTO update(ExternalUserDTO externalUserDTO) {
        LOG.debug("Request to update ExternalUser : {}", externalUserDTO);
        ExternalUser externalUser = externalUserMapper.toEntity(externalUserDTO);
        externalUser = externalUserRepository.save(externalUser);
        return externalUserMapper.toDto(externalUser);
    }

    /**
     * Partially update a externalUser.
     *
     * @param externalUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExternalUserDTO> partialUpdate(ExternalUserDTO externalUserDTO) {
        LOG.debug("Request to partially update ExternalUser : {}", externalUserDTO);

        return externalUserRepository
            .findById(externalUserDTO.getId())
            .map(existingExternalUser -> {
                externalUserMapper.partialUpdate(existingExternalUser, externalUserDTO);

                return existingExternalUser;
            })
            .map(externalUserRepository::save)
            .map(externalUserMapper::toDto);
    }

    /**
     * Get all the externalUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExternalUserDTO> findAll() {
        LOG.debug("Request to get all ExternalUsers");
        return externalUserRepository.findAll().stream().map(externalUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one externalUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExternalUserDTO> findOne(Long id) {
        LOG.debug("Request to get ExternalUser : {}", id);
        return externalUserRepository.findById(id).map(externalUserMapper::toDto);
    }

    /**
     * Delete the externalUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExternalUser : {}", id);
        externalUserRepository.deleteById(id);
    }
}
