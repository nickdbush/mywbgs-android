package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Lesson;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class TimetableCard extends Fragment {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title) TextView lblTitle;

    private int day;
    private String title;

    public TimetableCard() {

    }

    public static TimetableCard newInstance(int day, String title) {
        TimetableCard fragment = new TimetableCard();
        Bundle args = new Bundle();
        args.putInt("day", day);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day = 0;
        title = "Timetable";
        if (getArguments() != null) {
            day = getArguments().getInt("day", 0);
            title = getArguments().getString("title", "Timetable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_timetable, container, false);
        ButterKnife.bind(this, view);
        lblTitle.setText(title);

        RealmResults<Lesson> results = Realm.getDefaultInstance().where(Lesson.class)
                .equalTo("day", day)
                .findAll();

        for (Lesson result : results) {
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_timetable, null);
            ((TextView) item.findViewById(R.id.lbl_subject)).setText(result.getSubject());
            ((TextView) item.findViewById(R.id.lbl_time)).setText(result.getPeriod().getDurationString());
            ((TextView) item.findViewById(R.id.lbl_room)).setText(result.getRoom());
            linearLayout.addView(item);
        }

        return view;
    }

}
