package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import ru.ani.subscription.management.service.domain.enumeration.ReferralStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ReferralProgramDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralProgramDto implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String referralCode;

    private String description;

    @NotNull
    private LocalDate startDttm;

    @NotNull
    private LocalDate endDttm;

    @NotNull
    private BigDecimal rewardAmount;

    @NotNull
    private ReferralStatus status;

    private SourceApplicationDto sourceApplication;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferralStatus getStatus() {
        return status;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public SourceApplicationDto getSourceApplication() {
        return sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDto sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralProgramDto)) {
            return false;
        }

        ReferralProgramDto referralProgramDto = (ReferralProgramDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, referralProgramDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralProgramDto{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", referralCode='" + getReferralCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", rewardAmount=" + getRewardAmount() +
            ", status='" + getStatus() + "'" +
            ", sourceApplication=" + getSourceApplication() +
            "}";
    }
}
