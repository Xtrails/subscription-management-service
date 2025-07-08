package ru.ani.subscription.management.service.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ClientSubscriptionDao;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.domain.PaymentDao;
import ru.ani.subscription.management.service.domain.PaymentSystemDao;
import ru.ani.subscription.management.service.service.dto.ClientSubscriptionDto;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;
import ru.ani.subscription.management.service.service.dto.PaymentDto;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDto;

/**
 * Mapper for the entity {@link PaymentDao} and its DTO {@link PaymentDto}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDto, PaymentDao> {
    @Mapping(target = "clientSubscription", source = "clientSubscription", qualifiedByName = "clientSubscriptionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "externalUserId")
    @Mapping(target = "clietntSubscription", source = "clietntSubscription", qualifiedByName = "clientSubscriptionId")
    @Mapping(target = "paymentSystem", source = "paymentSystem", qualifiedByName = "paymentSystemId")
    PaymentDto toDto(PaymentDao s);

    @Named("clientSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientSubscriptionDto toDtoClientSubscriptionId(ClientSubscriptionDao clientSubscriptionDao);

    @Named("externalUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalUserDto toDtoExternalUserId(ExternalUserDao externalUserDao);

    @Named("paymentSystemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentSystemDto toDtoPaymentSystemId(PaymentSystemDao paymentSystemDao);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
