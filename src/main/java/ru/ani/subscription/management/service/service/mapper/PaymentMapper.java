package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ClientSubscription;
import ru.ani.subscription.management.service.domain.ExternalUser;
import ru.ani.subscription.management.service.domain.Payment;
import ru.ani.subscription.management.service.domain.PaymentSystem;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDTO;
import ru.ani.subscription.management.service.service.dto.ExternalUserDTO;
import ru.ani.subscription.management.service.service.dto.PaymentDTO;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDTO;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "clientSubscription", source = "clientSubscription", qualifiedByName = "clientSubscriptionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    @Mapping(target = "clietntSubscription", source = "clietntSubscription", qualifiedByName = "clientSubscriptionId")
    @Mapping(target = "paymentSystem", source = "paymentSystem", qualifiedByName = "paymentSystemId")
    PaymentDTO toDto(Payment s);

    @Named("clientSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientSubscriptionDTO toDtoClientSubscriptionId(ClientSubscription clientSubscription);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDTO toDtoExternalUserId(ExternalUser externalUser);

    @Named("paymentSystemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentSystemDTO toDtoPaymentSystemId(PaymentSystem paymentSystem);
}
