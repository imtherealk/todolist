package com.realk.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TodoList extends Activity {
    Realm realm;
    RealmResults<Todo> todos;
    Button addBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Realm.deleteRealmFile(this);

        realm = Realm.getInstance(this);
        realm.beginTransaction();
        Todo todoExample = realm.createObject(Todo.class);
        todoExample.setWhatToDo("영화 보기");
        todoExample.setDate(new Date());
        todoExample.setPlace("용산");
        todoExample.setDescription("매드 맥스");
        realm.commitTransaction();
        todos = realm.where(Todo.class).findAll();

        addBtn = (Button)findViewById(R.id.btnadd);
        ListView todoList = (ListView)findViewById(R.id.todolistview);

        TodoListAdapter todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
        todoList.setAdapter(todoAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoList.this, addItemActivity.class);
                startActivity(intent);
            }
        });

    }
}

class TodoListAdapter extends RealmBaseAdapter<Todo> implements ListAdapter {
    Context context;
    RealmResults<Todo> todos;
    int layout;

    private static class ViewHolder {
        TextView whatToDo;
        TextView date;
        CheckBox check;
    }

    public TodoListAdapter(Context context, int resId, RealmResults<Todo> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.todolistview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.whatToDo = (TextView) convertView.findViewById(R.id.whattodo);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Todo todoItem = realmResults.get(position);
        viewHolder.whatToDo.setText(todoItem.getWhatToDo());
        viewHolder.date.setText(todoItem.getDate().toString());
        viewHolder.check.setChecked(todoItem.isChecked());

        return convertView;
    }

    public RealmResults<Todo> getRealmResults() {
        return realmResults;
    }
}