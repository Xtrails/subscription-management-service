package ru.ani.subscription.management.service.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ani.subscription.management.service.domain.ReferralProgram;
import ru.ani.subscription.management.service.repository.ReferralProgramRepository;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDTO;
import ru.ani.subscription.management.service.service.mapper.ReferralProgramMapper;

/**
 * Service Implementation for managing {@link ru.ani.subscription.management.service.domain.ReferralProgram}.
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
     * @param referralProgramDTO the entity to save.
     * @return the persisted entity.
     */
    public ReferralProgramDTO save(ReferralProgramDTO referralProgramDTO) {
        LOG.debug("Request to save ReferralProgram : {}", referralProgramDTO);
        ReferralProgram referralProgram = referralProgramMapper.toEntity(referralProgramDTO);
        referralProgram = referralProgramRepository.save(referralProgram);
        return referralProgramMapper.toDto(referralProgram);
    }

    /**
     * Update a referralProgram.
     *
     * @param referralProgramDTO the entity to save.
     * @return the persisted entity.
     */
    public ReferralProgramDTO update(ReferralProgramDTO referralProgramDTO) {
        LOG.debug("Request to update ReferralProgram : {}", referralProgramDTO);
        ReferralProgram referralProgram = referralProgramMapper.toEntity(referralProgramDTO);
        referralProgram = referralProgramRepository.save(referralProgram);
        return referralProgramMapper.toDto(referralProgram);
    }

    /**
     * Partially update a referralProgram.
     *
     * @param referralProgramDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReferralProgramDTO> partialUpdate(ReferralProgramDTO referralProgramDTO) {
        LOG.debug("Request to partially update ReferralProgram : {}", referralProgramDTO);

        return referralProgramRepository
            .findById(referralProgramDTO.getId())
            .map(existingReferralProgram -> {
                referralProgramMapper.partialUpdate(existingReferralProgram, referralProgramDTO);

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
    public List<ReferralProgramDTO> findAll() {
        LOG.debug("Request to get all ReferralPrograms");
        return referralProgramRepository
            .findAll()
            .stream()
            .map(referralProgramMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the referralPrograms where ExternalUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReferralProgramDTO> findAllWhereExternalUserIsNull() {
        LOG.debug("Request to get all referralPrograms where ExternalUser is null");
        return StreamSupport.stream(referralProgramRepository.findAll().spliterator(), false)
            .filter(referralProgram -> referralProgram.getExternalUser() == null)
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
    public Optional<ReferralProgramDTO> findOne(Long id) {
        LOG.debug("Request to get ReferralProgram : {}", id);
        return referralProgramRepository.findById(id).map(referralProgramMapper::toDto);
    }

    /**
     * Delete the referralProgram by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReferralProgram : {}", id);
        referralProgramRepository.deleteById(id);
    }
}
