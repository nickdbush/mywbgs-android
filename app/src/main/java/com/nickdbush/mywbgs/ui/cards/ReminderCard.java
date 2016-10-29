package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Reminder;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class ReminderCard extends Card {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.lbl_empty)
    TextView lblEmpty;

    private String title;
    private LocalDate date;

    private Realm realm;

    public ReminderCard() {

    }

    public static ReminderCard newInstance(LocalDate date) {
        ReminderCard reminderCard = new ReminderCard();
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);
        reminderCard.setArguments(bundle);
        return reminderCard;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = "Reminders";
        date = (LocalDate) getArguments().getSerializable("date");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);

        // Rounded corners!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setClipToOutline(true);
        }

        RealmResults<Reminder> results = realm.where(Reminder.class)
                .equalTo("day", date.getDayOfWeek() - 1)
                .findAll();

        for (Reminder result : results) {
            // If it is not a reminder for today then ignore it
            if (result.getDate() != null && date.isEqual(result.getDate())) continue;
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_reminder, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getTitle());
            linearLayout.addView(item);
        }

        if (results.size() == 0) {
            lblEmpty.setText("No reminders today");
            lblEmpty.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
