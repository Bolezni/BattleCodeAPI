package com.bolezni.sevice.impl;

import com.bolezni.dto.CourseCreateDto;
import com.bolezni.dto.CourseInfo;
import com.bolezni.dto.CourseUpdate;
import com.bolezni.mapper.CourseMapper;
import com.bolezni.sevice.CourseService;
import com.bolezni.store.entity.CourseEntity;
import com.bolezni.store.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseInfo createCourse(CourseCreateDto course) {
        if(course == null){
            log.error("Course dto is null");
            throw new IllegalArgumentException("Course dto is null");
        }

        CourseEntity courseEntity = CourseEntity.builder()
                .name(course.name())
                .description(course.description())
                .build();

        CourseEntity saved = courseRepository.save(courseEntity);

        return courseMapper.mapToCourseInfo(saved);
    }

    @Override
    public CourseInfo getCourse(Long id) {
        CourseEntity course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with id " + id + " not found"));
        return courseMapper.mapToCourseInfo(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if(id == null){
            log.error("Course id is null");
            throw new IllegalArgumentException("Course id is null");
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CourseInfo updateCourse(Long id, CourseUpdate updateDto) {
        if(updateDto == null){
            log.error("Course update dto is null");
            throw new IllegalArgumentException("Course update dto is null");
        }

        CourseEntity course = getCourseById(id);

        Optional.ofNullable(updateDto.name())
                .filter(name -> !course.getName().equals(name))
                .ifPresent(course::setName);

        Optional.ofNullable(updateDto.description())
                .filter(description -> !course.getDescription().equals(description))
                .ifPresent(course::setDescription);

        CourseEntity saved = courseRepository.save(course);

        return courseMapper.mapToCourseInfo(saved);
    }

    private CourseEntity getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with id " + id + " not found"));
    }
}
