package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;

/**
 * A ClientSubscriptionDao.
 */
@Entity
@Table(name = "client_subscription", schema = "subscription_management_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientSubscriptionDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private UUID id;

    @NotNull
    @Column(name = "start_dttm", nullable = false)
    private LocalDate startDttm;

    @NotNull
    @Column(name = "end_dttm", nullable = false)
    private LocalDate endDttm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExternalUserDao user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplication", "subscriptionAccesses" }, allowSetters = true)
    private SubscriptionDetailsDao subscriptionDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ClientSubscriptionDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDttm() {
        return this.startDttm;
    }

    public ClientSubscriptionDao startDttm(LocalDate startDttm) {
        this.setStartDttm(startDttm);
        return this;
    }

    public void setStartDttm(LocalDate startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDate getEndDttm() {
        return this.endDttm;
    }

    public ClientSubscriptionDao endDttm(LocalDate endDttm) {
        this.setEndDttm(endDttm);
        return this;
    }

    public void setEndDttm(LocalDate endDttm) {
        this.endDttm = endDttm;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public ClientSubscriptionDao status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public ExternalUserDao getUser() {
        return this.user;
    }

    public void setUser(ExternalUserDao externalUser) {
        this.user = externalUser;
    }

    public ClientSubscriptionDao user(ExternalUserDao externalUser) {
        this.setUser(externalUser);
        return this;
    }

    public SubscriptionDetailsDao getSubscriptionDetails() {
        return this.subscriptionDetails;
    }

    public void setSubscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    public ClientSubscriptionDao subscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.setSubscriptionDetails(subscriptionDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientSubscriptionDao)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientSubscriptionDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientSubscriptionDao{" +
            "id=" + getId() +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
