package com.course.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    @MapKey("courseId")
    List<Map<String, Object>> getCoursesByStudentId(Long studentId);
}
