package com.realk.todolist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    Todo todo;
    RealmResults<Tag> tags;
    TextView dateTextView;
    TextView whatToDoTextView;
    TextView placeTextView;
    TextView descriptionTextView;
    ListView tagListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getInstance(this);

        dateTextView = (TextView)findViewById(R.id.date);
        whatToDoTextView = (TextView)findViewById(R.id.whattodo);
        placeTextView = (TextView)findViewById(R.id.place);
        descriptionTextView = (TextView)findViewById(R.id.description);
        tagListView = (ListView)findViewById(R.id.taglistview);

        Intent intent = getIntent();
        Bundle selected = intent.getExtras();
        int todoId = selected.getInt("todoId");
        this.todo = realm.where(Todo.class).equalTo("id", todoId).findFirst();

        tags = realm.where(Tag.class).equalTo("todos.id", this.todo.getId()).findAll();
        TagListAdapter tagAdapter = new TagListAdapter(this, R.id.taglistview, tags, true);
        tagListView.setAdapter(tagAdapter);

        dateTextView.setText(this.todo.getDate().toString());
        whatToDoTextView.setText(this.todo.getWhatToDo());
        placeTextView.setText(this.todo.getPlace());
        descriptionTextView.setText(this.todo.getDescription());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0,1,0,"수정");
        menu.add(0,2,0,"삭제");

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(DetailViewActivity.this, ModifyItemActivity.class);
                intent.putExtra("todoId", todo.getId());
                startActivity(intent);
                return true;
            case 2:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("정말 삭제할까요?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        for (Tag tag : todo.getTags()) {
                                            tag.getTodos().remove(todo);
                                        }
                                        todo.removeFromRealm();
                                    }
                                });

                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

                return true;
        }
        return false;
    }
}
