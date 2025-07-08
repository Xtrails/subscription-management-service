package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;

/**
 * Mapper for the entity {@link SubscriptionDetailsDao} and its DTO {@link SubscriptionDetailsDto}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionDetailsMapper extends EntityMapper<SubscriptionDetailsDto, SubscriptionDetailsDao> {
    @Mapping(target = "sourceApplication", source = "sourceApplication", qualifiedByName = "sourceApplicationId")
    SubscriptionDetailsDto toDto(SubscriptionDetailsDao s);

    @Named("sourceApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceApplicationDto toDtoSourceApplicationId(SourceApplicationDao sourceApplicationDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
