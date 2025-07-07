package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.PaymentSystem;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDTO;

/**
 * Mapper for the entity {@link PaymentSystem} and its DTO {@link PaymentSystemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentSystemMapper extends EntityMapper<PaymentSystemDTO, PaymentSystem> {}
