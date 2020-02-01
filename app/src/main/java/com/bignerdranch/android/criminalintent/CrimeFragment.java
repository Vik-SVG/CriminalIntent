package com.bignerdranch.android.criminalintent;


//import android.app.Activity;
//import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import java.util.List;
import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE ="DialogDate";
    private static final String DIALOG_TIME ="DialogTime";
    private static int REQUEST_DATE = 1;
    private static int REQUEST_TIME = 0;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mTimeButton;



    // public void returnResult(){
    //   getActivity().setResult(Activity.RESULT_OK, null);
    //}


    public static CrimeFragment newInstance (UUID crimeID){
    Bundle args = new Bundle();
    args.putSerializable(ARG_CRIME_ID, crimeID);

    CrimeFragment fragment = new CrimeFragment();
    fragment.setArguments(args);
    return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
      //  UUID crimeID = (UUID) getActivity().getIntent()
        //        .getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);

        UUID crimeID = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty space
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                //empty space
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
//        mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()));
                //(mCrime.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View V){

            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);

            /*Intent intent = DatePickerActivity.newIntent(getContext(), mCrime.getDate());
            startActivityForResult(intent, REQUEST_DATE);*/

        }
                                       });

        mTimeButton = v.findViewById(R.id.crime_time);

        mTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager1 = getFragmentManager();
                TimePickerFragment dialog1 = TimePickerFragment.newTimeInstance(mCrime.getDate());
                dialog1.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog1.show(manager1, DIALOG_TIME);

            }

        });


        updateDate();

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crimeSolved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return v;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            Date actualDate = mCrime.getDate();
            int hours = date.getHours();
            int min = date.getMinutes();
            actualDate.setHours(hours);
            actualDate.setMinutes(min);

            mCrime.setDate(actualDate);
            updateDate();
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            Date actualDate = mCrime.getDate();
           /* int years = date.getYear();
            int day = date.getDay();
            int month = date.getMonth();
            actualDate.setYear(years);
            actualDate.setMonth(month);*/
            actualDate.setTime(date.getTime());

            mCrime.setDate(actualDate);

            updateDate();

        }



    }

    private void updateDate() {
       // mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()));
        mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getDate()));
       // mTimeButton.setText(DateFormat.format("H:m:s", mCrime.getDate()));
    }

}
