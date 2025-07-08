package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.SourceApplicationDao;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDto;
import ru.ani.subscription.management.service.service.dto.SubscriptionAccessDto;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;

/**
 * Mapper for the entity {@link SubscriptionDetailsDao} and its DTO {@link SubscriptionDetailsDto}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionDetailsMapper extends EntityMapper<SubscriptionDetailsDto, SubscriptionDetailsDao> {
    @Mapping(target = "sourceApplication", source = "sourceApplication", qualifiedByName = "sourceApplicationId")
    @Mapping(target = "subscriptionAccesses", source = "subscriptionAccesses", qualifiedByName = "subscriptionAccessIdSet")
    SubscriptionDetailsDto toDto(SubscriptionDetailsDao s);

    @Mapping(target = "subscriptionAccesses", ignore = true)
    @Mapping(target = "removeSubscriptionAccess", ignore = true)
    SubscriptionDetailsDao toEntity(SubscriptionDetailsDto subscriptionDetailsDto);

    @Named("sourceApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceApplicationDto toDtoSourceApplicationId(SourceApplicationDao sourceApplicationDao);

    @Named("subscriptionAccessId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionAccessDto toDtoSubscriptionAccessId(SubscriptionAccessDao subscriptionAccessDao);

    @Named("subscriptionAccessIdSet")
    default Set<SubscriptionAccessDto> toDtoSubscriptionAccessIdSet(Set<SubscriptionAccessDao> subscriptionAccessDao) {
        return subscriptionAccessDao.stream().map(this::toDtoSubscriptionAccessId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
