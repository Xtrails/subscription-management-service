package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ReferralProgram;
import ru.ani.subscription.management.service.domain.SourceApplication;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDTO;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDTO;

/**
 * Mapper for the entity {@link ReferralProgram} and its DTO {@link ReferralProgramDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReferralProgramMapper extends EntityMapper<ReferralProgramDTO, ReferralProgram> {
    @Mapping(target = "sourceApplication", source = "sourceApplication", qualifiedByName = "sourceApplicationId")
    ReferralProgramDTO toDto(ReferralProgram s);

    @Named("sourceApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceApplicationDTO toDtoSourceApplicationId(SourceApplication sourceApplication);
}
