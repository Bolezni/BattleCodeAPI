package com.bolezni.dto;

import jakarta.validation.constraints.NotNull;

public record CourseUpdate(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
