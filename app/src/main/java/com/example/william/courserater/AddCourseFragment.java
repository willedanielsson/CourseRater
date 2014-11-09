package com.example.william.courserater;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddCourseFragment extends Fragment {
    public static boolean getIsCountrySet() {
        return isCountrySet;
    }

    public static void setIsCountrySet(boolean isCountrySet) {
        AddCourseFragment.isCountrySet = isCountrySet;
    }

    public static boolean getIsUniversitySet() {
        return isUniversitySet;
    }

    public static void setIsUniversitySet(boolean isUniversitySet) {
        AddCourseFragment.isUniversitySet = isUniversitySet;
    }

    private static boolean isCountrySet;
    private static boolean isUniversitySet;

    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    private EditText countryEditText;
    private ListView countryListView;
    private ArrayList<String> countryArrayList = new ArrayList<String>();

    private EditText universityEditText;
    private ListView universityListView;
    private ArrayList<String> universityArrayList = new ArrayList<String>();

    private EditText courseEditText;

    private Button addCourseButton;

    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setIsCountrySet(false);
        setIsUniversitySet(false);

        final View rootView =  inflater.inflate(R.layout.fragment_add_course, container, false);

        countryEditText = (EditText) rootView.findViewById(R.id.country_edit_text);
        if(!getArguments().getString("country").isEmpty()){
            countryEditText.setText(getArguments().getString("country"));
        }

        countryListView = (ListView) rootView.findViewById(R.id.country_list_view);
        countryListView.setVisibility(View.GONE);
        countryArrayList = getArguments().getStringArrayList("countryList");
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countryArrayList);
        countryListView.setAdapter(countryAdapter);


        universityEditText = (EditText) rootView.findViewById(R.id.university_edit_text);
        if(!getArguments().getString("university").isEmpty()){
            universityEditText.setText(getArguments().getString("university"));
        }
        universityListView = (ListView) rootView.findViewById(R.id.university_list_view);
        universityListView.setVisibility(View.GONE);
        if(!getArguments().getStringArrayList("universityList").isEmpty()){
            universityArrayList = getArguments().getStringArrayList("universityList");
        }
        final ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universityArrayList);

        courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);

        addCourseButton = (Button) rootView.findViewById(R.id.addCourseButton);
        addCourseButton.setBackgroundResource(R.drawable.button_shape);
        GradientDrawable drawable = (GradientDrawable) addCourseButton.getBackground();
        drawable.setColor(getResources().getColor(R.color.greyish));

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedCountry = (String) adapterView.getItemAtPosition(position);
                countryEditText.setText(clickedCountry);

                if(getArguments().getStringArrayList("universityList").isEmpty()){
                    dataBaseHandler.getUniversityForChosenCountry(clickedCountry, universityAdapter, universityArrayList, universityListView);
                }
                universityArrayList = getArguments().getStringArrayList("universityList");
                universityListView.setAdapter(universityAdapter);
                setIsCountrySet(true);
                countryListView.setVisibility(View.GONE);
            }
        });

        countryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
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

    courseEditText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if(charSequence.length()>0 && getIsCountrySet()==true && getIsUniversitySet()==true){
                addCourseButton.setBackgroundResource(R.drawable.button_shape);
                GradientDrawable drawable = (GradientDrawable) addCourseButton.getBackground();
                drawable.setColor(getResources().getColor(R.color.greenish));
            }

        }
        @Override
        public void afterTextChanged(Editable editable) {}
    });

    addCourseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String university = universityEditText.getText().toString();
            String courseCode = courseEditText.getText().toString();
            boolean localIsCountrySet = getIsCountrySet();
            boolean localIsUniversitySet = getIsUniversitySet();

            if(localIsCountrySet==true && localIsUniversitySet==true && courseCode.length()!=0 ) {
                dataBaseHandler.uploadCourseData(getActivity(), university, courseCode);
            }else if(localIsCountrySet==false){
                Toast.makeText(getActivity(), "Choose country from list", Toast.LENGTH_LONG).show();
            }else if(localIsUniversitySet==false){
                Toast.makeText(getActivity(), "Choose university from list", Toast.LENGTH_LONG).show();
            }else if(courseCode.length()!=0){
                Toast.makeText(getActivity(), "Coursecode need to be defined", Toast.LENGTH_LONG).show();
            }


        }
    });

    return rootView;

    }


    public static AddCourseFragment newInstance(String country, String university, ArrayList<String> countryArrayList, ArrayList<String> universityArrayList) {
        AddCourseFragment addCourseFragment = new AddCourseFragment();
        if(university!=null){
            Bundle args = new Bundle();

            args.putString("country", country);
            args.putString("university", university);
            args.putStringArrayList("universityList", universityArrayList);
            args.putStringArrayList("countryList", countryArrayList);

            addCourseFragment.setArguments(args);
        }

        return addCourseFragment;
    }

}
