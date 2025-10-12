package com.arturfrimu.lifemanager.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "workout_exercise", schema = "life_manager")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseEntity extends BaseEntity {
    @EmbeddedId
    private WorkoutExerciseId id;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}