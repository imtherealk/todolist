package com.realk.todolist.activity;

import android.app.Activity;
import android.os.Bundle;

import io.realm.Realm;

/**
 * Created by my on 2015-07-11.
 */
public class BaseActivity extends Activity {
    protected Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
