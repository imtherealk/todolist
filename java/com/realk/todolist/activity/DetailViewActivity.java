package com.realk.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.realk.todolist.R;
import com.realk.todolist.model.Todo;

public class DetailViewActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Todo selectedTodo = (Todo)intent.getSerializableExtra("selectedTodo");
    }
}
