package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.SubscriptionStatus;

/**
 * A ClientSubscription.
 */
@Entity
@Table(name = "client_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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
    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    private ExternalUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private SubscriptionDetails subscriptionDetails;

    @JsonIgnoreProperties(value = { "clientSubscription", "user", "clietntSubscription", "paymentSystem" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "clientSubscription")
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClientSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDttm() {
        return this.startDttm;
    }

    public ClientSubscription startDttm(LocalDate startDttm) {
        this.setStartDttm(startDttm);
        return this;
    }

    public void setStartDttm(LocalDate startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDate getEndDttm() {
        return this.endDttm;
    }

    public ClientSubscription endDttm(LocalDate endDttm) {
        this.setEndDttm(endDttm);
        return this;
    }

    public void setEndDttm(LocalDate endDttm) {
        this.endDttm = endDttm;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public ClientSubscription status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public ExternalUser getUser() {
        return this.user;
    }

    public void setUser(ExternalUser externalUser) {
        this.user = externalUser;
    }

    public ClientSubscription user(ExternalUser externalUser) {
        this.setUser(externalUser);
        return this;
    }

    public SubscriptionDetails getSubscriptionDetails() {
        return this.subscriptionDetails;
    }

    public void setSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    public ClientSubscription subscriptionDetails(SubscriptionDetails subscriptionDetails) {
        this.setSubscriptionDetails(subscriptionDetails);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setClientSubscription(null);
        }
        if (payment != null) {
            payment.setClientSubscription(this);
        }
        this.payment = payment;
    }

    public ClientSubscription payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientSubscription{" +
            "id=" + getId() +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
