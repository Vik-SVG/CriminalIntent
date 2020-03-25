package com.bignerdranch.android.criminalintent.time_pickers;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.bignerdranch.android.criminalintent.SingleFragmentActivity;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {
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
