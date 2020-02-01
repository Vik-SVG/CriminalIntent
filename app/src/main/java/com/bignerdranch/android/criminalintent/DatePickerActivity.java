package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity{
    private static final String EXTRA_CRIME_DATE =
            "com.bignerdranch.android.criminalintent.crime_date"; //crime

    public static Intent newIntent(Context packageContext, Date date) {
        Intent intent = new Intent(packageContext, DatePickerActivity.class);
        intent.putExtra(EXTRA_CRIME_DATE, date);
        return intent;
    }

    //@Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_DATE); //getIntent().getSerializableExtra
        return DatePickerFragment.newInstance(date);
    }

}
