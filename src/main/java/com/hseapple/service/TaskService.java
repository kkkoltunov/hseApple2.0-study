package com.hseapple.service;

import com.hseapple.app.security.UserAndRole;
import com.hseapple.dao.TaskEntity;
import com.hseapple.dao.UserTaskEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.hseapple.util.StringUtils.formatRequest;

@Service
public class TaskService {

    private final RestTemplate restTemplate;
    private final String url;

    public TaskService(RestTemplate restTemplate, @Value("${hseApple.storage.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public void deleteTask(Long taskID) {
        restTemplate.delete(formatRequest(url, "task/%s", taskID));
    }

    public List<TaskEntity> findTasks(Integer courseID, Long start) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<TaskEntity[]> exchange = restTemplate.exchange(url + "/subject/course/{courseID}/application/list?start={start}", HttpMethod.GET, entity, TaskEntity[].class, courseID, start);
        return List.of(exchange.getBody());
    }

    public TaskEntity getTaskForCourse(Long taskID) {
        ResponseEntity<TaskEntity> forEntity = restTemplate.getForEntity(formatRequest(url, "/task/%s", taskID), TaskEntity.class);
        return forEntity.getBody();
    }

    public TaskEntity createTask(TaskEntity taskEntity) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskEntity.setCreatedBy(user.getId());
        ResponseEntity<TaskEntity> messageEntityResponseEntity = restTemplate.postForEntity(formatRequest(url, "/course/task"), taskEntity, TaskEntity.class);
        return messageEntityResponseEntity.getBody();
    }

    public Iterable<UserTaskEntity> findAnswerTasks(Long taskID, Boolean state_answer, String form) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<UserTaskEntity[]> exchange = restTemplate.exchange(url + "/course/task/{taskID}?status={status}&form={form}", HttpMethod.GET, entity, UserTaskEntity[].class, taskID, state_answer, form);
        return List.of(exchange.getBody());
    }

    public UserTaskEntity createAnswer(Long taskID, UserTaskEntity userTaskEntity) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userTaskEntity.setUserID(user.getId());
        ResponseEntity<UserTaskEntity> messageEntityResponseEntity = restTemplate.postForEntity(formatRequest(url, "/course/task/{taskID}/answer", taskID), userTaskEntity, UserTaskEntity.class);
        return messageEntityResponseEntity.getBody();
    }

    public void updateTask(TaskEntity newTask, Long taskID) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newTask.setUpdatedBy(user.getId());
        restTemplate.put(formatRequest(url, "/task/%s", taskID), newTask);
    }

    public void updateUserTask(UserTaskEntity newUserTask) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newUserTask.setUpdatedBy(user.getId());
        restTemplate.put(formatRequest(url, "/task/score"), newUserTask);
    }
}