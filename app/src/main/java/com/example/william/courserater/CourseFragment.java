package com.example.william.courserater;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CourseFragment extends Fragment{

    public CourseFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final EditText universityEditText;
        final ListView universityListView;

        final ListView courseListView;
        final EditText courseEditText;


        View rootView = inflater.inflate(R.layout.fragment_course, container, false);


        /*Initialisation of University related*/
        universityEditText = (EditText) rootView.findViewById(R.id.university_edit_text);
        final ArrayList<String> universityArrayList = new ArrayList<String>();
        final ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universityArrayList);
        universityListView = (ListView) rootView.findViewById(R.id.university_list_view);
        universityListView.setVisibility(View.GONE);

        /*Initialisation of Course related*/
        final ArrayList<String> courseArrayList = new ArrayList<String>();
        final ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, courseArrayList);
        courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);
        courseListView = (ListView) rootView.findViewById(R.id.course_list_view);
        courseListView.setVisibility(View.GONE);

        Button viewCourseInformationButton = (Button) rootView.findViewById(R.id.viewCourseInformationButton);
        viewCourseInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fragment newCourseInformationFragment = CourseInformationFragment.newInstance("TANA21");
                //newCourseInformationFragment =
                String selectedCourse = courseEditText.getText().toString();
                ((Main)getActivity()).startNewCourseFragment(selectedCourse);
            }
        });

        /*
         * University part
         */
        try{
            HttpClient.get("connect.php", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String universityName = json.get(i).toString();
                            universityArrayList.add(universityName);
                        }
                        universityListView.setAdapter(universityAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                // Do something with the response
            });
        }catch (Exception e){
            Log.e("UniversityHTTP", e.toString());
        }

        universityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedUniversity = (String)adapterView.getItemAtPosition(position);
                universityEditText.setText(clickedUniversity);
                getCoursesForChosenUniversity(clickedUniversity, courseAdapter, courseArrayList, courseListView);

                universityListView.setVisibility(View.GONE);

            }
        });

        universityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                universityAdapter.getFilter().filter(charSequence);
                if(charSequence.length()==0){
                    universityListView.setVisibility(View.GONE);
                }else{
                    universityListView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        /*
         * Course part
         */

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedCourse = (String)adapterView.getItemAtPosition(position);
                courseEditText.setText(clickedCourse);
                courseListView.setVisibility(View.GONE);
            }
        });

        courseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                courseAdapter.getFilter().filter(charSequence);
                if(charSequence.length()==0){
                    courseListView.setVisibility(View.GONE);
                }else{
                    courseListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        return rootView;
    }

    public static CourseFragment newInstance() {
        CourseFragment courseFragment = new CourseFragment();
        return courseFragment;
    }


    /*
     *
     */
    public void getCoursesForChosenUniversity(String chosenUniversity, final ArrayAdapter courseAdapter, final ArrayList<String> courseArrayList, final ListView courseListView) {
        courseListView.setAdapter(courseAdapter);

        RequestParams params = new RequestParams();
        params.put("chosenUniversity", chosenUniversity);

        try {
            HttpClient.get("getCoursesForUniversity.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String universityName = json.get(i).toString();
                            courseArrayList.add(universityName);
                        }
                        courseListView.setAdapter(courseAdapter);

                    } catch (JSONException e) {
                        Log.e("JSONEXCPETION", e.toString());
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String string, Throwable e){
                   Log.e("OnFailure", e.toString());

                }
            });

        }catch (Exception e){
            Log.e("HTTPClient", e.toString());
        }

    }

}