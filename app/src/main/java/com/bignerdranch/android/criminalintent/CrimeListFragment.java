package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;

//import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final int REQUEST_CRIME = 1;
    private int mLastClikedPosition=-1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public  void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {
            if (mLastClikedPosition > -1) {
                mAdapter.notifyItemChanged(mLastClikedPosition);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        private void bind(Crime crimeT){
            mCrime = crimeT;
            mTitleTextView.setText(mCrime.getTitle());

            mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy", crimeT.getDate()));

           // if(crime.isSolved()){
           //     mSolvedImageView.setVisibility(View.VISIBLE);
          //  }else mSolvedImageView.setVisibility(View.GONE);

            if(crimeT.isSolved()) {
                mSolvedImageView.setVisibility(View.VISIBLE);
            } else mSolvedImageView.setVisibility(View.GONE);
           // mSolvedImageView.setVisibility(crimeT.isSolved() ? View.VISIBLE : View.GONE); //решение универсальное
        }

        private CrimeHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_crime, parent, false));


            itemView.setOnClickListener(this);

            mTitleTextView =(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        @Override
        public void onClick(View view){
            //Intent intent = new Intent(getActivity(), CrimeActivity.class);
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());

            mLastClikedPosition = this.getAdapterPosition();
            startActivityForResult(intent, REQUEST_CRIME);

            //Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Simple!", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CRIME){

        }

    }



    private class PoliceHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mCallPolice;
        private ImageView mSolvedImageP;

        private Crime mCrime;

        private void bind(Crime crimeP){
            mCrime = crimeP;
            mTitleTextView.setText(mCrime.getTitle());
            //mDateTextView.setText(mCrime.getDate().toString());
            mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy", crimeP.getDate()));

            //mSolvedImageView.setVisibility(crimeP.isSolved() ? View.VISIBLE : View.GONE); //решение универсальное
           // mSolvedImageView.setVisibility(View.VISIBLE);
            if(crimeP.isSolved()) {
                mSolvedImageP.setVisibility(View.VISIBLE);
            } else mSolvedImageP.setVisibility(View.GONE);


            //сюда добавить значение "requiredPolice", которое берется из CrimeFragment(из него же и задается) и обновляется при изменении
        }

        private PoliceHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_police, parent, false));
            //вот это тоже можно перенести в if и использовать аргумент int isRequiredPolice, чтобы все в одном классе было

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID());
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());
                    startActivityForResult(intent, REQUEST_CRIME);
                    //Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Police!", Toast.LENGTH_LONG).show();
                }
            });


            mCallPolice = itemView.findViewById(R.id.button);

            mCallPolice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), mCrime.getTitle()+"Police called!", Toast.LENGTH_LONG).show();
                }
            });

            mTitleTextView=(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
            mSolvedImageP = (ImageView) itemView.findViewById(R.id.crime_solved);

        }

      /*  @Override
        public void onClick(View view){  //можно реализовать через if isPoliseRequired, в одном классе
            Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Police!", Toast.LENGTH_LONG).show();
        }*/

    }




    public class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime>mCrimes;

        public CrimeAdapter(List<Crime>crimes){
            mCrimes = crimes;
        }


        @Override
        public int getItemViewType(int position){

            Crime crime = mCrimes.get(position); // тут может как-то по-дргуому можно, хз

            if(crime.isRequiredPolice()){
               return 1;
            } else return 0;

           // return crime.isRequiredPolice() ? 1 : 0;
        }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());


           if(viewType == 1){
                return new PoliceHolder(layoutInflater, parent);
            }else
                return new CrimeHolder(layoutInflater, parent);

            //свитч почему-то не возвращает значение. Надо исправить.



        }

           // return new CrimeHolder(layoutInflater, parent, viewType);  ////

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {    //тут был CrimeHolder вместо КRecycler

            Crime crime = mCrimes.get(position);


            // crime.isRequiredPolice(); вот это выражение помогает сделать без getItemViewType

            /*if (holder.getItemViewType()==1){
                PoliceHolder policeHolder = (PoliceHolder)holder;
                policeHolder.bind(crime);

            }else {
                CrimeHolder crimeHolder = (CrimeHolder)holder;
                crimeHolder.bind(crime);
            }*/

            //тоже свитч не реализуется

            switch (holder.getItemViewType()){
                case 0:
                    CrimeHolder crimeHolder = (CrimeHolder)holder;

                    crimeHolder.bind(crime);  ///не факт
                    break;

                case 1:
                    PoliceHolder policeHolder = (PoliceHolder)holder;
                    policeHolder.bind(crime);
                    break;
            }

         //   Crime crime = mCrimes.get(position);
          //  holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}