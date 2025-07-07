package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUser;
import ru.ani.subscription.management.service.domain.SourceApplication;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDTO;

/**
 * Mapper for the entity {@link SourceApplication} and its DTO {@link SourceApplicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface SourceApplicationMapper extends EntityMapper<SourceApplicationDTO, SourceApplication> {
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    SourceApplicationDTO toDto(SourceApplication s);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDTO toDtoExternalUserId(ExternalUser externalUser);
}
