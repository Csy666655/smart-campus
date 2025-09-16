package com.course.domain.po;

import lombok.Data;

@Data
public class Course {
    private Long courseId;
    private String courseNo;
    private String name;
    private Long teacherId;
    private int maxStudent;
}
