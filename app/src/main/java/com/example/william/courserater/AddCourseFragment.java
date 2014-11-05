package com.example.william.courserater;

import android.app.Fragment;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class AddCourseFragment extends Fragment {
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
        final View rootView =  inflater.inflate(R.layout.fragment_add_course, container, false);



        courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);

        addCourseButton = (Button) rootView.findViewById(R.id.addCourseButton);

        countryEditText = (EditText) rootView.findViewById(R.id.country_edit_text);
        countryListView = (ListView) rootView.findViewById(R.id.country_list_view);

        countryListView.setVisibility(View.GONE);
        countryArrayList = getArguments().getStringArrayList("countryList");
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countryArrayList);
        countryListView.setAdapter(countryAdapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedCountry = (String) adapterView.getItemAtPosition(position);
                countryEditText.setText(clickedCountry);
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

        universityEditText = (EditText) rootView.findViewById(R.id.university_edit_text);
        universityListView = (ListView) rootView.findViewById(R.id.university_list_view);

        universityListView.setVisibility(View.GONE);
        universityArrayList = getArguments().getStringArrayList("universityList");
        final ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universityArrayList);
        universityListView.setAdapter(universityAdapter);

        universityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            String clickedUniversity = (String)adapterView.getItemAtPosition(position);
            universityEditText.setText(clickedUniversity);

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

    addCourseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String university = universityEditText.getText().toString();
            String courseCode = courseEditText.getText().toString();

            if(university.length()!=0 && courseCode.length()!=0 ) {
                uploadCourseData(university, courseCode);
            }

        }
    });

    return rootView;

    }

    private void uploadCourseData(String university, String courseCode) {

        RequestParams params = new RequestParams();
        params.put("university", university);
        params.put("courseCode", courseCode);
        /*
         * TODO: HAndle so that the user can only add a course if the university exist
         */
        HttpClient.post("addCourse.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Toast.makeText(getActivity(),"Added course to database",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String string, Throwable e){
                Toast.makeText(getActivity(),"Added course to database",Toast.LENGTH_LONG).show();
            }
        });
    }


    public static AddCourseFragment newInstance(String university, ArrayList<String> countryArrayList, ArrayList<String> universityArrayList) {
        AddCourseFragment addCourseFragment = new AddCourseFragment();
        if(university!=null){
            Bundle args = new Bundle();

            args.putStringArrayList("universityList", universityArrayList);
            args.putStringArrayList("countryList", countryArrayList);
            args.putString("university", university);
            addCourseFragment.setArguments(args);
        }

        return addCourseFragment;
    }

}
