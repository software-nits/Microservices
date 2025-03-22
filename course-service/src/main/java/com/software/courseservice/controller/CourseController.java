package com.software.courseservice.controller;

import com.software.courseservice.bean.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private List<Course> courses = new ArrayList<>();

    @ResponseStatus(HttpStatus.ACCEPTED) // 201
    @GetMapping(value = "/all", produces = "application/json")
    public List<Course> getAllCourse() {
//        courses.add(new Course("React Course","Learn React", "React course for beginner"));
//        courses.add(new Course("Java Course","Learn Java", "Java course for beginner"));
//        courses.add(new Course("Angular Course","Angular Java", "Angular course for beginner"));
        return courses;
    }

    @PostMapping(value = "/save", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> getOrderById(@RequestBody Course course) {
        System.out.println(course.toString());
        courses.add(course);
        return ResponseEntity.ok(course.toString());
    }

}
