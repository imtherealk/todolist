package com.realk.todolist.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Todo extends RealmObject implements Serializable {
    private String whatToDo;
    private String date;
    private String place;
    private String description;
    private RealmList<Tag> tags;
    private boolean checked;
    @PrimaryKey
    private int id;

    public Todo() {
        super();
        checked = false;
    }

    public void setId(int id) { this.id = id; }
    public void setDate(String date) {
        this.date = date;
    }
    public void setWhatToDo(String whatToDo) {
        this.whatToDo = whatToDo;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }
    public String getWhatToDo() {
        return whatToDo;
    }
    public String getDate() {
        return date;
    }
    public String getPlace() {
        return place;
    }
    public String getDescription() {
        return description;
    }
    public RealmList<Tag> getTags() {
        return tags;
    }
    public boolean isChecked() {
        return checked;
    }
}
