package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.PaymentStatus;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @Column(name = "payment_dttm", nullable = false)
    private LocalDate paymentDttm;

    @NotNull
    @Column(name = "hash_sum", nullable = false)
    private String hashSum;

    @JsonIgnoreProperties(value = { "user", "subscriptionDetails", "payment" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ClientSubscription clientSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    private ExternalUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "subscriptionDetails", "payment" }, allowSetters = true)
    private ClientSubscription clietntSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentSystem paymentSystem;

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

    public LocalDate getPaymentDttm() {
        return this.paymentDttm;
    }

    public Payment paymentDttm(LocalDate paymentDttm) {
        this.setPaymentDttm(paymentDttm);
        return this;
    }

    public void setPaymentDttm(LocalDate paymentDttm) {
        this.paymentDttm = paymentDttm;
    }

    public String getHashSum() {
        return this.hashSum;
    }

    public Payment hashSum(String hashSum) {
        this.setHashSum(hashSum);
        return this;
    }

    public void setHashSum(String hashSum) {
        this.hashSum = hashSum;
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

    public ClientSubscription getClietntSubscription() {
        return this.clietntSubscription;
    }

    public void setClietntSubscription(ClientSubscription clientSubscription) {
        this.clietntSubscription = clientSubscription;
    }

    public Payment clietntSubscription(ClientSubscription clientSubscription) {
        this.setClietntSubscription(clientSubscription);
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
            ", paymentDttm='" + getPaymentDttm() + "'" +
            ", hashSum='" + getHashSum() + "'" +
            "}";
    }
}
