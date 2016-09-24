package com.nickdbush.mywbgs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Utils;
import com.nickdbush.mywbgs.ui.cards.CalendarCard;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;
import com.nickdbush.mywbgs.ui.cards.TimetableCard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayPage extends Fragment {
    @BindView(R.id.layout_cards)
    LinearLayout cardLayout;
    @BindView(R.id.lbl_date)
    TextView lblDate;

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

        lblDate.setText(Utils.getCurrentSchoolDay().toString("EEEE d MMMM"));

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        // TODO: 24/09/2016 Check there is actually homework and a timetable
        fragmentTransaction.add(R.id.layout_cards, TimetableCard.newInstance(Utils.getCurrentSchoolDay().getDayOfWeek() - 1, "Timetable"));
        fragmentTransaction.add(R.id.layout_cards, HomeworkCard.newInstance());
        fragmentTransaction.add(R.id.layout_cards, CalendarCard.newInstance());
        fragmentTransaction.commit();

        return view;
    }

}
