package com.arturfrimu.lifemanager.sport.dto;

import java.util.List;

public record ExercisePageResponse(
        List<ExerciseResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}
