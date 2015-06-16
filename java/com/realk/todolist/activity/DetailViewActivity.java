package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class DetailViewActivity extends Activity {

    Realm realm;
    RealmResults<Todo> todos;
    RealmResults<Tag> tags;
    TextView date;
    TextView whatToDo;
    TextView place;
    TextView description;
    ListView tagListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).findAll();

        date = (TextView)findViewById(R.id.date);
        whatToDo = (TextView)findViewById(R.id.whattodo);
        place = (TextView)findViewById(R.id.place);
        description = (TextView)findViewById(R.id.description);
        tagListView = (ListView)findViewById(R.id.taglistview);

        Intent intent = getIntent();
        Bundle selected = intent.getExtras();
        Todo selectedTodo = todos.get(selected.getInt("position"));

        tags = realm.where(Tag.class).equalTo("todos.id", selectedTodo.getId()).findAll();
        TagListAdapter tagAdapter = new TagListAdapter(this, R.id.taglistview, tags, true);
        tagListView.setAdapter(tagAdapter);

        date.setText(selectedTodo.getDate());
        whatToDo.setText(selectedTodo.getWhatToDo());
        place.setText(selectedTodo.getPlace());
        description.setText(selectedTodo.getDescription());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private static class ViewHolder {
        TextView tagName;
        TextView deleteTag;
    }

    class TagListAdapter extends RealmBaseAdapter<Tag> implements ListAdapter {
        public TagListAdapter(Context context, int resId, RealmResults<Tag> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.taglistview, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tagName = (TextView) convertView.findViewById(R.id.tagname);
                viewHolder.deleteTag = (TextView) convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Tag tagItem = realmResults.get(position);
            viewHolder.tagName.setText(tagItem.getTagName());
            return convertView;
        }
        public RealmResults<Tag> getRealmResults() {
            return realmResults;
        }
    }

}
