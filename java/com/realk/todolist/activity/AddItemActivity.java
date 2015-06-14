package com.realk.todolist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.realk.todolist.R;
import com.realk.todolist.model.Todo;

import io.realm.Realm;

public class AddItemActivity extends Activity {
    Realm realm;
    Button saveBtn;
    DatePicker picker;
    EditText whatToDo;
    EditText place;
    EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        realm = Realm.getInstance(this);
        saveBtn = (Button)findViewById(R.id.btnsave);
        picker = (DatePicker)findViewById(R.id.datepicker);
        whatToDo = (EditText)findViewById(R.id.editwhattodo);
        place = (EditText)findViewById(R.id.editplace);
        description = (EditText)findViewById(R.id.editdescription);

        ListView taglist = (ListView)findViewById(R.id.edittags);

        picker.init(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Todo listItem = realm.createObject(Todo.class);
                listItem.setDate(String.format("%d.%02d.%02d.", picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth()));
                listItem.setWhatToDo(whatToDo.getText().toString());
                listItem.setPlace(place.getText().toString());
                listItem.setDescription(description.getText().toString());
                realm.commitTransaction();
                finish();
            }
        });
    }
}
