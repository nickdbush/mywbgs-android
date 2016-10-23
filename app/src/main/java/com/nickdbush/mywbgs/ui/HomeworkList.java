package com.nickdbush.mywbgs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

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

    @BindView(R.id.layout_cards)
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

        homeworkAdapter = new HomeworkAdapter();
        listHomework.setAdapter(homeworkAdapter);

        return view;
    }

    private class HomeworkAdapter extends BaseAdapter {

        private RealmResults<Homework> homework;

        public HomeworkAdapter(RealmResults<Homework> homework) {
            this.homework = homework;
        }

        @Override
        public int getCount() {
            return homework.size();
        }

        @Override
        public Homework getItem(int position) {
            return homework.get(position);
        }

        @Override
        public long getItemId(int position) {
            return homework.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
