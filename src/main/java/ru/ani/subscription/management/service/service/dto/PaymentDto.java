package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import ru.ani.subscription.management.service.domain.enumeration.PaymentStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.PaymentDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDto implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private LocalDate paymentDttm;

    @NotNull
    private String hashSum;

    private ClientSubscriptionDto clientSubscription;

    private ExternalUserDto user;

    private ClientSubscriptionDto clietntSubscription;

    private PaymentSystemDto paymentSystem;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public ClientSubscriptionDto getClientSubscription() {
        return clientSubscription;
    }

    public void setClientSubscription(ClientSubscriptionDto clientSubscription) {
        this.clientSubscription = clientSubscription;
    }

    public ExternalUserDto getUser() {
        return user;
    }

    public void setUser(ExternalUserDto user) {
        this.user = user;
    }

    public ClientSubscriptionDto getClietntSubscription() {
        return clietntSubscription;
    }

    public void setClietntSubscription(ClientSubscriptionDto clietntSubscription) {
        this.clietntSubscription = clietntSubscription;
    }

    public PaymentSystemDto getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(PaymentSystemDto paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDto)) {
            return false;
        }

        PaymentDto paymentDto = (PaymentDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDto{" +
            "id='" + getId() + "'" +
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
