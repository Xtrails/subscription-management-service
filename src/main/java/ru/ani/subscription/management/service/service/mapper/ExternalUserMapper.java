package ru.ani.subscription.management.service.service.mapper;

import org.mapstruct.*;
import ru.ani.subscription.management.service.domain.ExternalUserDao;
import ru.ani.subscription.management.service.service.dto.ExternalUserDto;

/**
 * Mapper for the entity {@link ExternalUserDao} and its DTO {@link ExternalUserDto}.
 */
@Mapper(componentModel = "spring")
public interface ExternalUserMapper extends EntityMapper<ExternalUserDto, ExternalUserDao> {}
