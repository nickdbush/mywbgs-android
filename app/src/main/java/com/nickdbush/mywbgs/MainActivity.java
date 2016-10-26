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

import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.ui.DayPage;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements HomeworkCard.OnHomeworkClickedListener, DayPage.DayPageListeners {

    // How many days you can go back
    public final static int DAYS_BACK = 1 * 7;
    public final static int DAYS_FORWARDS = 5 * 7;

    @State
    int currentPage = DAYS_BACK;

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
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        dayAdapter = new DayAdapter(getSupportFragmentManager());
        pager.setAdapter(dayAdapter);
        pager.setCurrentItem(currentPage);

        sharedPreferences = getSharedPreferences("com.nickdbush.mywbgs", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        if (shouldShowIntro()) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
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

        // if (id == R.id.action_settings) {
        //     // TODO: 23/09/2016 Launch settings
        //     return true;
        // } else
        if (id == R.id.action_homework) {
            Intent intent = new Intent(getBaseContext(), HomeworkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentPage = pager.getCurrentItem();
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onHomeworkClicked(Homework homework) {

    }

    @Override
    public void next() {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    @Override
    public void back() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    @Override
    public void dateChanged(LocalDate date) {
        pager.setCurrentItem(Days.daysBetween(new LocalDate().minusDays(DAYS_BACK), date).getDays());
    }

    private class DayAdapter extends FragmentPagerAdapter {

        public DayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayPage.newInstance(new LocalDate().plusDays(position - DAYS_BACK));
        }

        @Override
        public int getCount() {
            return DAYS_FORWARDS + 1 + DAYS_BACK;
        }
    }
}
