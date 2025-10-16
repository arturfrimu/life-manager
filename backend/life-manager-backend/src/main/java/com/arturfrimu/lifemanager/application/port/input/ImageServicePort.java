package com.arturfrimu.lifemanager.application.port.input;

import java.util.UUID;

public interface ImageServicePort {

    byte[] downloadExerciseImagesAsZip(UUID exerciseId);
}