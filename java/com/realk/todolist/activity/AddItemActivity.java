package com.realk.todolist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.realk.todolist.R;
import com.realk.todolist.model.Tag;
import com.realk.todolist.model.Todo;
import java.util.StringTokenizer;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        realm = Realm.getInstance(this);
        todos = realm.where(Todo.class).findAll();
        alltags = realm.where(Tag.class).findAll();

        saveButton = (Button)findViewById(R.id.btnsave);
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        whatToDoEditText = (EditText)findViewById(R.id.editwhattodo);
        placeEditText = (EditText)findViewById(R.id.editplace);
        descriptionEditText = (EditText)findViewById(R.id.editdescription);
        tagsEditText = (EditText)findViewById(R.id.edittags);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    }
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Todo listItem = realm.createObject(Todo.class);
                        listItem.setDate(String.format("%d.%02d.%02d.", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                        listItem.setWhatToDo(whatToDoEditText.getText().toString());
                        listItem.setPlace(placeEditText.getText().toString());
                        listItem.setDescription(descriptionEditText.getText().toString());
                        listItem.setId(todos.size());

                        String taglist = tagsEditText.getText().toString();
                        StringTokenizer str = new StringTokenizer(taglist, ",");
                        while (str.hasMoreTokens()) {
                            String temp = str.nextToken();
                            if (!str.equals(",")) {
                                Tag tag = new Tag();
                                tag.setTagName(temp);
                                Tag tagcheck = realm.where(Tag.class).equalTo("tagName", temp).findFirst();

                                if (tagcheck == null) {
                                    Tag newtag = realm.copyToRealm(tag);
                                    newtag.getTodos().add(listItem);
                                    listItem.getTags().add(newtag);
                                } else tagcheck.getTodos().add(listItem);


                            }
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
