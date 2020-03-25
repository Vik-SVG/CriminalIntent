package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    public static CrimeLab sCrimeLab;

//    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();

        /*for(int i = 0; i<100;i++){
         Crime crime = new Crime();
         crime.setTitle("crime #"+i);
            crime.setSolved(i%4==0);
            crime.setRequiredPolice(i%3==0);

         mCrimes.add(crime);
        }*/
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getID().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.TIME, crime.getTime().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(Cols.REQUIRED_POLICE, crime.isRequiredPolice() ? 1 : 0);
        values.put(Cols.SUSPECT, crime.getSuspect());
        return values;
    }

    public void addCrime(Crime c){
//        mCrimes.add(c);

        ContentValues values = getContentValues(c);
        mDataBase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    public List<Crime>getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){

        /*for(Crime crime: mCrimes){
            if(crime.getID().equals(id)){
                return crime;
            }
        }*/

        CrimeCursorWrapper cursor = queryCrimes(
                Cols.UUID + " = ?",
                new String[]{ id.toString()}
        );

        try {
            if(cursor.getCount() ==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }


    }

    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFileName());
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getID().toString();
        ContentValues values = getContentValues(crime);

        mDataBase.update(CrimeDbSchema.CrimeTable.NAME, values,
                Cols.UUID + "= ?",
                new String[]{uuidString});
    }

//    private Cursor queryCrimes (String whereClause, String[] whereArgs){
        private CrimeCursorWrapper queryCrimes(String whereClause, String[]whereArgs){
        Cursor cursor = mDataBase.query(CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }


    public void deleteCrime(UUID crimeID){
//        Crime crime = getCrime(crimeID);
        mDataBase.delete(CrimeDbSchema.CrimeTable.NAME, Cols.UUID + "=?", new String[]{crimeID.toString()});
//        mCrimes.remove(crime);

    }

}