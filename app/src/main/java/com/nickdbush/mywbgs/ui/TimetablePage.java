package com.nickdbush.mywbgs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Utils;
import com.nickdbush.mywbgs.ui.cards.TimetableCard;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetablePage extends Fragment {
    private final String TITLE = "Timetable";

    @BindView(R.id.layout_pager)
    ViewPager viewPager;

    private TimetablePagerAdapter timetablePagerAdapter;

    public TimetablePage() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(TITLE);
    }

    public static TimetablePage newInstance() {
        TimetablePage fragment = new TimetablePage();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_timetable, container, false);
        ButterKnife.bind(this, view);

        timetablePagerAdapter = new TimetablePagerAdapter((FragmentManager) getChildFragmentManager());
        viewPager.setAdapter(timetablePagerAdapter);

        viewPager.setCurrentItem(Utils.getClosestSchoolDay());

        return view;
    }

    private class TimetablePagerAdapter extends FragmentPagerAdapter {

        public TimetablePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return TimetableCard.newInstance(position, false, Utils.DAYS[position]);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
