package com.flatirons.tasklist.model;

public enum Fields {
    Id("id")
    ,Name("name")
    ,DueDate("duedate")
    ,Status("status");

    private final String _field;

    Fields(String _field) {
        this._field = _field;
    }

    public String getKey() {
        return _field;
    }
}
