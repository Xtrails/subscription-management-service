package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.SubscriptionAccessDao;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.service.dto.SubscriptionAccessDto;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;

/**
 * Mapper for the entity {@link SubscriptionAccessDao} and its DTO {@link SubscriptionAccessDto}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionAccessMapper extends EntityMapper<SubscriptionAccessDto, SubscriptionAccessDao> {
    @Mapping(target = "subscriptionDetails", source = "subscriptionDetails", qualifiedByName = "subscriptionDetailsIdSet")
    SubscriptionAccessDto toDto(SubscriptionAccessDao s);

    @Mapping(target = "removeSubscriptionDetails", ignore = true)
    SubscriptionAccessDao toEntity(SubscriptionAccessDto subscriptionAccessDto);

    @Named("subscriptionDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionDetailsDto toDtoSubscriptionDetailsId(SubscriptionDetailsDao subscriptionDetailsDao);

    @Named("subscriptionDetailsIdSet")
    default Set<SubscriptionDetailsDto> toDtoSubscriptionDetailsIdSet(Set<SubscriptionDetailsDao> subscriptionDetailsDao) {
        return subscriptionDetailsDao.stream().map(this::toDtoSubscriptionDetailsId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
