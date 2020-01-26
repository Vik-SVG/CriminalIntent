package com.bignerdranch.android.criminalintent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";
    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newTimeInstance(Date date){

        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
                fragment.setArguments(args);
                return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){





     /*   final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);*/

        Date date =(Date)getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);


        mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_picker);
        //init
        mTimePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = 1995;
                                /*int month = 1;
                                int day = 1;*/
                                int hour = mTimePicker.getCurrentHour();
                                int min = mTimePicker.getCurrentMinute();
                                Date date = new GregorianCalendar(year, month, day,hour,min).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();


        /*TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        tpd.setView(v);
        tpd.setTitle(R.string.date_picker_title);
        tpd.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = 1995;
                int month = 1;
                int day = 1;
                int hour = mTimePicker.getCurrentHour();
               int min = mTimePicker.getCurrentMinute();
                //int sec = mTimePicker.getDayOfMonth();
               Date date = new GregorianCalendar(year,month,day,hour,min).getTime();
                // Date date = new Date(year, month, day, hour, min);
                sendResult(Activity.RESULT_OK, date);
            }
        });
        return tpd;*/


        /*return new TimePickerDialog(getActivity(), this, hour, minute).
                //Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mTimePicker.getYear();
                                int month = mTimePicker.getMonth();
                                int day = mTimePicker.getDayOfMonth();
                                int hour = mTimePicker.getHour();
                                int min = mTimePicker.getMinute();
                                //int sec = mTimePicker.getDayOfMonth();
                                Date date = new Date(hour, min);
                                sendResult(Activity.RESULT_OK, date);

                            }
                        })
                .create();*/


        /*return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));*/




    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user  must implement onTimeListener
       // view.setIs24HourView(true);
        //DateFormat.is24HourFormat(getActivity());
    }

    private void sendResult(int resultCode, Date date){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);

    }


}
