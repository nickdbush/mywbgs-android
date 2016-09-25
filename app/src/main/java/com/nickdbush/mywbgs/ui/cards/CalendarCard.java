package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Event;
import com.nickdbush.mywbgs.models.Utils;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.Arrays;
import java.util.Comparator;

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

        LocalDateTime current = Utils.getCurrentSchoolDay().toLocalDateTime(new LocalTime(0, 0, 0));

        RealmResults<Event> events = Realm.getDefaultInstance().where(Event.class)
                .lessThan("start", current.plusDays(1).toDate())
                .greaterThan("end", current.toDate())
                .findAll();

        Event[] orderedEvents = Arrays.copyOf(events.toArray(), events.size(), Event[].class);
        Arrays.sort(orderedEvents, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if (o1.getPriority() < o2.getPriority())
                    return -1;
                if (o1.getPriority() > o2.getPriority())
                    return 1;
                return 0;
            }
        });

        Log.d("MyWBGS", "CURRENT_START: " + current.toString());
        Log.d("MyWBGS", "CURRENT_END: " + current.plusDays(1).toString());

        for (Event result : orderedEvents) {
            Log.d("MyWBGS", "START: " + result.getStart().toString());
            Log.d("MyWBGS", "END: " + result.getEnd().toString());
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_event, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getTitle());
            ((TextView) item.findViewById(R.id.lbl_time)).setText(result.getDurationString());
            linearLayout.addView(item);
        }

        return view;
    }

}
