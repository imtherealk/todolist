package com.realk.todolist.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TodoListActivity extends BaseActivity {

    /* Views */
    Button tagSelectButton;
    Button addButton;
    ListView todoListView;

    /* Filters */
    Tag tagFilter;
    Status status;

    /* Data */
    List<Todo> todos = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        /* Set views */
        addButton = (Button) findViewById(R.id.btnadd);
        tagSelectButton = (Button) findViewById(R.id.btntagselect);
        todoListView = (ListView) findViewById(R.id.todolistview);

        /* Init adapter */
        todos = fetchTodoList(null, Status.NOT_DONE);

        /* Fetch initial data */
        status = Status.NOT_DONE;
        tagFilter = null;
        updateTodoList();

        /* Set listeners */
        tagSelectButton.setOnClickListener(this.tagSelectButtonListener);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoListActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        this.todoListView.setAdapter(todoListAdapter);
    }

    private final BaseAdapter todoListAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return todos.size();
        }

        @Override
        public Object getItem(int position) {
            return todos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.todolistview, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.whatToDo = (TextView) convertView.findViewById(R.id.whattodo);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Todo todoItem = (Todo) this.getItem(position);
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
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "Not Done").setCheckable(true).setChecked(true);
        menu.add(0, 2, 0, "Done").setCheckable(true);
        menu.add(0, 3, 0, "All").setCheckable(true);
        menu.setGroupCheckable(0, true, true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                item.setChecked(true);
                status = Status.NOT_DONE;
                updateTodoList();
                return true;
            case 2:
                item.setChecked(true);
                status = Status.DONE;
                updateTodoList();
                return true;
            case 3:
                item.setChecked(true);
                status = Status.ALL;
                updateTodoList();
                return true;
        }
        return false;
    }

    public RealmResults<Todo> fetchTodoList(Tag tagFilter, Status status) {
        RealmQuery<Todo> query = realm.where(Todo.class);
        // Add tag filter
        if (tagFilter != null) {
            query = query.contains("tags.tagName", tagFilter.getTagName());
        }
        // Add status filter
        switch (status) {
            case NOT_DONE:
                query = query.equalTo("checked", false);
                break;
            case DONE:
                query = query.equalTo("checked", true);
                break;
            case ALL:
                break;
        }

        // Execute query
        return query.findAllSorted("date");
    }

    public void updateTodoList() {
        todos = fetchTodoList(tagFilter, status);

        // Update adapter
        todoListAdapter.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView whatToDo;
        TextView date;
        CheckBox checkBox;
    }

    public enum Status {
        NOT_DONE,  // 완료 안됨
        DONE,   // 완료 됨
        ALL  // 모두
    }

    private final View.OnClickListener tagSelectButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final List<String> allTagStrings = new ArrayList<>();
            RealmResults<Tag> allTags = realm.where(Tag.class).findAll();

            allTags.sort("tagName");
            allTagStrings.add("-전체보기-");
            for (Tag tag : allTags) {
                allTagStrings.add(tag.getTagName());
            }
            ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(
                    TodoListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    allTagStrings
            );

            new AlertDialog.Builder(TodoListActivity.this)
                    .setAdapter(tagsAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    tagFilter = null;
                                    break;
                                default:
                                    tagFilter = realm.where(Tag.class).equalTo("tagName", allTagStrings.get(which)).findFirst();
                                    break;
                            }
                            tagSelectButton.setText(allTagStrings.get(which));

                            updateTodoList();
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    };
}