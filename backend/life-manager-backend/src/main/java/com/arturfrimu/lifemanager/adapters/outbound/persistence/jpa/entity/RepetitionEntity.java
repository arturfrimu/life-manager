package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repetition", schema = "life_manager")
public class RepetitionEntity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Embedded
    private WorkoutExerciseId workoutExerciseId;

    @Column(name = "weight")
    private BigInteger weight;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "executed_at")
    private Instant executedAt;
}

