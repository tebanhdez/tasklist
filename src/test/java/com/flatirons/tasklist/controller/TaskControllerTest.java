package com.flatirons.tasklist.controller;

import com.flatirons.tasklist.model.Task;
import com.flatirons.tasklist.service.TaskService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TaskControllerTest extends JerseyTest {

    Gson parser = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private Task testTask = new Task("", "test-task", new Date());
    Entity<String> taskEntity = Entity.entity(parser.toJson(testTask), MediaType.APPLICATION_JSON);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private long beginTime = Timestamp.valueOf("2018-01-01 00:00:00").getTime();
    private long endTime = Timestamp.valueOf("2018-12-31 00:58:00").getTime();

    @Override
    protected Application configure(){
        return new ResourceConfig(TaskController.class);
    }

    @Before
    public void insertTestData(){
        testTask.setDueDate(new Date(getRandomTimeBetweenTwoDates()));
        testTask = TaskService.getInstance().saveOrUpdateAddress(testTask);
    }
    @After
    public void deleteTestData(){
        TaskService.getInstance().deleteTask(testTask.getId());
    }
    @Test
    public void testGetAllPendingTask() throws Exception {
        final Response response = target().path("task").request().get();
        Assert.assertEquals(200, response.getStatus());
    }
    @Test
    public void testGetTaskById() throws Exception {

        String url = String.format("task/%s", testTask.getId());
        final Response response = target().path(url).request().get();
        Assert.assertEquals(200, response.getStatus());
        String responseTask = response.readEntity(String.class);
        Task task = parser.fromJson(responseTask, Task.class);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), task.getName());
        Assert.assertEquals(formatter.format(testTask.getDueDate()), formatter.format(task.getDueDate()));
        Assert.assertEquals(testTask.getCompleted(), task.getCompleted());
    }
    @Test
    public void testGetTaskByIdNoExist() throws Exception {
        final Response response = target().path("task/-1").request().get();
        Assert.assertEquals(200, response.getStatus());
    }
    @Test
    public void testAddNewTask() throws Exception {
        final Response response = target().path("task").request().post(taskEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testUpdateTask() throws Exception {
        Assert.assertFalse("Test task not empty", testTask.getId().isEmpty());
        String url = String.format("task", testTask.getId());
        testTask.setName("modified");
        testTask.setCompleted(true);
        final Response response = target().path(url).request().put(Entity.entity(parser.toJson(testTask), MediaType.APPLICATION_JSON));
        Assert.assertEquals(200, response.getStatus());
        String responseTask = response.readEntity(String.class);
        Task task = parser.fromJson(responseTask, Task.class);
        Assert.assertEquals(testTask.getId(), task.getId());
        Assert.assertEquals(testTask.getName(), "modified");
        Assert.assertTrue("Update completion failed", task.getCompleted());
    }

    @Test
    public void testDeleteTask() throws Exception {
        Assert.assertFalse("Test task not empty", testTask.getId().isEmpty());
        String url = String.format("task/%s", testTask.getId());
        final Response response = target().path(url).request().delete();
        Assert.assertEquals(200, response.getStatus());
    }

    private long getRandomTimeBetweenTwoDates () {
        long diff = endTime - beginTime + 1;
        return beginTime + (long) (Math.random() * diff);
    }
}
