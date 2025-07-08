package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.domain.ReferralProgramDao;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDto;

/**
 * Mapper for the entity {@link ExternalUserDao} and its DTO {@link ExternalUserDto}.
 */
@Mapper(componentModel = "spring")
public interface ExternalUserMapper extends EntityMapper<ExternalUserDto, ExternalUserDao> {
    @Mapping(target = "referralCreator", source = "referralCreator", qualifiedByName = "referralProgramId")
    @Mapping(target = "referralProgram", source = "referralProgram", qualifiedByName = "referralProgramId")
    ExternalUserDto toDto(ExternalUserDao s);

    @Named("referralProgramId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReferralProgramDto toDtoReferralProgramId(ReferralProgramDao referralProgramDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
