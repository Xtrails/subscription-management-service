package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUser;
import ru.ani.subscription.management.service.domain.ReferralProgram;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDTO;

/**
 * Mapper for the entity {@link ExternalUser} and its DTO {@link ExternalUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalUserMapper extends EntityMapper<ExternalUserDTO, ExternalUser> {
    @Mapping(target = "referralCreator", source = "referralCreator", qualifiedByName = "referralProgramId")
    @Mapping(target = "referralProgram", source = "referralProgram", qualifiedByName = "referralProgramId")
    ExternalUserDTO toDto(ExternalUser s);

    @Named("referralProgramId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReferralProgramDTO toDtoReferralProgramId(ReferralProgram referralProgram);
}
