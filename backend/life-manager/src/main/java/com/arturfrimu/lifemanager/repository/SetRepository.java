package com.arturfrimu.lifemanager.repository;

import com.arturfrimu.lifemanager.entity.Set;
import com.arturfrimu.lifemanager.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SetRepository extends JpaRepository<Set, UUID> {
    List<Set> findByWorkoutExercise(WorkoutExercise workoutExercise);
    List<Set> findByWorkoutExerciseOrderBySetIndexAsc(WorkoutExercise workoutExercise);
    List<Set> findByWorkoutExerciseAndCompleted(WorkoutExercise workoutExercise, Boolean completed);
    
    @Query("SELECT MAX(s.setIndex) FROM Set s WHERE s.workoutExercise.id = :workoutExerciseId")
    Optional<Integer> findMaxSetIndexByWorkoutExerciseId(@Param("workoutExerciseId") UUID workoutExerciseId);
}

