package com.arturfrimu.lifemanager.repository;

import com.arturfrimu.lifemanager.entity.Exercise;
import com.arturfrimu.lifemanager.entity.WorkoutExercise;
import com.arturfrimu.lifemanager.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {
    List<WorkoutExercise> findByWorkoutSession(WorkoutSession workoutSession);
    List<WorkoutExercise> findByExercise(Exercise exercise);
    List<WorkoutExercise> findByWorkoutSessionOrderByOrderIndexAsc(WorkoutSession workoutSession);
}

