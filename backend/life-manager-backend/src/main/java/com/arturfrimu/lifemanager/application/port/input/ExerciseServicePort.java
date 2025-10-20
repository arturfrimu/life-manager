package com.arturfrimu.lifemanager.application.port.input;

import com.arturfrimu.lifemanager.domain.model.Exercise;
import com.arturfrimu.lifemanager.domain.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ExerciseServicePort {
    Exercise createExercise(String name, Type type, String description);

    Exercise getExercise(UUID id);

    Page<Exercise> findAllExercises(Pageable pageable);

    void deleteExercise(UUID id);

    Exercise updateExercise(UUID id, String name, Type type, String description);

    void attachImage(UUID exerciseId, List<MultipartFile> files);
}

