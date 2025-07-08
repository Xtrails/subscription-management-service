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
import ru.ani.subscription.management.service.domain.ReferralProgramDao;
import ru.ani.subscription.management.service.repository.ReferralProgramRepository;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDto;
import ru.ani.subscription.management.service.service.mapper.ReferralProgramMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ReferralProgramDao}.
 */
@Service
@Transactional
public class ReferralProgramService {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralProgramService.class);

    private final ReferralProgramRepository referralProgramRepository;

    private final ReferralProgramMapper referralProgramMapper;

    public ReferralProgramService(ReferralProgramRepository referralProgramRepository, ReferralProgramMapper referralProgramMapper) {
        this.referralProgramRepository = referralProgramRepository;
        this.referralProgramMapper = referralProgramMapper;
    }

    /**
     * Save a referralProgram.
     *
     * @param referralProgramDto the entity to save.
     * @return the persisted entity.
     */
    public ReferralProgramDto save(ReferralProgramDto referralProgramDto) {
        LOG.debug("Request to save ReferralProgram : {}", referralProgramDto);
        ReferralProgramDao referralProgramDao = referralProgramMapper.toEntity(referralProgramDto);
        referralProgramDao = referralProgramRepository.save(referralProgramDao);
        return referralProgramMapper.toDto(referralProgramDao);
    }

    /**
     * Update a referralProgram.
     *
     * @param referralProgramDto the entity to save.
     * @return the persisted entity.
     */
    public ReferralProgramDto update(ReferralProgramDto referralProgramDto) {
        LOG.debug("Request to update ReferralProgram : {}", referralProgramDto);
        ReferralProgramDao referralProgramDao = referralProgramMapper.toEntity(referralProgramDto);
        referralProgramDao = referralProgramRepository.save(referralProgramDao);
        return referralProgramMapper.toDto(referralProgramDao);
    }

    /**
     * Partially update a referralProgram.
     *
     * @param referralProgramDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReferralProgramDto> partialUpdate(ReferralProgramDto referralProgramDto) {
        LOG.debug("Request to partially update ReferralProgram : {}", referralProgramDto);

        return referralProgramRepository
            .findById(referralProgramDto.getId())
            .map(existingReferralProgram -> {
                referralProgramMapper.partialUpdate(existingReferralProgram, referralProgramDto);

                return existingReferralProgram;
            })
            .map(referralProgramRepository::save)
            .map(referralProgramMapper::toDto);
    }

    /**
     * Get all the referralPrograms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReferralProgramDto> findAll() {
        LOG.debug("Request to get all ReferralPrograms");
        return referralProgramRepository
            .findAll()
            .stream()
            .map(referralProgramMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one referralProgram by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReferralProgramDto> findOne(UUID id) {
        LOG.debug("Request to get ReferralProgram : {}", id);
        return referralProgramRepository.findById(id).map(referralProgramMapper::toDto);
    }

    /**
     * Delete the referralProgram by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete ReferralProgram : {}", id);
        referralProgramRepository.deleteById(id);
    }
}
