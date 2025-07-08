package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExternalUserDao.
 */
@Entity
@Table(name = "external_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalUserDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "external_user_id", nullable = false, unique = true)
    private String externalUserId;

    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ReferralProgramDao referralCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    private ReferralProgramDao referralProgram;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ExternalUserDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public ExternalUserDao externalUserId(String externalUserId) {
        this.setExternalUserId(externalUserId);
        return this;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public ReferralProgramDao getReferralCreator() {
        return this.referralCreator;
    }

    public void setReferralCreator(ReferralProgramDao referralProgram) {
        this.referralCreator = referralProgram;
    }

    public ExternalUserDao referralCreator(ReferralProgramDao referralProgram) {
        this.setReferralCreator(referralProgram);
        return this;
    }

    public ReferralProgramDao getReferralProgram() {
        return this.referralProgram;
    }

    public void setReferralProgram(ReferralProgramDao referralProgram) {
        this.referralProgram = referralProgram;
    }

    public ExternalUserDao referralProgram(ReferralProgramDao referralProgram) {
        this.setReferralProgram(referralProgram);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalUserDao)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalUserDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalUserDao{" +
            "id=" + getId() +
            ", externalUserId='" + getExternalUserId() + "'" +
            "}";
    }
}
