package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.SourceApplication;
import ru.ani.subscription.management.service.domain.SubscriptionDetails;
import ru.ani.subscription.management.service.service.dto.SourceApplicationDTO;
import ru.ani.subscription.management.service.service.dto.SubscriptionDetailsDTO;

/**
 * Mapper for the entity {@link SubscriptionDetails} and its DTO {@link SubscriptionDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionDetailsMapper extends EntityMapper<SubscriptionDetailsDTO, SubscriptionDetails> {
    @Mapping(target = "sourceApplication", source = "sourceApplication", qualifiedByName = "sourceApplicationId")
    SubscriptionDetailsDTO toDto(SubscriptionDetails s);

    @Named("sourceApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceApplicationDTO toDtoSourceApplicationId(SourceApplication sourceApplication);
}
