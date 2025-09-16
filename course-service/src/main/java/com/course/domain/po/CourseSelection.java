package com.course.domain.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseSelection {
    private Long selectionId;
    private Long studentId;
    private Long courseId;
    private LocalDateTime selectedAt;
}
