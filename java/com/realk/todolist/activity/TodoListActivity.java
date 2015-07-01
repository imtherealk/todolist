package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class TodoListActivity extends Activity {
    Realm realm;
    RealmResults<Todo> todos;
    Button addButton;
    ListView todoListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

//        Realm.deleteRealmFile(this);
        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).equalTo("checked", false).findAll();
        todos.sort("date");

        addButton = (Button)findViewById(R.id.btnadd);
        todoListView = (ListView)findViewById(R.id.todolistview);

        TodoListAdapter todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
        todoListView.setAdapter(todoAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoListActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private static class ViewHolder {
        TextView whatToDo;
        TextView date;
        CheckBox checkBox;
    }

    class TodoListAdapter extends RealmBaseAdapter<Todo> implements ListAdapter {

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
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Todo todoItem = realmResults.get(position);
            viewHolder.whatToDo.setText(todoItem.getWhatToDo());
            viewHolder.date.setText(todoItem.getDate().toString());
            viewHolder.checkBox.setChecked(todoItem.isChecked());

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CheckBox checkBox = (CheckBox) v;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            todoItem.setChecked(checkBox.isChecked());
                        }
                    });

                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TodoListActivity.this, DetailViewActivity.class);
                    intent.putExtra("todoId", todoItem.getId());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        public RealmResults<Todo> getRealmResults() {
            return realmResults;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0,1,0,"Not Done");
        menu.add(0,2,0,"Done");
        menu.add(0,3,0,"All");

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        TodoListAdapter todoAdapter;
        switch (item.getItemId()) {
            case 1:
                todos = realm.where(Todo.class).equalTo("checked", false).findAll();
                todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
                todoListView.setAdapter(todoAdapter);
                return true;
            case 2:
                todos = realm.where(Todo.class).equalTo("checked", true).findAll();
                todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
                todoListView.setAdapter(todoAdapter);
                return true;
            case 3:
                todos = realm.where(Todo.class).findAll();
                todoAdapter = new TodoListAdapter(this, R.id.todolistview, todos, true);
                todoListView.setAdapter(todoAdapter);
                return true;
        }
        return false;
    }
}