package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ClientSubscriptionDao;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.domain.SubscriptionDetailsDao;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDto;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDto;

/**
 * Mapper for the entity {@link ClientSubscriptionDao} and its DTO {@link ClientSubscriptionDto}.
 */
@Mapper(componentModel = "spring")
public interface ClientSubscriptionMapper extends EntityMapper<ClientSubscriptionDto, ClientSubscriptionDao> {
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    @Mapping(target = "subscriptionDetails", source = "subscriptionDetails", qualifiedByName = "subscriptionDetailsId")
    ClientSubscriptionDto toDto(ClientSubscriptionDao s);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDto toDtoExternalUserId(ExternalUserDao externalUserDao);

    @Named("subscriptionDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionDetailsDto toDtoSubscriptionDetailsId(SubscriptionDetailsDao subscriptionDetailsDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
