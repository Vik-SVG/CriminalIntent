package com.bignerdranch.android.criminalintent.time_pickers;

import android.app.Activity;
//import android.app.Dialog;
//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;
    public Button mDatePickerButton;


    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        /*View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);*/

        View v = inflater.inflate(R.layout.dialog_date, container, false);



        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);


        mDatePickerButton = (Button) v.findViewById(R.id.date_pick_butt);

        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day, hour, min).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        });

        return v;


               /* new AlertDialog.Builder(getActivity())
                .setView(v)
        .setTitle(R.string.date_picker_title)
        .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                            Date date = new GregorianCalendar(year, month, day, hour, min).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();*/
    }

    private void sendResult(int resultCode, Date date){



        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);



        if(getTargetFragment()==null){
            Activity hostingActivity = getActivity();
            hostingActivity.setResult(resultCode, intent);
            hostingActivity.finish();
            return;
        } else{
            dismiss();
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), resultCode, intent);
        }

    }
}
