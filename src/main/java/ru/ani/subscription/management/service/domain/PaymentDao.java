package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.PaymentStatus;

/**
 * A PaymentDao.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @NotNull
    @Column(name = "payment_dttm", nullable = false)
    private LocalDate paymentDttm;

    @NotNull
    @Column(name = "hash_sum", nullable = false)
    private String hashSum;

    @JsonIgnoreProperties(value = { "user", "subscriptionDetails", "payment" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ClientSubscriptionDao clientSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    private ExternalUserDao user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "subscriptionDetails", "payment" }, allowSetters = true)
    private ClientSubscriptionDao clietntSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentSystemDao paymentSystem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public PaymentDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public PaymentDao amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public PaymentDao status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDttm() {
        return this.paymentDttm;
    }

    public PaymentDao paymentDttm(LocalDate paymentDttm) {
        this.setPaymentDttm(paymentDttm);
        return this;
    }

    public void setPaymentDttm(LocalDate paymentDttm) {
        this.paymentDttm = paymentDttm;
    }

    public String getHashSum() {
        return this.hashSum;
    }

    public PaymentDao hashSum(String hashSum) {
        this.setHashSum(hashSum);
        return this;
    }

    public void setHashSum(String hashSum) {
        this.hashSum = hashSum;
    }

    public ClientSubscriptionDao getClientSubscription() {
        return this.clientSubscription;
    }

    public void setClientSubscription(ClientSubscriptionDao clientSubscription) {
        this.clientSubscription = clientSubscription;
    }

    public PaymentDao clientSubscription(ClientSubscriptionDao clientSubscription) {
        this.setClientSubscription(clientSubscription);
        return this;
    }

    public ExternalUserDao getUser() {
        return this.user;
    }

    public void setUser(ExternalUserDao externalUser) {
        this.user = externalUser;
    }

    public PaymentDao user(ExternalUserDao externalUser) {
        this.setUser(externalUser);
        return this;
    }

    public ClientSubscriptionDao getClietntSubscription() {
        return this.clietntSubscription;
    }

    public void setClietntSubscription(ClientSubscriptionDao clientSubscription) {
        this.clietntSubscription = clientSubscription;
    }

    public PaymentDao clietntSubscription(ClientSubscriptionDao clientSubscription) {
        this.setClietntSubscription(clientSubscription);
        return this;
    }

    public PaymentSystemDao getPaymentSystem() {
        return this.paymentSystem;
    }

    public void setPaymentSystem(PaymentSystemDao paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public PaymentDao paymentSystem(PaymentSystemDao paymentSystem) {
        this.setPaymentSystem(paymentSystem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDao)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDao{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentDttm='" + getPaymentDttm() + "'" +
            ", hashSum='" + getHashSum() + "'" +
            "}";
    }
}
