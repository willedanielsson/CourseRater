package com.example.william.courserater;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class CourseInformationFragment extends Fragment {


    public CourseInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_information, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<String> informationArrayList = new ArrayList<String>();
        final ArrayAdapter<String> informationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, informationArrayList);
        final ListView informationListView = (ListView) rootView.findViewById(R.id.courseInformation_list_view);


        String courseName = getArguments().getString("courseName");
        TextView textView = (TextView)rootView.findViewById(R.id.textViewTest);
        textView.setText(courseName);

        RequestParams params = new RequestParams();
        params.put("courseName", courseName);

        try{
            HttpClient.get("getCourseInformation.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String courseInformation = json.get(i).toString();
                            System.out.println(courseInformation);
                            //informationArrayList.add(courseInformation);
                        }
                        //informationListView.setAdapter(informationAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String string, Throwable e){
                    Log.e("OnFailure", e.toString());

                }
            });
        }catch (Exception e){
            Log.e("HTTPcourseInf", e.toString());
        }

        return rootView;
    }

    public static CourseInformationFragment newInstance(String courseName) {
        CourseInformationFragment courseInformationFragment = new CourseInformationFragment();

        Bundle args = new Bundle();
        args.putString("courseName", courseName);
        courseInformationFragment.setArguments(args);
        return courseInformationFragment;

    }
}