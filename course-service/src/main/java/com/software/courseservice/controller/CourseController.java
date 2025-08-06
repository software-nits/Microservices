package com.software.courseservice.controller;

import com.software.courseservice.bean.Course;
import com.software.courseservice.bean.EmployeeBean;
import com.software.courseservice.config.ApiConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Autowired
    WebClient webClient;
    @Autowired
    private ApiConfiguration apiConfiguration;
    private List<Course> courses = new ArrayList<>();

    @ResponseStatus(HttpStatus.ACCEPTED) // 201
    @GetMapping(value = "/all", produces = "application/json")
    public List<Course> getAllCourse() {
//        courses.add(new Course("React Course","Learn React", "React course for beginner"));
//        courses.add(new Course("Java Course","Learn Java", "Java course for beginner"));
//        courses.add(new Course("Angular Course","Angular Java", "Angular course for beginner"));
        return courses;
    }


    @ResponseStatus(HttpStatus.ACCEPTED) // 201
    @GetMapping(value = "/api", produces = "application/json")
    public String getApiInfo() {
        String s = null;
//        log.error("it is null or empty - {}empty ", s);
        return apiConfiguration.getKey()+" "+apiConfiguration.getToken();
    }

    @PostMapping(value = "/save", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> getOrderById(@RequestBody Course course) {
        System.out.println(course.toString());
        courses.add(course);
        return ResponseEntity.ok(course.toString());
    }

    @ResponseStatus(HttpStatus.ACCEPTED) // 201
    @GetMapping(value = "/web", produces = "application/json")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("consumed requests with message: " + extracted(new EmployeeBean("101","rajnish", "kumar", "rajnish@gmail.com", null)));
    }
    private ResponseEntity<String> extracted(EmployeeBean employee) {
        return WebClient.builder().build().post()
                .uri("http://localhost:7094/inventory/web-call")
                .body(BodyInserters.fromValue(employee))
                .exchangeToMono(response -> response.toEntity(String.class)).block();
    }
    private String extracted1(EmployeeBean employee) {
        return WebClient.builder().build().post()
                .uri("http://localhost:7094/inventory/web-call")
                .body(BodyInserters.fromValue(employee))
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        responseEntity -> {
                            // Handle success response here
                            HttpStatusCode status = responseEntity.getStatusCode();
                            URI location = responseEntity.getHeaders().getLocation();
                            // handle response as necessary
                        },
                        error -> {
                            // Handle the error here
                            if (error instanceof WebClientResponseException ex) {
                                HttpStatusCode status = ex.getStatusCode();
                                System.out.println("Error Status Code: " + status.value());
                            } else {
                                // Handle other types of errors
                                System.err.println("An unexpected error occurred: " + error.getMessage());
                            }
                        }).toString();
    }

}
