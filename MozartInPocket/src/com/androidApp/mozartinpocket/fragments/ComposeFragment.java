package com.androidApp.mozartinpocket.fragments;

import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.util.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class ComposeFragment extends Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        if (savedInstanceState != null) {
        }
        Navigation.compose(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.compose_music_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
