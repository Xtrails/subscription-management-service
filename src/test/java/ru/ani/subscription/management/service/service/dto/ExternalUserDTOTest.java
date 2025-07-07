package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ExternalUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalUserDTO.class);
        ExternalUserDTO externalUserDTO1 = new ExternalUserDTO();
        externalUserDTO1.setId(1L);
        ExternalUserDTO externalUserDTO2 = new ExternalUserDTO();
        assertThat(externalUserDTO1).isNotEqualTo(externalUserDTO2);
        externalUserDTO2.setId(externalUserDTO1.getId());
        assertThat(externalUserDTO1).isEqualTo(externalUserDTO2);
        externalUserDTO2.setId(2L);
        assertThat(externalUserDTO1).isNotEqualTo(externalUserDTO2);
        externalUserDTO1.setId(null);
        assertThat(externalUserDTO1).isNotEqualTo(externalUserDTO2);
    }
}
