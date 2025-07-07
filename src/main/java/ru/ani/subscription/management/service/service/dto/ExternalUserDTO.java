package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ExternalUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String externalUserId;

    private ReferralProgramDTO referralCreator;

    private ReferralProgramDTO referralProgram;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public ReferralProgramDTO getReferralCreator() {
        return referralCreator;
    }

    public void setReferralCreator(ReferralProgramDTO referralCreator) {
        this.referralCreator = referralCreator;
    }

    public ReferralProgramDTO getReferralProgram() {
        return referralProgram;
    }

    public void setReferralProgram(ReferralProgramDTO referralProgram) {
        this.referralProgram = referralProgram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalUserDTO)) {
            return false;
        }

        ExternalUserDTO externalUserDTO = (ExternalUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, externalUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalUserDTO{" +
            "id=" + getId() +
            ", externalUserId='" + getExternalUserId() + "'" +
            ", referralCreator=" + getReferralCreator() +
            ", referralProgram=" + getReferralProgram() +
            "}";
    }
}
