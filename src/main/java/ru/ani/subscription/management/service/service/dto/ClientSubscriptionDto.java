package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ClientSubscriptionDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientSubscriptionDto implements Serializable {

    private UUID id;

    @NotNull
    private LocalDate startDttm;

    @NotNull
    private LocalDate endDttm;

    @NotNull
    private SubscriptionStatus status;

    private ExternalUserDto user;

    private SubscriptionDetailsDto subscriptionDetails;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDttm() {
        return startDttm;
    }

    public void setStartDttm(LocalDate startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDate getEndDttm() {
        return endDttm;
    }

    public void setEndDttm(LocalDate endDttm) {
        this.endDttm = endDttm;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public ExternalUserDto getUser() {
        return user;
    }

    public void setUser(ExternalUserDto user) {
        this.user = user;
    }

    public SubscriptionDetailsDto getSubscriptionDetails() {
        return subscriptionDetails;
    }

    public void setSubscriptionDetails(SubscriptionDetailsDto subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientSubscriptionDto)) {
            return false;
        }

        ClientSubscriptionDto clientSubscriptionDto = (ClientSubscriptionDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientSubscriptionDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientSubscriptionDto{" +
            "id='" + getId() + "'" +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", status='" + getStatus() + "'" +
            ", user=" + getUser() +
            ", subscriptionDetails=" + getSubscriptionDetails() +
            "}";
    }
}
