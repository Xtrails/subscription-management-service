package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ClientSubscription;
import ru.ani.subscription.management.service.domain.ExternalUser;
import ru.ani.subscription.management.service.domain.SubscriptionDetails;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDTO;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDTO;

/**
 * Mapper for the entity {@link ClientSubscription} and its DTO {@link ClientSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientSubscriptionMapper extends EntityMapper<ClientSubscriptionDTO, ClientSubscription> {
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    @Mapping(target = "subscriptionDetails", source = "subscriptionDetails", qualifiedByName = "subscriptionDetailsId")
    ClientSubscriptionDTO toDto(ClientSubscription s);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDTO toDtoExternalUserId(ExternalUser externalUser);

    @Named("subscriptionDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionDetailsDTO toDtoSubscriptionDetailsId(SubscriptionDetails subscriptionDetails);
}
