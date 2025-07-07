package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ClientSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDttm;

    @NotNull
    private LocalDate endDttm;

    @NotNull
    private SubscriptionStatus status;

    private ExternalUserDTO user;

    private SubscriptionDetailsDTO subscriptionDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ExternalUserDTO getUser() {
        return user;
    }

    public void setUser(ExternalUserDTO user) {
        this.user = user;
    }

    public SubscriptionDetailsDTO getSubscriptionDetails() {
        return subscriptionDetails;
    }

    public void setSubscriptionDetails(SubscriptionDetailsDTO subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientSubscriptionDTO)) {
            return false;
        }

        ClientSubscriptionDTO clientSubscriptionDTO = (ClientSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientSubscriptionDTO{" +
            "id=" + getId() +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", status='" + getStatus() + "'" +
            ", user=" + getUser() +
            ", subscriptionDetails=" + getSubscriptionDetails() +
            "}";
    }
}
