package com.desafio.votacao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agenda")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class AgendaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Transient
    private boolean active;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<VoteEntity> votes = new HashSet<>();

    @Column(name = "duration")
    private Long duration;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @Column(name = "deadline", columnDefinition = "DATETIME(6)")
    private LocalDateTime deadline;

    public AgendaEntity() {
    }

    public AgendaEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(Set<VoteEntity> votes) {
        this.votes = votes;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getDuration() {
        return duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isActive() {
        return deadline != null && LocalDateTime.now().isBefore(this.deadline);
    }

    public void setDuration(Long duration) {
        this.duration = ObjectUtils.isNotEmpty(duration) ? duration : 1 ;
    }

    @PreUpdate
    protected void onUpdate() {
        if(ObjectUtils.isEmpty(this.deadline) && ObjectUtils.isNotEmpty(this.duration)) {
            this.deadline = LocalDateTime.now().plusMinutes(this.duration);
        }
    }

    @Override
    public String toString() {
        return "AgendaEntity{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", votes=" + votes +
                ", duration=" + duration +
                ", createdAt=" + createdAt +
                ", deadline=" + deadline +
                '}';
    }
}
