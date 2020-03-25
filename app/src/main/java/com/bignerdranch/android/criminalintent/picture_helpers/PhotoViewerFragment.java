package com.bignerdranch.android.criminalintent.picture_helpers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bignerdranch.android.criminalintent.R;

import java.io.File;

public class PhotoViewerFragment extends DialogFragment {

        private static final String ARG_PHOTO_FILE = "photoFile";

        private ImageView mPhotoView;
        private File mPhotoFile;

        public static PhotoViewerFragment newInstance(File photoFile) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_PHOTO_FILE, photoFile);

            PhotoViewerFragment fragment = new PhotoViewerFragment();
            fragment.setArguments(args);

            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

            View view = inflater.inflate(R.layout.dialog_photo, container, false);

            mPhotoView = (ImageView) view.findViewById(R.id.photo_view_dialog);

            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                mPhotoView.setImageBitmap(bitmap);
            }

            return view;
        }

   /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // String imageFile = (String) getArguments().getSerializable(ARG_PHOTO_FILE);

        mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        mPhotoView = (ImageView) v.findViewById(R.id.photo_view_dialog);
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }


        return new AlertDialog.Builder(getActivity(), R.style.ThemeOverlay_AppCompat_Dialog)
            .setView(v)
            .create();


      *//*  return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();*//*
    }*/


   /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //use this to modify dialog characteristics
        Log.d("CriminalIntent", "CrimePhotoDialogFragment.onCreateDialog()");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        return dialog;
    }*/
    }