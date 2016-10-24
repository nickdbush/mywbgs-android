package com.nickdbush.mywbgs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.ui.HomeworkEdit;
import com.nickdbush.mywbgs.ui.HomeworkList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeworkActivity extends AppCompatActivity implements HomeworkEdit.OnSaveListener, HomeworkList.OnHomeworkClickedListener {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen, HomeworkList.newInstance());
        ft.addToBackStack("homework_list");
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.screen, HomeworkEdit.newInstance(null));
            ft.commit();
            return true;
        }
        return false;
    }

    @Override
    public void onSave() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen, HomeworkList.newInstance());
        ft.addToBackStack("homework_list");
        ft.commit();
    }

    @Override
    public void onHomeworkClicked(Homework homework) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen, HomeworkEdit.newInstance(homework));
        ft.commit();
    }
}
