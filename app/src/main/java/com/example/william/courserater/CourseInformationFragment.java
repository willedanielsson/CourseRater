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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * TODO: Need a more clean design of the headline of the current course
 */
public class CourseInformationFragment extends Fragment {

    private ListView informationListView;
    private TextView courseNameTextView;
    private TextView courseAverageRatingTextView;
    public final Float sumOfValues= 0.0f;
    private boolean initRun = true;

    public CourseInformationFragment() {
        // Required empty public constructor
    }
/*
 * TODO: Add comments below the ratings (prob in a list)
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_information, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String courseName = getArguments().getString("courseName");

        courseNameTextView = (TextView) rootView.findViewById(R.id.courseName_text_view);
        courseNameTextView.setText(courseName);

        courseAverageRatingTextView = (TextView) rootView.findViewById(R.id.course_average_rating);


        /*
         * List of parts of the course with their values from database
         */
        final ArrayList<Item> informationArrayList = new ArrayList<Item>();
        final MyAdapter adapter = new MyAdapter(rootView.getContext(), informationArrayList);
        informationListView = (ListView) rootView.findViewById(R.id.courseInformation_list_view);
        final ArrayList<Float> informationRatingArrayList = new ArrayList<Float>();
        final ArrayList<Text> informatinoCommentArrayList = new ArrayList<Text>();

        RequestParams params = new RequestParams();
        params.put("courseName", courseName);

        try{
            HttpClient.get("getCourseInformation.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    try {
                        JSONArray ratingsArray = (JSONArray) json.get("Ratings");
                        JSONObject JSONObject = ratingsArray.getJSONObject(0);
                        Iterator<?> keys = JSONObject.keys();
                        while ( keys.hasNext()){
                            String key = (String)keys.next();
                            System.out.println(key);
                            Float value = Float.parseFloat(JSONObject.getString(key));
                            informationRatingArrayList.add(value);
                        }


                        createInformationList(informationRatingArrayList, informationArrayList, adapter, sumOfValues);
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


    private void createInformationList(ArrayList<Float> informationRatingArrayList, ArrayList<Item> informationArrayList, ArrayAdapter adapter, Float sumOfValues) {
        if(informationRatingArrayList.get(3)!=0){
            informationArrayList.add(new Item("Usefulness", informationRatingArrayList.get(3)));
             //getSum(informationRatingArrayList.get(3));
            sumOfValues = sumOfValues+informationRatingArrayList.get(3);
        }
        if(informationRatingArrayList.get(7)!=0){
            informationArrayList.add(new Item("Difficulty", informationRatingArrayList.get(7)));
            // This values should not inflict the average points of the course
        }
        if(informationRatingArrayList.get(5)!=0){
            informationArrayList.add(new Item("Exam", informationRatingArrayList.get(5)));
            //getSum(informationRatingArrayList.get(5));
            sumOfValues = sumOfValues+informationRatingArrayList.get(5);
        }
        if(informationRatingArrayList.get(6)!=0){
            informationArrayList.add(new Item("Lectures", informationRatingArrayList.get(6)));
            //getSum(informationRatingArrayList.get(6));
            sumOfValues = sumOfValues+informationRatingArrayList.get(6);
        }
        if(informationRatingArrayList.get(0)!=0){
            informationArrayList.add(new Item("Lessons", informationRatingArrayList.get(0)));
            //getSum(informationRatingArrayList.get(0));
            sumOfValues = sumOfValues+informationRatingArrayList.get(0);
        }
        if(informationRatingArrayList.get(2)!=0){
            informationArrayList.add(new Item("Labaratory", informationRatingArrayList.get(2)));
            //getSum(informationRatingArrayList.get(2));
            sumOfValues = sumOfValues+informationRatingArrayList.get(2);
        }
        if(informationRatingArrayList.get(9)!=0){
            informationArrayList.add(new Item("Seminar", informationRatingArrayList.get(8)));
            //getSum(informationRatingArrayList.get(8));
            sumOfValues = sumOfValues+informationRatingArrayList.get(8);
        }
        if(informationRatingArrayList.get(8)!=0){
            informationArrayList.add(new Item("Project", informationRatingArrayList.get(7)));
            //getSum(informationRatingArrayList.get(7));
            sumOfValues = sumOfValues+informationRatingArrayList.get(7);
        }
        if(informationRatingArrayList.get(1)!=0){
            informationArrayList.add(new Item("Homeassignment", informationRatingArrayList.get(1)));
            //getSum(informationRatingArrayList.get(1));
            sumOfValues = sumOfValues+informationRatingArrayList.get(1);
        }
        if(informationRatingArrayList.get(4)!=0){
            informationArrayList.add(new Item("Case", informationRatingArrayList.get(4)));
            //getSum(informationRatingArrayList.get(4));
            sumOfValues = sumOfValues+informationRatingArrayList.get(4);
        }
        Float averageScore;
        averageScore = sumOfValues/informationArrayList.size();
        double result = (double) Math.round(averageScore*10)/10;
        String resultText = String.valueOf(result);
        if(initRun==true){
            courseAverageRatingTextView.setText(resultText);
            initRun=false;
        }

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