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
import android.widget.EditText;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeworkEdit extends Fragment {

    @BindView(R.id.txt_title)
    EditText txtTitle;
    @BindView(R.id.txt_description)
    EditText txtDescription;

    private Homework homework;
    private OnSaveListener onSaveListener;

    public HomeworkEdit() {
    }

    public static HomeworkEdit newInstance(Homework homework) {
        HomeworkEdit fragment = new HomeworkEdit();
        Bundle args = new Bundle();
        if (homework != null) args.putLong("homeworkId", homework.getId());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            onSaveListener = (OnSaveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnSaveListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_homework_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Realm realm = Realm.getDefaultInstance();
        if (item.getItemId() == R.id.action_save) {
            realm.beginTransaction();
            if (homework == null)
                homework = Realm.getDefaultInstance().createObject(Homework.class);
            homework.setTitle(txtTitle.getText().toString().trim());
            homework.setDescription(txtDescription.getText().toString().trim());
            realm.commitTransaction();
            onSaveListener.onSave();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            if (homework != null) {
                realm.beginTransaction();
                homework.deleteFromRealm();
                realm.commitTransaction();
            }
            onSaveListener.onSave();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework_edit, container, false);
        Log.d(getClass().getSimpleName(), "Binding!");
        ButterKnife.bind(this, view);

        if (getArguments().containsKey("homeworkId")) {
            homework = Realm.getDefaultInstance().where(Homework.class)
                    .equalTo("id", getArguments().getLong("homeworkId")).findFirst();
            txtTitle.setText(homework.getTitle());
            txtDescription.setText(homework.getDescription());
        }

        // Enable the options menu
        setHasOptionsMenu(true);

        return view;
    }

    public interface OnSaveListener {
        void onSave();
    }


}
