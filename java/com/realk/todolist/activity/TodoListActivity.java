package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class TodoListActivity extends Activity {
    Realm realm;
    RealmResults<Todo> todos;
    Button addBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

//        Realm.deleteRealmFile(this);
        realm = Realm.getInstance(this);

        todos = realm.where(Todo.class).findAll();

        addBtn = (Button)findViewById(R.id.btnadd);
        final ListView todoList = (ListView)findViewById(R.id.todolistview);

        TodoListAdapter todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
        todoList.setAdapter(todoAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoListActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

    }

    private static class ViewHolder {
        TextView whatToDo;
        TextView date;
        CheckBox check;
    }

    class TodoListAdapter extends RealmBaseAdapter<Todo> implements ListAdapter {
        Context context;
        RealmResults<Todo> todos;
        int layout;

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
            final int todoPosition = position;
            viewHolder.whatToDo.setText(todoItem.getWhatToDo());
            viewHolder.date.setText(todoItem.getDate());
            viewHolder.check.setChecked(todoItem.isChecked());

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TodoListActivity.this, DetailViewActivity.class);
                    intent.putExtra("position", todoPosition);
                    startActivity(intent);

                }
            });
            return convertView;
        }

        public RealmResults<Todo> getRealmResults() {
            return realmResults;
        }
    }
}