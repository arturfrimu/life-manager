package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workout_exercise", schema = "life_manager")
public class WorkoutExerciseEntity extends BaseEntity {
    @EmbeddedId
    private WorkoutExerciseId id;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}

