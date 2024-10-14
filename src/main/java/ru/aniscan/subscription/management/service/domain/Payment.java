package ru.aniscan.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.aniscan.subscription.management.service.domain.enumeration.PaymentStatus;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private ExternalUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "subscriptionType", "sourceApplication" }, allowSetters = true)
    private ClientSubscription clientSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplications" }, allowSetters = true)
    private PaymentSystem paymentSystem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionTypes", "users", "paymentSystems" }, allowSetters = true)
    private SourceApplication sourceApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public Payment status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ExternalUser getUser() {
        return this.user;
    }

    public void setUser(ExternalUser externalUser) {
        this.user = externalUser;
    }

    public Payment user(ExternalUser externalUser) {
        this.setUser(externalUser);
        return this;
    }

    public ClientSubscription getClientSubscription() {
        return this.clientSubscription;
    }

    public void setClientSubscription(ClientSubscription clientSubscription) {
        this.clientSubscription = clientSubscription;
    }

    public Payment clientSubscription(ClientSubscription clientSubscription) {
        this.setClientSubscription(clientSubscription);
        return this;
    }

    public PaymentSystem getPaymentSystem() {
        return this.paymentSystem;
    }

    public void setPaymentSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public Payment paymentSystem(PaymentSystem paymentSystem) {
        this.setPaymentSystem(paymentSystem);
        return this;
    }

    public SourceApplication getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public Payment sourceApplication(SourceApplication sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            "}";
    }
}
