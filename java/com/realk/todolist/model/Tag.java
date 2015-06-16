package com.realk.todolist.model;

import java.io.Serializable;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tag extends RealmObject implements Serializable {
    @PrimaryKey
    private String tagName;
    private RealmList<Todo> todos;

    public Tag() {
        super();
    }
    public Tag(String tagName) {
        this.tagName = tagName;
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
