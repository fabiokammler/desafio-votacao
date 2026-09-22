package com.desafio.votacao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "agenda_id")
    @JsonIgnore
    private AgendaEntity agenda;

    @Setter
    @Getter
    @Column(name = "associate_id", nullable = false)
    private String associate;

    @Setter
    @Getter
    @Column(name = "vote", nullable = false)
    private Boolean voteValue; // "SIM" ou "NAO"

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    public VoteEntity() {
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
