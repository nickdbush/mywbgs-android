package com.nickdbush.mywbgs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlidePolicy;
import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.models.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class TimetableEdit extends Fragment implements ISlidePolicy {
    @BindViews({
            R.id.txtl_subject_1,
            R.id.txtl_subject_2,
            R.id.txtl_subject_3,
            R.id.txtl_subject_4,
            R.id.txtl_subject_5,
            // R.id.txtl_subject_6
    })
    List<TextInputLayout> txtlSubject;
    @BindViews({
            R.id.txtl_room_1,
            R.id.txtl_room_2,
            R.id.txtl_room_3,
            R.id.txtl_room_4,
            R.id.txtl_room_5,
            // R.id.txtl_room_6
    })
    List<TextInputLayout> txtlRoom;
    @BindView(R.id.layout_period_6)
    LinearLayout periodSixLayout;
    @BindView(R.id.lbl_day)
    TextView lblDay;

    public TimetableEdit() {

    }

    public static TimetableEdit newInstance(int day) {
        TimetableEdit fragment = new TimetableEdit();
        Bundle args = new Bundle();
        args.putInt("day", day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, view);
        int day = getArguments().getInt("day", 0);
        lblDay.setText(Utils.DAYS[day]);
        // if(day == 1 || day == 3) {
        //     periods = 6;
        //     periodSixLayout.setVisibility(View.VISIBLE);
        // }
        return view;
    }

    public boolean isValid() {
        boolean valid = true;
        for (int i = 0; i < txtlSubject.size(); i++) {
            TextInputLayout subjectInputLayout = txtlSubject.get(i);
            EditText subjectInputEditText = subjectInputLayout.getEditText();
            TextInputLayout roomInputLayout = txtlRoom.get(i);
            EditText roomInputEditText = roomInputLayout.getEditText();

            subjectInputLayout.setErrorEnabled(false);
            roomInputLayout.setErrorEnabled(false);

            if (subjectInputEditText.getText().toString().trim().length() == 0) {
                subjectInputLayout.setError("Subject name required");
                valid = false;
            }
            if (roomInputEditText.getText().toString().trim().length() == 0) {
                roomInputLayout.setError("Room required");
                valid = false;
            }
        }
        return valid;
    }

    public Lesson[] harvest() {
        Lesson[] lessons = new Lesson[txtlSubject.size()];
        if (!isValid()) return null;
        for (int i = 0; i < lessons.length; i++) {
            TextInputLayout subjectInputLayout = txtlSubject.get(i);
            EditText subjectInputEditText = subjectInputLayout.getEditText();
            TextInputLayout roomInputLayout = txtlRoom.get(i);
            EditText roomInputEditText = roomInputLayout.getEditText();

            Lesson lesson = new Lesson();
            lesson.generateId();
            lesson.setDay(getArguments().getInt("day", 0));
            lesson.setPeriod(i);
            lesson.setSubject(subjectInputEditText.getText().toString());
            lesson.setRoom(roomInputEditText.getText().toString());
            lessons[i] = lesson;
        }

        return lessons;
    }

    @Override
    public boolean isPolicyRespected() {
        return isValid();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {

    }
}
