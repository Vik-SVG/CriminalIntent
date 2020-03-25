package com.bignerdranch.android.criminalintent.swipers_helpers;


public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}
