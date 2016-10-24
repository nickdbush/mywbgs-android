package com.nickdbush.mywbgs.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.models.Utils;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkEdit extends Fragment {

    @BindView(R.id.txt_title)
    EditText txtTitle;
    @BindView(R.id.txt_description)
    EditText txtDescription;
    @BindView(R.id.lbl_date)
    TextView lblDate;
    @BindView(R.id.spinner_subject)
    Spinner spinnerSubject;

    private Homework homework;
    private LocalDate selectedDate;
    private LessonAdapter lessonAdapter;
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
            homework.setDueDate(selectedDate);
            homework.setPeriod(lessonAdapter.getItem(spinnerSubject.getSelectedItemPosition()).getRawPeriod());
            realm.commitTransaction();
            onSaveListener.onSave();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete homework?")
                    .setMessage("You can't get it back!")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (homework != null) {
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                homework.deleteFromRealm();
                                realm.commitTransaction();
                            }
                            onSaveListener.onSave();
                        }
                    }).show();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework_edit, container, false);
        ButterKnife.bind(this, view);

        if (getArguments().containsKey("homeworkId")) {
            homework = Realm.getDefaultInstance().where(Homework.class)
                    .equalTo("id", getArguments().getLong("homeworkId")).findFirst();
            txtTitle.setText(homework.getTitle());
            txtDescription.setText(homework.getDescription());
            selectedDate = homework.getDueDate();
        } else {
            // TODO: 24/10/2016 Test this! 
            selectedDate = Utils.getNextSchoolDay();
        }
        updateSelectedDate();

        RealmResults<Lesson> lessons = Realm.getDefaultInstance().where(Lesson.class)
                .equalTo("day", selectedDate.getDayOfWeek() - 1)
                .findAll();
        lessonAdapter = new LessonAdapter(getContext(), lessons);
        spinnerSubject.setAdapter(lessonAdapter);

        if (homework != null) spinnerSubject.setSelection(homework.getRawPeriod());

        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        LocalDate newDate = new LocalDate(year, month + 1, day);
                        if (newDate.getDayOfWeek() == 6 || newDate.getDayOfWeek() == 7) {
                            Snackbar.make(lblDate, "Selected date must not be on the weekend", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        selectedDate = newDate;
                        updateSelectedDate();
                        updateSpinnerAdapter();
                    }
                }, selectedDate.getYear(), selectedDate.getMonthOfYear() - 1, selectedDate.getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate(new LocalDate().toDate().getTime());
                datePickerDialog.show();
            }
        });
        // Enable the options menu
        setHasOptionsMenu(true);

        return view;
    }

    private void updateSelectedDate() {
        lblDate.setText(dateToString(selectedDate));
    }

    private void updateSpinnerAdapter() {
        RealmResults<Lesson> lessons = Realm.getDefaultInstance().where(Lesson.class)
                .equalTo("day", selectedDate.getDayOfWeek() - 1)
                .findAll();
        lessonAdapter.lessons = lessons;
        lessonAdapter.notifyDataSetChanged();
    }

    private String dateToString(LocalDate date) {
        return date.toString("E, d MMM");
    }

    public interface OnSaveListener {
        void onSave();
    }

    private class LessonAdapter extends BaseAdapter {

        private Context context;
        public List<Lesson> lessons;

        public LessonAdapter(Context context, List<Lesson> lessons) {
            this.context = context;
            this.lessons = lessons;
        }

        @Override
        public int getCount() {
            return lessons.size();
        }

        @Override
        public Lesson getItem(int i) {
            return lessons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return lessons.get(i).getId();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = ((LayoutInflater) (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.item_spinner, viewGroup, false);
            }
            ((TextView) (convertView.findViewById(R.id.lbl_text))).setText(getItem(i).getSubject().NAME);
            return convertView;
        }
    }
}
