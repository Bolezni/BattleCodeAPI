package com.bolezni.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseCreateDto(
        @NotBlank(message = "Название курса обязательно")
        @Size(min = 3, max = 100, message = "Название курса должно быть от 3 до 100 символов")
        String name,

        @NotBlank(message = "Описание курса обязательно")
        @Size(min = 10, max = 1000, message = "Описание курса должно быть от 10 до 1000 символов")
        String description
) {

}
