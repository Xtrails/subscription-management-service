package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import ru.ani.subscription.management.service.domain.enumeration.PaymentStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private LocalDate paymentDttm;

    @NotNull
    private String hashSum;

    private ClientSubscriptionDTO clientSubscription;

    private ExternalUserDTO user;

    private ClientSubscriptionDTO clietntSubscription;

    private PaymentSystemDTO paymentSystem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDttm() {
        return paymentDttm;
    }

    public void setPaymentDttm(LocalDate paymentDttm) {
        this.paymentDttm = paymentDttm;
    }

    public String getHashSum() {
        return hashSum;
    }

    public void setHashSum(String hashSum) {
        this.hashSum = hashSum;
    }

    public ClientSubscriptionDTO getClientSubscription() {
        return clientSubscription;
    }

    public void setClientSubscription(ClientSubscriptionDTO clientSubscription) {
        this.clientSubscription = clientSubscription;
    }

    public ExternalUserDTO getUser() {
        return user;
    }

    public void setUser(ExternalUserDTO user) {
        this.user = user;
    }

    public ClientSubscriptionDTO getClietntSubscription() {
        return clietntSubscription;
    }

    public void setClietntSubscription(ClientSubscriptionDTO clietntSubscription) {
        this.clietntSubscription = clietntSubscription;
    }

    public PaymentSystemDTO getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(PaymentSystemDTO paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentDttm='" + getPaymentDttm() + "'" +
            ", hashSum='" + getHashSum() + "'" +
            ", clientSubscription=" + getClientSubscription() +
            ", user=" + getUser() +
            ", clietntSubscription=" + getClietntSubscription() +
            ", paymentSystem=" + getPaymentSystem() +
            "}";
    }
}
