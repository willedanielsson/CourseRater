package com.example.william.courserater;


import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CourseFragment extends Fragment{

    private static boolean isCountrySet;
    private static boolean isUniversitySet;
    private static boolean isCourseSet;

    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    private EditText countryEditText;
    private ListView countryListView;

    private EditText universityEditText;
    private ListView universityListView;

    private ListView courseListView;
    private EditText courseEditText;

    private ArrayList<String> countryArrayList = new ArrayList<String>();
    //private static final String COUNTRYLISTKEY = "countryListLabels";

    private ArrayList<String> universityArrayList = new ArrayList<String>();
    //private static final String UNILISTKEY = "uniListLabels";

    private ArrayList<String> courseArrayList = new ArrayList<String>();
    //private static final String MYOTHERLISTKEY = "courseListLabels";

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.course,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_course:
                addNewCourse();
        }
        return super.onOptionsItemSelected(item);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        setIsCountrySet(false);
        setIsUniversitySet(false);
        setIsCourseSet(false);


        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countryArrayList);
        countryEditText = (EditText) rootView.findViewById(R.id.country_edit_text);
        countryListView = (ListView) rootView.findViewById(R.id.country_list_view);
        countryListView.setVisibility(View.GONE);


        /*Initialisation of University related*/
        final ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universityArrayList);
        universityEditText = (EditText) rootView.findViewById(R.id.university_edit_text);
        universityListView = (ListView) rootView.findViewById(R.id.university_list_view);
        universityListView.setVisibility(View.GONE);

        /*Initialisation of Course related*/
        final ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, courseArrayList);
        courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);
        courseListView = (ListView) rootView.findViewById(R.id.course_list_view);
        courseListView.setVisibility(View.GONE);

        final Button viewCourseInformationButton = (Button) rootView.findViewById(R.id.viewCourseInformationButton);
        viewCourseInformationButton.setBackgroundResource(R.drawable.button_shape);
        GradientDrawable drawable = (GradientDrawable) viewCourseInformationButton.getBackground();
        drawable.setColor(getResources().getColor(R.color.greyish));

        viewCourseInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUniversity = "Linköpings tekniska högskola";
                String selectedCourse = "TANA21";
                countryEditText.setText("");
                universityEditText.setText("");
                courseEditText.setText("");
                ((Main)getActivity()).startNewCourseFragment(selectedUniversity, selectedCourse);

            /* DEVELOPMENT

              boolean localIsCountrySet = getIsCountrySet();
                boolean localIsUniversitySet = getIsUniversitySet();
                boolean localIsCourseSet = getIsCourseSet();
                if(localIsCountrySet==true && localIsUniversitySet==true && localIsCourseSet==true){
                    String selectedUniversity = courseEditText.getText().toString();
                    String selectedCourse = courseEditText.getText().toString();

                    countryEditText.setText("");
                    universityEditText.setText("");
                    courseEditText.setText("");

                    ((Main)getActivity()).startNewCourseFragment(selectedUniversity, selectedCourse);
                }else if(localIsCountrySet == false){
                    Toast.makeText(getActivity(), "Choose country from list", Toast.LENGTH_LONG).show();
                }else if(localIsUniversitySet == false){
                    Toast.makeText(getActivity(), "Choose university from list", Toast.LENGTH_LONG).show();
                }else if(localIsCourseSet == false){
                    Toast.makeText(getActivity(), "Choose course from list", Toast.LENGTH_LONG).show();
                }
            */


            }
        });

        if(!countryArrayList.isEmpty()){
            //The list is populated and therefore we dont need to add items to it
        }else {
            try {
                HttpClient.get("getCountries.php", null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                        try {
                            for (int i = 0; i < json.length(); i++) {
                                String countryName = json.get(i).toString();
                                countryArrayList.add(countryName);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    // Do something with the response
                });
            } catch (Exception e) {
                Log.e("CountryHTTP", e.toString());
            }
        }
        countryListView.setAdapter(countryAdapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedCountry = (String) adapterView.getItemAtPosition(position);
                countryEditText.setText(clickedCountry);
                setIsCountrySet(true);
                if(!universityArrayList.isEmpty()){
                    // The list is populated, dont do anything
                }else {
                    dataBaseHandler.getUniversityForChosenCountry(clickedCountry, universityAdapter, universityArrayList, universityListView);
                }
                universityListView.setAdapter(universityAdapter);
                countryListView.setVisibility(View.GONE);
            }

        });

        countryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                // Show and Hide list if texts is empty
                countryAdapter.getFilter().filter(charSequence);
                if(charSequence.length()==0){
                    countryListView.setVisibility(View.GONE);
                }else{
                    countryListView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        universityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedUniversity = (String)adapterView.getItemAtPosition(position);
                universityEditText.setText(clickedUniversity);
                setIsUniversitySet(true);
                if(!courseArrayList.isEmpty()){
                    // The list is populated, no need to get new ones
                }else{
                    dataBaseHandler.getCoursesForChosenUniversity(clickedUniversity, courseAdapter, courseArrayList, courseListView);
                }
                courseListView.setAdapter(courseAdapter);
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
                setIsCourseSet(true);

                boolean localIsCountrySet = getIsCountrySet();
                boolean localIsUniversitySet = getIsUniversitySet();
                boolean localIsCourseSet = getIsCourseSet();
                if(localIsCountrySet==true && localIsUniversitySet==true && localIsCourseSet==true){

                    viewCourseInformationButton.setBackgroundResource(R.drawable.button_shape);
                    GradientDrawable drawable = (GradientDrawable) viewCourseInformationButton.getBackground();
                    drawable.setColor(getResources().getColor(R.color.greenish));
                }
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

    private void addNewCourse() {
        // If country is empty, clear all
        if(countryEditText.getText().equals(null)){
            ((Main)getActivity()).createAddCourseFragment(null ,null,countryArrayList, universityArrayList);
            countryEditText.setText("");
            universityEditText.setText("");
            courseEditText.setText("");
        }
        // Country is taken but not the university
        else if(countryEditText.getText().equals(null) && universityEditText.getText()==null){
            ((Main)getActivity()).createAddCourseFragment(countryEditText.getText().toString() ,null, countryArrayList, universityArrayList);
            countryEditText.setText("");
            universityEditText.setText("");
            courseEditText.setText("");

        }else{
            ((Main)getActivity()).createAddCourseFragment(countryEditText.getText().toString(), universityEditText.getText().toString(), countryArrayList, universityArrayList);
            countryEditText.setText("");
            universityEditText.setText("");
            courseEditText.setText("");
        }
    }

    public static boolean getIsCountrySet() {
        return isCountrySet;
    }

    public static void setIsCountrySet(boolean isCountrySet) {
        CourseFragment.isCountrySet = isCountrySet;
    }

    public static boolean getIsUniversitySet() {
        return isUniversitySet;
    }

    public static void setIsUniversitySet(boolean isUniversitySet) {
        CourseFragment.isUniversitySet = isUniversitySet;
    }

    public static boolean getIsCourseSet() {
        return isCourseSet;
    }

    public static void setIsCourseSet(boolean isCourseSet) {
        CourseFragment.isCourseSet = isCourseSet;
    }

}