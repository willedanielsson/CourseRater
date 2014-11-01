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
import android.widget.Adapter;
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

    private ListView informationListView;

    public CourseInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_information, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String courseName = getArguments().getString("courseName");

        final ArrayList<Item> testArrayList = new ArrayList<Item>();
        final MyAdapter adapter = new MyAdapter(rootView.getContext(), testArrayList);
        //final ArrayAdapter<String> informationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, informationPartsArrayList);
        informationListView = (ListView) rootView.findViewById(R.id.courseInformation_list_view);
        //informationListView.setAdapter(informationAdapter);

        final ArrayList<String> informationRatingArrayList = new ArrayList<String>();
        RequestParams params = new RequestParams();
        params.put("courseName", courseName);

        try{
            HttpClient.get("getCourseInformation.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    try {
                        JSONArray ratingsArray = (JSONArray) json.get("Ratings");
                        JSONObject test = ratingsArray.getJSONObject(0);
                        Iterator<?> keys = test.keys();
                        while ( keys.hasNext()){
                            String key = (String)keys.next();
                            String value = test.getString(key);
                            System.out.println(key);
                            informationRatingArrayList.add(value);
                        }
                        createInformationList(informationRatingArrayList, testArrayList, adapter);
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

    private void createInformationList(ArrayList<String> informationRatingArrayList, ArrayList<Item> testArrayList, ArrayAdapter adapter) {
        testArrayList.add(new Item("Usefulness",informationRatingArrayList.get(3)));
        testArrayList.add(new Item("Exam",informationRatingArrayList.get(5)));
        testArrayList.add(new Item("Lectures",informationRatingArrayList.get(6)));
        testArrayList.add(new Item("Lessons", informationRatingArrayList.get(0)));
        testArrayList.add(new Item("Labaratory",informationRatingArrayList.get(2)));
        testArrayList.add(new Item("Seminar",informationRatingArrayList.get(8)));
        testArrayList.add(new Item("Project",informationRatingArrayList.get(7)));
        testArrayList.add(new Item("Homeassignment",informationRatingArrayList.get(1)));
        testArrayList.add(new Item("Case",informationRatingArrayList.get(4)));


        informationListView.setAdapter(adapter);
    }

    public static CourseInformationFragment newInstance(String courseName) {
        CourseInformationFragment courseInformationFragment = new CourseInformationFragment();

        Bundle args = new Bundle();
        args.putString("courseName", courseName);
        courseInformationFragment.setArguments(args);
        return courseInformationFragment;

    }
}