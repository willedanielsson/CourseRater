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


public class AddCourseFragment extends Fragment {

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

        addCourseButton = (Button) rootView.findViewById(R.id.addCourseButton);

        courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);

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
                System.out.println("Jaa");
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


    public static AddCourseFragment newInstance(String university, ArrayList<String> arrayList) {
        AddCourseFragment addCourseFragment = new AddCourseFragment();
        if(university!=null){
            Bundle args = new Bundle();

            args.putStringArrayList("universityList", arrayList);
            args.putString("university", university);
            addCourseFragment.setArguments(args);
        }

        return addCourseFragment;
    }

}
