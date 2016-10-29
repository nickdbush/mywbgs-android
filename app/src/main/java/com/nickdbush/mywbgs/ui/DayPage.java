package com.nickdbush.mywbgs.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.HomeworkActivity;
import com.nickdbush.mywbgs.MainActivity;
import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.ui.cards.CalendarCard;
import com.nickdbush.mywbgs.ui.cards.Card;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;
import com.nickdbush.mywbgs.ui.cards.TimetableCard;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayPage extends Fragment implements Card.OnCardClickedListener, HomeworkCard.OnHomeworkClickedListener {

    @BindView(R.id.lbl_date)
    TextView lblDate;
    @BindView(R.id.layout_cards)
    LinearLayout layoutCards;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_next)
    ImageView imgNext;

    private LocalDate date;
    private DayPageListeners dayPageListeners;

    public DayPage() {
    }

    public static DayPage newInstance(LocalDate date) {
        DayPage fragment = new DayPage();
        Bundle args = new Bundle();
        args.putSerializable("stateDate", date);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: 20/10/2016 Should @Nullable be on every Fragment#onCreate()?
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = (LocalDate) getArguments().getSerializable("stateDate");
    }

    @Override
    public void onAttach(Context context) {
        try {
            dayPageListeners = (DayPageListeners) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DayPageListeners");
        }
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        generateCards();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, view);

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayPageListeners.next();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayPageListeners.back();
            }
        });
        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dayPageListeners.dateChanged(new LocalDate(i, i1 + 1, i2));
                    }
                }, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
                // dialog.setTitle("Go to date");
                dialog.getDatePicker().setMinDate(new LocalDate().minusDays(MainActivity.DAYS_BACK).toDate().getTime());
                dialog.getDatePicker().setMaxDate(new LocalDate().plusDays(MainActivity.DAYS_FORWARDS).toDate().getTime());
                dialog.show();
            }
        });

        lblDate.setText(date.toString("EEEE d MMMM yyyy"));
        generateCards();
        return view;
    }

    private void generateCards() {
        TimetableCard timetableCard = TimetableCard.newInstance(date);
        HomeworkCard homeworkCard = HomeworkCard.newInstance(date, false);
        CalendarCard calendarCard = CalendarCard.newInstance(date);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.layout_cards, timetableCard, "timetable");
        ft.commit();
        ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.layout_cards, homeworkCard, "homework");
        ft.commit();
        ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.layout_cards, calendarCard, "calendar");
        ft.commit();
    }

    @Override
    public void onClick(Card card) {
        if (card instanceof HomeworkCard) {
            Intent intent = new Intent(getContext(), HomeworkActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onHomeworkClicked(Homework homework) {

    }

    public interface DayPageListeners {
        void next();

        void back();

        void dateChanged(LocalDate date);
    }
}
