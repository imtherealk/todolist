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
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddItemActivity extends Activity {
    Realm realm;
    RealmResults<Todo> todos;
    RealmResults<Tag> alltags;
    Button saveButton;
    DatePicker datePicker;
    EditText whatToDoEditText;
    EditText placeEditText;
    EditText descriptionEditText;
    EditText tagsEditText;
    Todo todoItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).findAll();
        alltags = realm.where(Tag.class).findAll();

        saveButton = (Button) findViewById(R.id.btnsave);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        whatToDoEditText = (EditText) findViewById(R.id.editwhattodo);
        placeEditText = (EditText) findViewById(R.id.editplace);
        descriptionEditText = (EditText) findViewById(R.id.editdescription);
        tagsEditText = (EditText) findViewById(R.id.edittags);

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
                        Todo todo = realm.createObject(Todo.class);

                        todo.setId((int) (realm.where(Todo.class).maximumInt("id") + 1));
                        todo.setDate(date);
                        todo.setWhatToDo(whatTodo);
                        todo.setPlace(place);
                        todo.setDescription(description);

                        for (String tagString : tagStrings) {
                            if (tagString.trim().equals(""))
                                continue;
                            Tag tag = realm.where(Tag.class).equalTo("tagName", tagString).findFirst();
                            if (tag == null) {
                                tag = realm.createObject(Tag.class);
                                tag.setTagName(tagString);
                            }
                            todo.getTags().add(tag);
                            tag.getTodos().add(todo);
                        }
                        todoItem = todo;
                    }
                });
                Intent intent = new Intent(AddItemActivity.this, DetailViewActivity.class);
                intent.putExtra("todoId", todoItem.getId());
                startActivity(intent);
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
