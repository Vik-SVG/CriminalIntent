package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {


    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private boolean mRequiredPolice;
    private String mSuspectPhone;


    public Crime(){
        this(UUID.randomUUID());
       /* mID = UUID.randomUUID();
        mDate = new Date();*/
    }

    public Crime(UUID id){
        mID = id;
        mDate = new Date();

    }


    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public String getSuspect(){
        return mSuspect;
    }

    public void setSuspect(String suspect){
        mSuspect = suspect;
    }

    public String getSuspectPhone() { return mSuspectPhone; }

    public void setSuspectPhone(String mSuspectPhone) { this.mSuspectPhone = mSuspectPhone; }

    public String getPhotoFileName(){
        return "IMG_" + getID().toString()+".jpg";
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiredPolice() {
        return mRequiredPolice;
    }

    public void setRequiredPolice(boolean requiredPolice) {
        mRequiredPolice = requiredPolice;
    }
}
