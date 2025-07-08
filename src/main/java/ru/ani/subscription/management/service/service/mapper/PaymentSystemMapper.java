package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.PaymentSystemDao;
import ru.ani.subscription.management.service.service.dto.PaymentSystemDto;

/**
 * Mapper for the entity {@link PaymentSystemDao} and its DTO {@link PaymentSystemDto}.
 */
@Mapper(componentModel = "spring")
public interface PaymentSystemMapper extends EntityMapper<PaymentSystemDto, PaymentSystemDao> {}
