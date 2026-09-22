package com.desafio.votacao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Getter
    @Column(name = "description", length = 4000)
    private String description;

    @Setter
    @Transient
    private boolean active;

    @Setter
    @Getter
    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<VoteEntity> votes = new HashSet<>();

    @Getter
    @Column(name = "duration")
    private Long duration;

    @Setter
    @Getter
    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @Setter
    @Getter
    @Column(name = "deadline", columnDefinition = "DATETIME(6)")
    private LocalDateTime deadline;

    public AgendaEntity() {
    }

    public AgendaEntity(String title, String description) {
        this.title = title;
        this.description = description;
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
