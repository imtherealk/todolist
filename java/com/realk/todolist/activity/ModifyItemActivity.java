package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ModifyItemActivity extends Activity {
    Realm realm;
    RealmResults<Todo> todos;
    RealmResults<Tag> tags;
    Todo todo;
    Button saveButton;
    DatePicker datePicker;
    EditText whatToDoEditText;
    EditText placeEditText;
    EditText descriptionEditText;
    EditText tagsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).findAll();

        saveButton = (Button) findViewById(R.id.btnsave);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        whatToDoEditText = (EditText) findViewById(R.id.editwhattodo);
        placeEditText = (EditText) findViewById(R.id.editplace);
        descriptionEditText = (EditText) findViewById(R.id.editdescription);
        tagsEditText = (EditText) findViewById(R.id.edittags);

        Intent intent = getIntent();
        Bundle selected = intent.getExtras();
        int todoId = selected.getInt("todoId");
        this.todo = realm.where(Todo.class).equalTo("id", todoId).findFirst();
        tags = realm.where(Tag.class).equalTo("todos.id", todoId).findAll();

        whatToDoEditText.setText(todo.getWhatToDo());
        placeEditText.setText(todo.getPlace());
        descriptionEditText.setText(todo.getDescription());

        String tagString = "";
        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {
            tagString += tagIterator.next().getTagName();
            if (tagIterator.hasNext())
                tagString += ",";
        }

        tagsEditText.setText(tagString);

        Date date = todo.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    }
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                final Date date = calendar.getTime();
                final String whatTodo = whatToDoEditText.getText().toString();
                final String place = placeEditText.getText().toString();
                final String description = descriptionEditText.getText().toString();
                final List<String> tagStrings = Arrays.asList(tagsEditText.getText().toString().split(","));
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        todo.setDate(date);
                        todo.setWhatToDo(whatTodo);
                        todo.setPlace(place);
                        todo.setDescription(description);
                        todo.getTags().clear();

                        for (String tagString : tagStrings) {
                            Tag tag = realm.where(Tag.class).equalTo("tagName", tagString).findFirst();
                            if (tag == null) {
                                tag = realm.createObject(Tag.class);
                                tag.setTagName(tagString);
                            }
                            todo.getTags().add(tag);
                        }
                    }
                });
                finish();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
