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
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.repository.ExternalUserRepository;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.mapper.ExternalUserMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ExternalUserDao}.
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
     * @param externalUserDto the entity to save.
     * @return the persisted entity.
     */
    public ExternalUserDto save(ExternalUserDto externalUserDto) {
        LOG.debug("Request to save ExternalUser : {}", externalUserDto);
        ExternalUserDao externalUserDao = externalUserMapper.toEntity(externalUserDto);
        externalUserDao = externalUserRepository.save(externalUserDao);
        return externalUserMapper.toDto(externalUserDao);
    }

    /**
     * Update a externalUser.
     *
     * @param externalUserDto the entity to save.
     * @return the persisted entity.
     */
    public ExternalUserDto update(ExternalUserDto externalUserDto) {
        LOG.debug("Request to update ExternalUser : {}", externalUserDto);
        ExternalUserDao externalUserDao = externalUserMapper.toEntity(externalUserDto);
        externalUserDao = externalUserRepository.save(externalUserDao);
        return externalUserMapper.toDto(externalUserDao);
    }

    /**
     * Partially update a externalUser.
     *
     * @param externalUserDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExternalUserDto> partialUpdate(ExternalUserDto externalUserDto) {
        LOG.debug("Request to partially update ExternalUser : {}", externalUserDto);

        return externalUserRepository
            .findById(externalUserDto.getId())
            .map(existingExternalUser -> {
                externalUserMapper.partialUpdate(existingExternalUser, externalUserDto);

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
    public List<ExternalUserDto> findAll() {
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
    public Optional<ExternalUserDto> findOne(UUID id) {
        LOG.debug("Request to get ExternalUser : {}", id);
        return externalUserRepository.findById(id).map(externalUserMapper::toDto);
    }

    /**
     * Delete the externalUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete ExternalUser : {}", id);
        externalUserRepository.deleteById(id);
    }
}
