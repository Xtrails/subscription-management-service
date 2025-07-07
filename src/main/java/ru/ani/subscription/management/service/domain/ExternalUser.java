package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExternalUser.
 */
@Entity
@Table(name = "external_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "external_user_id", nullable = false, unique = true)
    private String externalUserId;

    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ReferralProgram referralCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    private ReferralProgram referralProgram;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public ExternalUser externalUserId(String externalUserId) {
        this.setExternalUserId(externalUserId);
        return this;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public ReferralProgram getReferralCreator() {
        return this.referralCreator;
    }

    public void setReferralCreator(ReferralProgram referralProgram) {
        this.referralCreator = referralProgram;
    }

    public ExternalUser referralCreator(ReferralProgram referralProgram) {
        this.setReferralCreator(referralProgram);
        return this;
    }

    public ReferralProgram getReferralProgram() {
        return this.referralProgram;
    }

    public void setReferralProgram(ReferralProgram referralProgram) {
        this.referralProgram = referralProgram;
    }

    public ExternalUser referralProgram(ReferralProgram referralProgram) {
        this.setReferralProgram(referralProgram);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalUser{" +
            "id=" + getId() +
            ", externalUserId='" + getExternalUserId() + "'" +
            "}";
    }
}
