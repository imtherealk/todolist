package com.realk.todolist.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Tag extends RealmObject implements Serializable {
    private String tagName;
    private RealmList<Todo> todos;

    public Tag() {
        super();
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public void setTodos(RealmList<Todo> todos) {
        this.todos = todos;
    }
    public String getTagName() {
        return tagName;
    }
    public RealmList<Todo> getTodos() {
        return todos;
    }
}
