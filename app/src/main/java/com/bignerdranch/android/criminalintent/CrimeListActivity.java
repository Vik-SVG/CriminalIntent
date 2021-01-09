package com.bignerdranch.android.criminalintent;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;



public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeletion(Crime crime){
        Fragment newKotlinFrag = new BlankFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_fragment_container, newKotlinFrag)
                .commit();

    }

    @Override
    public void onCrimeSelected(Crime crime){
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent = CrimePagerActivity.newIntent(this, crime.getID());
            startActivity(intent);
        } else{
            Fragment newDetail = CrimeFragment.newInstance(crime.getID());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();

        }
    }

}



