package com.flatirons.tasklist.controller;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flatirons.tasklist.model.Task;
import com.flatirons.tasklist.service.TaskService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("task")
@RequestScoped
public class TaskController {

    Gson parser = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(){
        return Response.ok(parser.toJson(TaskService.getInstance().getPendingTasks())).build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") String id){
        return Response.ok(parser.toJson(TaskService.getInstance().getTaskById(id))).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(String taskToSave){
        Task task = parser.fromJson(taskToSave, Task.class);
        return Response.ok(parser.toJson(TaskService.getInstance().saveOrUpdateAddress(task))).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String taskToSave){
        Task task = parser.fromJson(taskToSave, Task.class);
        return Response.ok(parser.toJson(TaskService.getInstance().saveOrUpdateAddress(task))).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id){
        boolean result = TaskService.getInstance().deleteTask(id);
        return Response.ok().build();
    }
}
