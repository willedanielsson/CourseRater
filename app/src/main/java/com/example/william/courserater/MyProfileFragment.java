package com.example.william.courserater;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyProfileFragment extends Fragment {


    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();

        return fragment;
    }
    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


}
