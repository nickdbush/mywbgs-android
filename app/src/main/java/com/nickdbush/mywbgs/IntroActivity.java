package com.nickdbush.mywbgs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.ui.TimetableEdit;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IntroActivity extends AppIntro {

    private SharedPreferences sharedPreferences;
    private Lesson[][] lessons = new Lesson[5][];
    private int responses = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("com.nickdbush.mywbgs", MODE_PRIVATE);

        setBarColor(Color.parseColor("#555555"));
        skipButtonEnabled = false;
        addSlide(AppIntroFragment.newInstance("This is alpha software", "Things will probably break.\nYou have been warned!", R.drawable.bug, Color.parseColor("#555555")));
        addSlide(TimetableEdit.newInstance(0));
        addSlide(TimetableEdit.newInstance(1));
        addSlide(TimetableEdit.newInstance(2));
        addSlide(TimetableEdit.newInstance(3));
        addSlide(TimetableEdit.newInstance(4));
        pager.setPagingEnabled(false);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        if (oldFragment instanceof TimetableEdit) {
            Lesson[] harvest = ((TimetableEdit) oldFragment).harvest();
            lessons[oldFragment.getArguments().getInt("day")] = harvest;
        }
        Log.d(getClass().getSimpleName(), "IntroActivity#onSlideChanged");
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Lesson[] lessonArray : lessons) {
            for (Lesson lesson : lessonArray) {
                realm.copyToRealm(lesson);
            }
        }
        realm.commitTransaction();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nodata", false);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
