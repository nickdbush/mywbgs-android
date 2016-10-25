package com.nickdbush.mywbgs.ui.cards;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class Card extends Fragment {
    protected OnCardClickedListener onCardClickedListener;

    @Override
    public void onAttach(Context context) {
        try {
            if (getParentFragment() != null)
                onCardClickedListener = (OnCardClickedListener) getParentFragment();
            else
                onCardClickedListener = (OnCardClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCardClickedListener");
        }
        super.onAttach(context);
    }

    public interface OnCardClickedListener {
        void onClick(Card card);
    }

    protected class OnClickedListener implements View.OnClickListener {
        private Card card;

        public OnClickedListener(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View view) {
            onCardClickedListener.onClick(card);
        }
    }
}
