package com.hseapple.service;

import com.hseapple.dao.SubjectEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.hseapple.util.StringUtils.formatRequest;

@Service
public class SubjectService {
    private final RestTemplate restTemplate;
    private final String url;

    public SubjectService(RestTemplate restTemplate, @Value("${hseApple.storage.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Iterable<SubjectEntity> findAllSubjects() {
        ResponseEntity<SubjectEntity[]> forEntity = restTemplate.getForEntity(formatRequest(url, "/subjects"), SubjectEntity[].class);
        return List.of(forEntity.getBody());
    }

    public SubjectEntity createSubject(SubjectEntity subjectEntity) {
        ResponseEntity<SubjectEntity> messageEntityResponseEntity = restTemplate.postForEntity(formatRequest(url, "/subject"), subjectEntity, SubjectEntity.class);
        return messageEntityResponseEntity.getBody();
    }

    public void updateSubject(SubjectEntity newSubject, Long subjectID) {
        restTemplate.put(formatRequest(url, "/subject/%s", subjectID), newSubject);
    }

    public void deleteSubject(Long subjectID) {
        restTemplate.delete(formatRequest(url, "/subject/%s", subjectID));
    }

}