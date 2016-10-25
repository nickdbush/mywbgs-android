package com.nickdbush.mywbgs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.models.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkList extends Fragment {

    @BindView(R.id.layout_list)
    ListView listHomework;
    @BindView(R.id.layout_empty)
    RelativeLayout emptyLayout;

    private OnHomeworkClickedListener onHomeworkClickedListener;

    public HomeworkList() {
    }

    public static HomeworkList newInstance() {
        HomeworkList fragment = new HomeworkList();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            onHomeworkClickedListener = (OnHomeworkClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnHomeworkClickedListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_homework_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        ButterKnife.bind(this, view);

        RealmResults<Homework> homework = Realm.getDefaultInstance().where(Homework.class)
                .findAll()
                .sort("dueDate");

        final HomeworkAdapter homeworkAdapter = new HomeworkAdapter(homework, onHomeworkClickedListener);
        listHomework.setAdapter(homeworkAdapter);

        listHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(getClass().getSimpleName(), String.valueOf(i));
                onHomeworkClickedListener.onHomeworkClicked(homeworkAdapter.getItem(i));
            }
        });

        if (homework.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            listHomework.setVisibility(View.GONE);
        }

        // Enable the options menu
        setHasOptionsMenu(true);

        return view;
    }

    // TODO: 24/10/2016 Extend OnClickListener
    public interface OnHomeworkClickedListener {
        void onHomeworkClicked(Homework homework);
    }

    private class HomeworkAdapter extends BaseAdapter {

        private RealmResults<Homework> homeworks;
        private OnHomeworkClickedListener onHomeworkClickedListener;

        public HomeworkAdapter(RealmResults<Homework> homeworks, OnHomeworkClickedListener onHomeworkClickedListener) {
            this.homeworks = homeworks;
            this.onHomeworkClickedListener = onHomeworkClickedListener;
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


            final Homework homework = homeworks.get(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onHomeworkClickedListener.onHomeworkClicked(homework);
                }
            });
            ((TextView) view.findViewById(R.id.lbl_title)).setText(homework.getTitle());
            ((TextView) view.findViewById(R.id.lbl_subject)).setText(homework.getLesson().getSubject().NAME + " - " + Utils.getDayOfWeekAsString(homework.getDueDate()));

            CheckBox chkCompleted = (CheckBox) view.findViewById(R.id.chk_completed);
            chkCompleted.setOnCheckedChangeListener(null);
            chkCompleted.setChecked(homework.isCompleted());
            chkCompleted.setOnCheckedChangeListener(new OnHomeworkCheckedListener(homework));

            return view;
        }

        private class OnHomeworkCheckedListener implements CompoundButton.OnCheckedChangeListener {
            private Homework homework;

            public OnHomeworkCheckedListener(Homework homework) {
                this.homework = homework;
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                homework.setCompleted(isChecked);
                realm.commitTransaction();
            }
        }
    }
}
