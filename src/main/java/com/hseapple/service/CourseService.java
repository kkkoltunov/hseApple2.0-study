package com.hseapple.service;

import com.hseapple.dao.CourseEntity;
import com.hseapple.dao.RequestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.hseapple.util.StringUtils.formatRequest;

@Service
public class CourseService {

    private final RestTemplate restTemplate;
    private final String url;

    public CourseService(RestTemplate restTemplate, @Value("${hseApple.storage.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public List<CourseEntity> findAllCourse() {
        ResponseEntity<CourseEntity[]> forEntity = restTemplate.getForEntity(formatRequest(url, "/subject/courses"), CourseEntity[].class);
        return List.of(forEntity.getBody());
    }

    public Iterable<RequestEntity> findAllRequests(Integer courseID, Boolean approved) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<RequestEntity[]> exchange = restTemplate.exchange(url + "/subject/course/{courseID}/application/list?approved={approved}", HttpMethod.GET, entity, RequestEntity[].class, courseID, approved);
        return List.of(exchange.getBody());
    }

    public CourseEntity createCourse(CourseEntity courseEntity) {
        ResponseEntity<CourseEntity> messageEntityResponseEntity = restTemplate.postForEntity(formatRequest(url, "/subject/course"), courseEntity, CourseEntity.class);
        return messageEntityResponseEntity.getBody();
    }

    public void updateCourse(CourseEntity newCourse, Integer courseID) {
        restTemplate.put(formatRequest(url, "/subject/course/%s", courseID), newCourse);
    }

    public void deleteCourse(Integer courseID) {
        restTemplate.delete(formatRequest(url, "/subject/course/%s", courseID));
    }

}