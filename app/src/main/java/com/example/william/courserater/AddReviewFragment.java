package com.example.william.courserater;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class AddReviewFragment extends Fragment {


    public AddReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_add_review, container, false);


        return rootView;
    }

    public static AddReviewFragment newInstance(String universityName, String courseName) {
       AddReviewFragment addReviewFragment = new AddReviewFragment();

        Bundle args = new Bundle();
        args.putString("universityName", universityName);
        args.putString("courseName", courseName);
        addReviewFragment.setArguments(args);
        return addReviewFragment;

    }


}
