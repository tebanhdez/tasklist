package com.flatirons.tasklist.model;

import java.util.Date;

public class Task {
    private String Id;
    private String Name;
    private Date DueDate;
    private boolean Completed;

    public Task(){}

    public Task(String id, String name, Date dueDate){
        this.Id = id;
        this.Name = name;
        this.DueDate = dueDate;
        this.Completed = false;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getDueDate() {
        return DueDate;
    }

    public void setDueDate(Date dueDate) {
        DueDate = dueDate;
    }

    public boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }
}