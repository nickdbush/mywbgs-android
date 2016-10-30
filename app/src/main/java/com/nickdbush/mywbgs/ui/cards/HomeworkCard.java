package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkCard extends Card {

    @BindView(R.id.layout_linear_list)
    LinearLayout linearLayout;
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.lbl_empty)
    TextView lblEmpty;

    private String title;
    private LocalDate date;

    private OnHomeworkClickedListener onHomeworkClickedListener;
    private Realm realm;

    public HomeworkCard() {

    }

    public static HomeworkCard newInstance(LocalDate date, boolean homeworkClickable) {
        HomeworkCard fragment = new HomeworkCard();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        args.putBoolean("homeworkClickable", homeworkClickable);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeworkCard newInstance(LocalDate date, String title, boolean homeworkClickable) {
        HomeworkCard fragment = new HomeworkCard();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        args.putString("title", title);
        args.putBoolean("homeworkClickable", homeworkClickable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title", "Homework");
        date = (LocalDate) getArguments().getSerializable("date");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onAttach(Context context) {
        if (getArguments().getBoolean("homeworkClickable", false)) {
            try {
                onHomeworkClickedListener = (OnHomeworkClickedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement OnHomeworkClickedListener");
            }
        }
        super.onAttach(context);
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

        RealmResults<Homework> results = realm.where(Homework.class)
                .equalTo("dueDate", date.toDate())
                .findAll()
                .sort("period");

        for (final Homework result : results) {
            View item = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_homework, null);
            ((TextView) item.findViewById(R.id.lbl_title)).setText(result.getTitle());
            ((TextView) item.findViewById(R.id.lbl_subject)).setText(result.getLesson().getSubject());
            // ((TextView) item.findViewById(R.id.lbl_subject)).setTextColor(result.getLesson().getSubject().COLOR);

            final CheckBox chkCompleted = (CheckBox) item.findViewById(R.id.chk_completed);
            chkCompleted.setOnCheckedChangeListener(null);
            chkCompleted.setChecked(result.isCompleted());
            final OnCheckedChangeListener onCheckedChangeListener = new OnHomeworkCheckedListener(result);
            chkCompleted.setOnCheckedChangeListener(onCheckedChangeListener);

            if (getArguments().getBoolean("homeworkClickable", false)) {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onHomeworkClickedListener.onHomeworkClicked(result);
                    }
                });
            }

            linearLayout.addView(item);
        }

        view.setOnClickListener(new OnClickedListener(this));

        if (results.size() == 0) {
            lblEmpty.setText("No homework due for today");
            lblEmpty.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public interface OnHomeworkClickedListener {
        void onHomeworkClicked(Homework homework);
    }

    private class OnHomeworkCheckedListener implements OnCheckedChangeListener {
        private Homework homework;

        public OnHomeworkCheckedListener(Homework homework) {
            this.homework = homework;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            realm.beginTransaction();
            homework.setCompleted(isChecked);
            realm.commitTransaction();
        }
    }
}
