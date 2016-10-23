package com.nickdbush.mywbgs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeworkList extends Fragment {

    @BindView(R.id.layout_list)
    ListView listHomework;

    private HomeworkAdapter homeworkAdapter;

    public HomeworkList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        ButterKnife.bind(this, view);

        RealmResults<Homework> homework = Realm.getDefaultInstance().where(Homework.class)
                .findAll();

        homeworkAdapter = new HomeworkAdapter(homework);
        listHomework.setAdapter(homeworkAdapter);

        return view;
    }

    private class HomeworkAdapter extends BaseAdapter {

        private RealmResults<Homework> homeworks;

        public HomeworkAdapter(RealmResults<Homework> homeworks) {
            this.homeworks = homeworks;
        }

        @Override
        public int getCount() {
            return homeworks.size();
        }

        @Override
        public Homework getItem(int position) {
            return homeworks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return homeworks.get(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_homework, parent, false);
            }

            Homework homework = homeworks.get(position);
            ((TextView) view.findViewById(R.id.lbl_title)).setText(homework.getTitle());
            ((TextView) view.findViewById(R.id.lbl_subject)).setText(homework.getLesson().getSubject().NAME);
            // ((TextView) item.findViewById(R.id.lbl_subject)).setTextColor(result.getLesson().getSubject().COLOR);
            CheckBox chkCompleted = (CheckBox) view.findViewById(R.id.chk_completed);
            chkCompleted.setChecked(homework.isCompleted());
//            chkCompleted.setOnCheckedChangeListener(new OnHomeworkCheckedListener(result));

            return view;
        }
    }
}
