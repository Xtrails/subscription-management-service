package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import ru.ani.subscription.management.service.domain.enumeration.ReferralStatus;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.ReferralProgram} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralProgramDTO implements Serializable {

    private Long id;

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

    private SourceApplicationDTO sourceApplication;

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

    public SourceApplicationDTO getSourceApplication() {
        return sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDTO sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralProgramDTO)) {
            return false;
        }

        ReferralProgramDTO referralProgramDTO = (ReferralProgramDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, referralProgramDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralProgramDTO{" +
            "id=" + getId() +
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
