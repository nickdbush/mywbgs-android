package com.nickdbush.mywbgs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Utils;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;
import com.nickdbush.mywbgs.ui.cards.TimetableCard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayPage extends Fragment {
    @BindView(R.id.layout_cards) LinearLayout cardLayout;

    public TodayPage() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Today");
    }

    public static TodayPage newInstance() {
        TodayPage fragment = new TodayPage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_today, container, false);
        ButterKnife.bind(this, view);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout_cards, TimetableCard.newInstance(Utils.getClosestSchoolDay(), "Timetable"));
        fragmentTransaction.add(R.id.layout_cards, HomeworkCard.newInstance());
        fragmentTransaction.commit();

        return view;
    }
}
