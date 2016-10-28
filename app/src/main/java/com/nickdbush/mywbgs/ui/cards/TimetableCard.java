package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.models.Utils;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class TimetableCard extends Card {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.lbl_empty)
    TextView lblEmpty;

    private LocalDate date;
    private String title;

    private Realm realm;

    public TimetableCard() {

    }

    public static TimetableCard newInstance(LocalDate date) {
        TimetableCard fragment = new TimetableCard();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = "Timetable";
        date = (LocalDate) getArguments().getSerializable("date");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);
        lblTitle.setText(title);

        // Rounded corners!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setClipToOutline(true);
        }

        RealmResults<Lesson> results = realm.where(Lesson.class)
                .equalTo("day", date.getDayOfWeek() - 1)
                .findAll();

        int nextPeriod = 0;
        for (int i = 0; i < results.size(); i++) {
            Lesson result = results.get(i);
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_timetable, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getSubject());
            // ((TextView) item.findViewById(R.id.lbl_title)).setTextColor(result.getSubject().COLOR);
            ((TextView) item.findViewById(R.id.lbl_time)).setText(result.getPeriod().getDurationString());
            ((TextView) item.findViewById(R.id.lbl_room)).setText(result.getRoom());
            // Only highlight next period if today's timetable
            if (Utils.getCurrentDay().equals(date)) {
                if (result.isPassed()) {
                    nextPeriod++;
                } else if (nextPeriod == i) {
                    // item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    // ((TextView) item.findViewById(R.id.lbl_title)).setTextColor(Color.WHITE);
                    // ((TextView) item.findViewById(R.id.lbl_room)).setTextColor(Color.WHITE);
                    // ((TextView) item.findViewById(R.id.lbl_time)).setTextColor(Color.WHITE);
                    item.findViewById(R.id.view_colour).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    ((TextView) item.findViewById(R.id.lbl_title)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    ((TextView) item.findViewById(R.id.lbl_room)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    ((TextView) item.findViewById(R.id.lbl_time)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                }
            }
            linearLayout.addView(item);
        }

        if (results.size() == 0) {
            lblEmpty.setText("No lessons today");
            lblEmpty.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
