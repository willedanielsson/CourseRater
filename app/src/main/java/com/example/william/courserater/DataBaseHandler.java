package com.example.william.courserater;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by William on 2014-11-08.
 */
public class DataBaseHandler {

    public DataBaseHandler(){

    }

    public void getUniversityForChosenCountry (String clickedCountry, final ArrayAdapter<String> universityAdapter, final ArrayList<String> universityArrayList, final ListView universityListView) {

        RequestParams countryParams = new RequestParams();
        countryParams.put("country", clickedCountry);

        try {
            HttpClient.get("getUniversitiesForCountry.php", countryParams, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    try{
                        for(int i = 0; i<json.length(); i++){
                            String universityName = json.get(i).toString();
                            universityArrayList.add(universityName);
                        }
                    }catch (JSONException e){
                        Log.e("JSONEXEPT", e.toString());
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String string, Throwable e){
                    Log.e("OnFailure", e.toString());

                }
            });
        }catch (Exception e){
            Log.e("HTTPUNI", e.toString());
        }
    //return universityArrayList;
    }

    public void getCoursesForChosenUniversity(String chosenUniversity, final ArrayAdapter courseAdapter, final ArrayList<String> courseArrayList, final ListView courseListView) {
        //courseListView.setAdapter(courseAdapter);

        RequestParams params = new RequestParams();
        params.put("chosenUniversity", chosenUniversity);

        try {
            HttpClient.get("getCoursesForUniversity.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String courseName = json.get(i).toString();
                            courseArrayList.add(courseName);
                        }
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


    public void uploadCourseData(final Activity activity, String university, String courseCode) {

        RequestParams params = new RequestParams();
        params.put("university", university);
        params.put("courseCode", courseCode);

        HttpClient.post("addCourse.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Toast.makeText(activity, "Added course to database", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String string, Throwable e){
                Toast.makeText(activity,"Does not work",Toast.LENGTH_LONG).show();
                Log.e("FELET", e.toString());
            }
        });
    }
}
