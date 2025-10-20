package com.arturfrimu.lifemanager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Table(
    name = "set",
    schema = "life_manager",
    uniqueConstraints = @UniqueConstraint(columnNames = {"workout_exercise_id", "set_index"})
)
public class Set extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "set_index", nullable = false)
    private Integer setIndex;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "distance_meters", precision = 10, scale = 2)
    private BigDecimal distanceMeters;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;
}
