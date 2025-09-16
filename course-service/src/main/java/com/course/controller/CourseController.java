package com.course.controller;

import com.common.utils.Result;
import com.common.utils.UserContext;
import com.course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping("test")
    public Long test(){
      log.info("用户id为{}",UserContext.getUser());
      return UserContext.getUser();
    }
    @GetMapping("/getSelect")
    public Result getUserSelected(){
        return courseService.getUserSelect();
    }
}
