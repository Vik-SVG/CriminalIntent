package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        long date = getLong(getColumnIndex(Cols.DATE));
        long time = getLong(getColumnIndex(Cols.TIME));
        int isSolved = getInt(getColumnIndex(Cols.SOLVED));
        String suspect =  getString(getColumnIndex(Cols.SUSPECT));
        int requiredPolice= getInt(getColumnIndex(Cols.REQUIRED_POLICE));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setTime(new Date(time));
        crime.setSolved(isSolved !=0 );
        crime.setRequiredPolice(requiredPolice!=0);
        crime.setSuspect(suspect);
        return crime;
    }
}
