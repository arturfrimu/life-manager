package com.arturfrimu.lifemanager.repository;

import com.arturfrimu.lifemanager.entity.User;
import com.arturfrimu.lifemanager.entity.WorkoutExercise;
import com.arturfrimu.lifemanager.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {
    List<WorkoutSession> findByUser(User user);
    List<WorkoutSession> findByUserAndCompletedAtIsNotNull(User user);
    List<WorkoutSession> findByUserAndCompletedAtIsNull(User user);
    List<WorkoutSession> findByUserAndStartedAtBetween(User user, Instant start, Instant end);
    
    @Query("SELECT DISTINCT ws FROM WorkoutSession ws " +
           "LEFT JOIN FETCH ws.workoutExercises we " +
           "LEFT JOIN FETCH we.exercise " +
           "WHERE ws.id = :id")
    Optional<WorkoutSession> findByIdWithExercises(@Param("id") UUID id);
    
    @Query("SELECT DISTINCT we FROM WorkoutExercise we " +
           "LEFT JOIN FETCH we.sets " +
           "WHERE we.workoutSession.id = :workoutSessionId")
    List<WorkoutExercise> findWorkoutExercisesWithSets(@Param("workoutSessionId") UUID workoutSessionId);
}

