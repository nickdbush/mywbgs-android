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
import com.nickdbush.mywbgs.models.Event;
import com.nickdbush.mywbgs.models.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class CalendarCard extends Fragment {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title)
    TextView lblTitle;

    private String title;

    public CalendarCard() {

    }

    public static CalendarCard newInstance() {
        CalendarCard fragment = new CalendarCard();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = "Calendar";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);
        lblTitle.setText(title);

        RealmResults<Event> events = Realm.getDefaultInstance().where(Event.class)
                .between("start", Utils.getCurrentSchoolDay().toDate(), Utils.getCurrentSchoolDay().plusDays(1).toDate())
                .findAll();

        for (Event result : events) {
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_event, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getTitle());
            ((TextView) item.findViewById(R.id.lbl_time)).setText(result.getDurationString());
            linearLayout.addView(item);
        }

        return view;
    }

}
