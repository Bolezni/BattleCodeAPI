package com.bolezni.sevice;

import com.bolezni.dto.CourseCreateDto;
import com.bolezni.dto.CourseInfo;
import com.bolezni.dto.CourseUpdate;

public interface CourseService {
    CourseInfo createCourse(CourseCreateDto course);

    CourseInfo getCourse(Long id);

    void deleteCourse(Long id);

    CourseInfo updateCourse(Long id, CourseUpdate updateDto);
}
