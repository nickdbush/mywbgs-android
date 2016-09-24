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
import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.models.Utils;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkCard extends Fragment {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title)
    TextView lblTitle;

    private String title;

    public HomeworkCard() {

    }

    public static HomeworkCard newInstance() {
        HomeworkCard fragment = new HomeworkCard();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = "Homework";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.bind(this, view);
        lblTitle.setText(title);

        RealmResults<Homework> results = Realm.getDefaultInstance().where(Homework.class)
                .equalTo("dueDate", new LocalDate().toDate())
                .findAll();

        for (Homework result : results) {
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_homework, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getTitle());
            ((TextView) item.findViewById(R.id.lbl_subject)).setText(result.getLesson().getSubject());
            ((TextView) item.findViewById(R.id.lbl_period)).setText(result.getLesson().getPeriod().getPeriodName());
            linearLayout.addView(item);
        }

        return view;
    }

}
