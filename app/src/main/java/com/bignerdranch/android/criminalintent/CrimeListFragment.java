package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);

    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;

        private Crime mCrime;

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_crime, parent, false));


            itemView.setOnClickListener(this);

            mTitleTextView =(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Simple!", Toast.LENGTH_LONG).show();
        }

    }



    private class PoliceHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mCallPolice;

        private Crime mCrime;

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            //сюда добавить значение "requiredPolice", которое берется из CrimeFragment(из него же и задается) и обновляется при изменении
        }

        public PoliceHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_police, parent, false));
            //вот это тоже можно перенести в if и использовать аргумент int isRequiredPolice, чтобы все в одном классе было

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Police!", Toast.LENGTH_LONG).show();
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

        }

      /*  @Override
        public void onClick(View view){  //можно реализовать через if isPoliseRequired, в одном классе
            Toast.makeText(getActivity(), mCrime.getTitle()+" clicked Police!", Toast.LENGTH_LONG).show();
        }*/

    }




    public class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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
        }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if(viewType == 0){
                return new CrimeHolder(layoutInflater, parent);
            }else
                return new PoliceHolder(layoutInflater, parent);


            //свитч почему-то не возвращает значение. Надо исправить.
            /* switch (viewType) {
                case 0:
                    return new CrimeHolder(layoutInflater, parent);
                case 2:
                    return new PoliceHolder(layoutInflater, parent);
            } */
        }

           // return new CrimeHolder(layoutInflater, parent, viewType);  ////

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {    //тут был CrimeHolder вместо КRecycler

            Crime crime = mCrimes.get(position);


            // crime.isRequiredPolice(); вот это выражение помогает сделать без getItemViewType

            if (holder.getItemViewType()==0){

                CrimeHolder crimeHolder = (CrimeHolder)holder;
                crimeHolder.bind(crime);

            }else {
                PoliceHolder policeHolder = (PoliceHolder)holder;
                policeHolder.bind(crime);
            }

            //тоже свитч не реализуется

          /*  switch (holder.getItemViewType()){
                case 0:
                    CrimeHolder crimeHolder = (CrimeHolder)holder;

                    crimeHolder.bind(crime);  ///не факт
                    break;

                case 2:
                    PoliceHolder policeHolder = (PoliceHolder)holder;
                    policeHolder.bind(crime);
                    break;
            } */

         //   Crime crime = mCrimes.get(position);
          //  holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}