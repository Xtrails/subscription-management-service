package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ExternalUserDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalUserDto implements Serializable {

    private UUID id;

    @NotNull
    private String externalUserId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalUserDto)) {
            return false;
        }

        ExternalUserDto externalUserDto = (ExternalUserDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, externalUserDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalUserDto{" +
            "id='" + getId() + "'" +
            ", externalUserId='" + getExternalUserId() + "'" +
            "}";
    }
}
