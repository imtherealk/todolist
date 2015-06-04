package com.realk.todolist;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Tag extends RealmObject {
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
