package com.nickdbush.mywbgs.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayPage extends Fragment {

    @BindView(R.id.lbl_date)
    TextView lblDate;
    @BindView(R.id.layout_cards)
    LinearLayout layoutCards;

    private LocalDate date;

    public DayPage() {
    }

    public static DayPage newInstance(LocalDate date) {
        DayPage fragment = new DayPage();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: 20/10/2016 Should @Nullable be on every Fragment#onCreate()?
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = (LocalDate) getArguments().getSerializable("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_day, container, false);
        ButterKnife.bind(this, view);

        lblDate.setText(date.toString("EEEE d MMMM yyyy"));

        if (Utils.getCurrentDay().equals(date)) {
            lblDate.setText("Today");
            lblDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        Fragment[] cards = {
                TimetableCard.newInstance(date),
                HomeworkCard.newInstance(date),
                CalendarCard.newInstance(date)
        };

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (cards.length > 0) {
            ft.replace(R.id.layout_cards, cards[0]);
            for (int i = 1; i < cards.length; i++) {
                ft.add(R.id.layout_cards, cards[i]);
            }
        }
        ft.commitNow();

        return view;
    }
}
