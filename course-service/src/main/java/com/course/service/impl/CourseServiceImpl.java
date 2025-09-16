package com.course.service.impl;

import com.common.utils.Result;
import com.common.utils.UserContext;
import com.course.mapper.CourseMapper;
import com.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Override
    public Result getUserSelect() {
       List<Map<String, Object>> map = courseMapper.getCoursesByStudentId(UserContext.getUser());
        return Result.success(map);
    }
}
