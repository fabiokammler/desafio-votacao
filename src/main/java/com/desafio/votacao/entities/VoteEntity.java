package com.desafio.votacao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "votes",
       uniqueConstraints = {
            @UniqueConstraint(columnNames = {"agenda_id", "associate_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class VoteEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    @JsonIgnore
    private AgendaEntity agenda;

    @Column(name = "associate_id", nullable = false)
    private String associate;

    @Column(name = "vote", nullable = false)
    private Boolean voteValue; // "SIM" ou "NAO"

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    public VoteEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgendaEntity getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaEntity agenda) {
        this.agenda = agenda;
    }

    public String getAssociate() {
        return associate;
    }

    public void setAssociate(String associate) {
        this.associate = associate;
    }

    public Boolean getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(Boolean voteValue) {
        this.voteValue = voteValue;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VoteEntity that = (VoteEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(getAgenda(), that.getAgenda())
                && Objects.equals(getAssociate(), that.getAssociate())
                && Objects.equals(getVoteValue(), that.getVoteValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getAgenda(), getAssociate(), getVoteValue());
    }
}
