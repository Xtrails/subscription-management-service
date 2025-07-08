package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;

/**
 * Mapper for the entity {@link SourceApplicationDao} and its DTO {@link SourceApplicationDto}.
 */
@Mapper(componentModel = "spring")
public interface SourceApplicationMapper extends EntityMapper<SourceApplicationDto, SourceApplicationDao> {
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    SourceApplicationDto toDto(SourceApplicationDao s);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDto toDtoExternalUserId(ExternalUserDao externalUserDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
