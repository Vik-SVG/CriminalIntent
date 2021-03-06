package com.bignerdranch.android.criminalintent;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bignerdranch.android.criminalintent.picture_helpers.PhotoViewerFragment;
import com.bignerdranch.android.criminalintent.picture_helpers.PictureUtils;
import com.bignerdranch.android.criminalintent.time_pickers.DatePickerFragment;
import com.bignerdranch.android.criminalintent.time_pickers.TimePickerFragment;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import leakcanary.AppWatcher;
import leakcanary.LeakCanary;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE ="DialogDate";
    private static final String DIALOG_TIME ="DialogTime";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    private Callbacks mCallbacks2;
    private File mPhotoFile;
    private CheckBox mRequiredPolice;
    private Button mCallSuspect;
    private int mPhotoWidth;
    private int mPhotoHeight;




public interface Callbacks{
    void onCrimeUpdated(Crime crime);
    void onCrimeDeletion(Crime crime);
}
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
    public void onAttach(Context context){
    super.onAttach(context);
    mCallbacks = (Callbacks) context;
    mCallbacks2 = (Callbacks) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
      //  UUID crimeID = (UUID) getActivity().getIntent()
        //        .getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);

        UUID crimeID = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        setHasOptionsMenu(true);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);



    }

    @Override
    public void onPause(){
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach(){
    super.onDetach();
    mCallbacks = null;
    mCallbacks2 = null;
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
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

                //empty space
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View V){

            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);

            /*Intent intent = DatePickerActivity.newIntent(getContext(), mCrime.getDate());
            startActivityForResult(intent, REQUEST_DATE);    //start Dialog like new Activity*/

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


        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crimeSolved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCrime.setSolved(isChecked);
                    updateCrime();
            }
        });

        mRequiredPolice = (CheckBox) v.findViewById(R.id.police_required);
        mRequiredPolice.setChecked(mCrime.isRequiredPolice());
        mRequiredPolice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setRequiredPolice(isChecked);
                updateCrime();
            }
        });


        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            Intent i = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .getIntent()
                    .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));


               /* new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));*/
            i = Intent.createChooser(i, getString(R.string.send_report));
            startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });



        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mCallSuspect = v.findViewById(R.id.call_suspect_button);
        mCallSuspect.setEnabled(mCrime.getSuspect()!=null && mCrime.getSuspectPhone()!=null);
        mCallSuspect.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){

                Intent call = new Intent(Intent.ACTION_DIAL);
                String numb = mCrime.getSuspectPhone();
                call.setData(Uri.parse("tel:"+numb));//change the number
                startActivity(call);

            }
        });


        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager)!=null;

        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), //intent for opening camera and making shots, however it's not scaled/. Should be replaced with camerax or fotofile
                        "com.bignerdranch.android.criminalintent.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
            for(ResolveInfo activity : cameraActivities){
                getActivity().grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(captureImage, REQUEST_PHOTO);

            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        /*File photo = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        Uri mUri = FileProvider.getUriForFile(getActivity(),
                "com.bignerdranch.android.criminalintent.fileprovider",
                photo);

       Glide.with(getContext()).load(mUri).into(mPhotoView);*/


        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mPhotoWidth = mPhotoView.getMeasuredWidth();
                    mPhotoHeight = mPhotoView.getMeasuredHeight();

                    updatePhotoView();
                }
            });
        }

        mPhotoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CrimeFragment", "click on photo");
                if (mPhotoFile==null || !mPhotoFile.exists()){ return;} else {
                    FragmentManager manager = /*getFragmentManager();*/ getParentFragmentManager();
                    PhotoViewerFragment dialog = PhotoViewerFragment.newInstance(mPhotoFile);
                    dialog.show(manager, DIALOG_PHOTO);

                   // FragmentTransaction transaction = manager.beginTransaction();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    //ransaction.add(android.R.id.content, dialog).addToBackStack(null).commit();

                }
            }
        });





        updatePhotoView();

        updateDate();

        return v;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_delete_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_button:

                UUID crimeIDmenu = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
                Crime tCrime = CrimeLab.get(getActivity()).getCrime(crimeIDmenu);
                CrimeLab.get(getActivity()).deleteCrime(tCrime.getID());

                // Back to CrimeListFragment by Intent.
              // updateCrime();
               /* DisplayMetrics metrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(metrics);*/
               // mCallbacks2.onCrimeDeletion(tCrime);

                boolean hasTwoPanes = getResources().getBoolean(R.bool.has_two_panes);
                if(hasTwoPanes){
                    mCallbacks2.onCrimeDeletion(tCrime);

                } else{
                    getActivity().finish();
                }




            /*    if(metrics.densityDpi == DisplayMetrics.DENSITY_LOW || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
                    getActivity().finish(); //закрывает активность при удалении Crime на планшете и телефоне
                } else{
                   getActivity().finish();
                }*/

               /* Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                startActivity(intent);*/
               return true;

                /*Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getID());
                startActivity(intent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

         /*   Date actualDate = mCrime.getDate();
            int hours = date.getHours();
            int min = date.getMinutes();
            actualDate.setHours(hours);
            actualDate.setMinutes(min);*/

            mCrime.setTime(date);
            updateCrime();
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
            updateCrime();
            updateDate();

        }


        if(requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();

            String[] projection    = new String[] {Phone.DISPLAY_NAME,
                    Phone.NUMBER
                    };

            Cursor people = getActivity().getContentResolver().query(contactUri, projection, null, null, null);

            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
          int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

          try{
              if(people.getCount()==0) {
              return;
              }
              if (people.moveToFirst())
                  do {
                      String name = people.getString(0);
                      mCrime.setSuspect(name);
                    String number = people.getString(indexNumber);
                    mCrime.setSuspectPhone(number);
                  }while (people.moveToNext());
                } finally {
              people.close();
          }
          updateDate();

        }

        if(requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.criminalintent.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
            updateCrime();
        }

    }

    private void updateCrime(){
    CrimeLab.get(getActivity()).updateCrime(mCrime);
    mCallbacks.onCrimeUpdated(mCrime);

    }

    private void updateDate() {

    String localeDateFormat = getResources().getString(R.string.date_format);

        mDateButton.setText(DateFormat.format(localeDateFormat, mCrime.getDate()));

        mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getTime()));
        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }
    }


    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect==null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile ==null|| !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), mPhotoWidth, mPhotoHeight
            );

            Glide.with(getContext()).load(bitmap).into(mPhotoView);

          //  mPhotoView.setImageBitmap(bitmap);
        }
    }

}
