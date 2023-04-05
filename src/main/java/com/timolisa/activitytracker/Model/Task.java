package com.timolisa.activitytracker.Model;

import com.timolisa.activitytracker.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private long id;

    @Column(name = "title")
    private java.lang.String title;

    @Column(name = "description")
    private java.lang.String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
