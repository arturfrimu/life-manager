package com.arturfrimu.lifemanager.repository;

import com.arturfrimu.lifemanager.entity.User;
import com.arturfrimu.lifemanager.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {
    List<WorkoutSession> findByUser(User user);
    List<WorkoutSession> findByUserAndCompletedAtIsNotNull(User user);
    List<WorkoutSession> findByUserAndCompletedAtIsNull(User user);
    List<WorkoutSession> findByUserAndStartedAtBetween(User user, Instant start, Instant end);
}

