package com.arturfrimu.lifemanager.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WorkoutExerciseId implements Serializable {
    @Column(name = "workout_id", nullable = false)
    private UUID workoutId;
    @Column(name = "exercise_id", nullable = false)
    private UUID exerciseId;
}
