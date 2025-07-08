package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.domain.ReferralProgramDao;
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.dto.ReferralProgramDto;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;

/**
 * Mapper for the entity {@link ReferralProgramDao} and its DTO {@link ReferralProgramDto}.
 */
@Mapper(componentModel = "spring")
public interface ReferralProgramMapper extends EntityMapper<ReferralProgramDto, ReferralProgramDao> {
    @Mapping(target = "referralCreator", source = "referralCreator", qualifiedByName = "externalUserId")
    @Mapping(target = "sourceApplication", source = "sourceApplication", qualifiedByName = "sourceApplicationId")
    ReferralProgramDto toDto(ReferralProgramDao s);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDto toDtoExternalUserId(ExternalUserDao externalUserDao);

    @Named("sourceApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceApplicationDto toDtoSourceApplicationId(SourceApplicationDao sourceApplicationDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
