package com.realk.todolist;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Todo extends RealmObject {
    private String whatToDo;
    private Date date;
    private String place;
    private String description;
    private RealmList<Tag> tags;
    private boolean checked;

    public Todo() {
        super();
        checked = false;
    }

    public void setDate(Date date) {
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

    public String getWhatToDo() {
        return whatToDo;
    }
    public Date getDate() {
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
