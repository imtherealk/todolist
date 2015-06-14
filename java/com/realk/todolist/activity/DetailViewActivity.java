package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realk.todolist.R;
import com.realk.todolist.model.Todo;

import io.realm.Realm;
import io.realm.RealmResults;

public class DetailViewActivity extends Activity {

    Realm realm;
    RealmResults<Todo> todos;
    TextView date;
    TextView whatToDo;
    TextView place;
    TextView description;
    ListView tags;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).findAll();

        date = (TextView)findViewById(R.id.date);
        whatToDo = (TextView)findViewById(R.id.whattodo);
        place = (TextView)findViewById(R.id.place);
        description = (TextView)findViewById(R.id.description);
        tags = (ListView)findViewById(R.id.tags);

        Intent intent = getIntent();
        Bundle selected = intent.getExtras();
        Todo selectedTodo = todos.get(selected.getInt("position"));

        date.setText(selectedTodo.getDate());
        whatToDo.setText(selectedTodo.getWhatToDo());
        place.setText(selectedTodo.getPlace());
        description.setText(selectedTodo.getDescription());
    }
}
