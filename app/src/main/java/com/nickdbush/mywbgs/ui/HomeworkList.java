package com.nickdbush.mywbgs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;
import com.nickdbush.mywbgs.models.Utils;
import com.nickdbush.mywbgs.ui.cards.Card;
import com.nickdbush.mywbgs.ui.cards.HomeworkCard;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkList extends Fragment implements Card.OnCardClickedListener {

    @BindView(R.id.layout_cards)
    LinearLayout cardList;
    @BindView(R.id.layout_empty)
    RelativeLayout emptyLayout;

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
        View view = inflater.inflate(R.layout.fragment_homework_list, container, false);
        ButterKnife.bind(this, view);

        HashMap<LocalDate, HomeworkCard> cards = new HashMap<LocalDate, HomeworkCard>();

        RealmResults<Homework> results = Realm.getDefaultInstance().where(Homework.class)
                // TODO: 25/10/2016 Filtering
                .greaterThanOrEqualTo("dueDate", new LocalDate().toDate())
                .or()
                .equalTo("completed", false)
                .findAll()
                .sort("dueDate");

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null) ft.remove(fragment);
            }
        }
        ft.commitNow();

        LocalDate currentDate = new LocalDate();
        for (Homework homework : results) {
            if (!cards.containsKey(homework.getDueDate())) {
                String date = Utils.getHelpfulDate(homework.getDueDate());
                if (homework.getDueDate().isBefore(currentDate)) date += " (overdue)";
                HomeworkCard homeworkCard = HomeworkCard.newInstance(homework.getDueDate(), date, true);
                cards.put(homework.getDueDate(), homeworkCard);
                ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.layout_cards, homeworkCard, homework.getDueDate().toString());
                ft.commit();
            }
        }

        if (results.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            cardList.setVisibility(View.GONE);
        }

        // Enable the options menu
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(Card card) {

    }
}
