package com.nickdbush.mywbgs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.ui.HomeworkEdit;
import com.nickdbush.mywbgs.ui.HomeworkList;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeworkActivity extends AppCompatActivity implements HomeworkEdit.OnSaveListener, HomeworkCard.OnHomeworkClickedListener {

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
        getSupportFragmentManager().popBackStackImmediate("homework_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.screen, HomeworkList.newInstance(), "homework_list");
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.screen, HomeworkEdit.newInstance(null), "homework_edit");
            ft.addToBackStack("homework_edit");
            ft.commit();
            return true;
        }
        return false;
    }

    @Override
    public void onSave() {
        getSupportFragmentManager().popBackStackImmediate("homework_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen, HomeworkList.newInstance(), "homework_list");
        ft.commitNow();
    }

    @Override
    public void onHomeworkClicked(Homework homework) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen, HomeworkEdit.newInstance(homework), "homework_edit");
        ft.addToBackStack("homework_edit");
        ft.commit();
    }
}
