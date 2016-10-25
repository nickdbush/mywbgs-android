package com.nickdbush.mywbgs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nickdbush.mywbgs.ui.DayPage;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager_days)
    ViewPager pager;

    private DayAdapter dayAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        dayAdapter = new DayAdapter(getSupportFragmentManager());
        pager.setAdapter(dayAdapter);
        pager.setCurrentItem(DayAdapter.OFFSET);

        sharedPreferences = getSharedPreferences("com.nickdbush.mywbgs", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        if (shouldShowIntro()) {
            // Intent intent = new Intent(getBaseContext(), IntroActivity.class);
            // startActivity(intent);
            // finish();
        }
        super.onResume();
    }

    private boolean shouldShowIntro() {
        return sharedPreferences.getBoolean("nodata", true);
    }

    @Override
    protected void onResumeFragments() {
        dayAdapter.notifyDataSetChanged();
        super.onResumeFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // TODO: 23/09/2016 Launch settings
            return true;
        } else if (id == R.id.action_homework) {
            Intent intent = new Intent(getBaseContext(), HomeworkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Icepick.saveInstanceState(this, outState);
    }

    private class DayAdapter extends FragmentPagerAdapter {

        // How many days you can go back
        public final static int OFFSET = 5;

        public DayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayPage.newInstance(new LocalDate().plusDays(position - OFFSET));
        }

        @Override
        public int getCount() {
            return (7 * 7) + OFFSET;
        }
    }
}
