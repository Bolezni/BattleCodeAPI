package com.bolezni.controller;

import com.bolezni.dto.CourseCreateDto;
import com.bolezni.dto.CourseInfo;
import com.bolezni.dto.CourseUpdate;
import com.bolezni.sevice.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "course_methods")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CourseController {
    CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseInfo> createCourse(@Valid @RequestBody CourseCreateDto courseDto) {
        CourseInfo createdCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseInfo> getCourseById(@PathVariable(name = "courseId") Long courseId) {
        CourseInfo courseInfo = courseService.getCourse(courseId);
        return ResponseEntity.ok(courseInfo);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseInfo> updateCourse(@PathVariable(name = "courseId") Long courseId,
                                                   @Valid @RequestBody CourseUpdate updateDto){
        CourseInfo courseInfo = courseService.updateCourse(courseId, updateDto);
        return ResponseEntity.ok(courseInfo);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "courseId") Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}
