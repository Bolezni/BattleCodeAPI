package com.bolezni.mapper;

import com.bolezni.dto.CourseInfo;
import com.bolezni.store.entity.CourseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseInfo mapToCourseInfo(CourseEntity courseEntity);
}
