package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ExternalUserDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalUserDto.class);
        ExternalUserDto externalUserDto1 = new ExternalUserDto();
        externalUserDto1.setId(UUID.randomUUID());
        ExternalUserDto externalUserDto2 = new ExternalUserDto();
        assertThat(externalUserDto1).isNotEqualTo(externalUserDto2);
        externalUserDto2.setId(externalUserDto1.getId());
        assertThat(externalUserDto1).isEqualTo(externalUserDto2);
        externalUserDto2.setId(UUID.randomUUID());
        assertThat(externalUserDto1).isNotEqualTo(externalUserDto2);
        externalUserDto1.setId(null);
        assertThat(externalUserDto1).isNotEqualTo(externalUserDto2);
    }
}
