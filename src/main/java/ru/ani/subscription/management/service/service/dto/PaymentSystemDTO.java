package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.PaymentSystem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentSystemDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentSystemDTO)) {
            return false;
        }

        PaymentSystemDTO paymentSystemDTO = (PaymentSystemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentSystemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentSystemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
