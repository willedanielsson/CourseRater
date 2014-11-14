package com.example.william.courserater;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * TODO: Need a more clean design of the headline of the current course
 */
public class CourseInformationFragment extends Fragment {

    private ListView informationListView;
    private TextView courseNameTextView;
    private TextView courseAverageRatingTextView;
    private ProgressBar progressBar;

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

        final String courseName = getArguments().getString("courseName");

        courseNameTextView = (TextView) rootView.findViewById(R.id.courseName_text_view);
        courseNameTextView.setText(courseName);

        courseAverageRatingTextView = (TextView) rootView.findViewById(R.id.course_average_rating);


        /*
         * List of parts of the course with their values from database
         */
        final ArrayList<ItemRating> courseParts = new ArrayList<ItemRating>();
        final ArrayList<Float> informationRatingArrayList = new ArrayList<Float>();
        final ArrayList<ArrayList<String>> groupList = new ArrayList<ArrayList<String>>();

        final AdapterRating adapter = new AdapterRating(rootView.getContext(), courseParts);

        informationListView = (ListView) rootView.findViewById(R.id.courseInformation_list_view);

        /*
         * Change the overscroll effect color to my green one
         */
        int glowDrawableId = rootView.getContext().getResources().getIdentifier("overscroll_glow","drawable", "android");
        Drawable androidGlow = rootView.getContext().getResources().getDrawable(glowDrawableId);
        androidGlow.setColorFilter(getResources().getColor(R.color.greenish), PorterDuff.Mode.MULTIPLY);

        int edgeDrawableId = rootView.getContext().getResources().getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidEdge = rootView.getContext().getResources().getDrawable(edgeDrawableId);
        androidEdge.setColorFilter(getResources().getColor(R.color.greenish), PorterDuff.Mode.MULTIPLY);

       InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);


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
                            Float value = Float.parseFloat(JSONObject.getString(key));
                            informationRatingArrayList.add(value);
                        }
                        getComments(groupList,json);

                        createInformationList(informationRatingArrayList, courseParts, adapter);

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
        
        informationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArrayList<String> clickedPartCommentList = new ArrayList<String>();
                ItemRating clickedItem = (ItemRating) adapterView.getItemAtPosition(position);

                String clickedPart = clickedItem.getLabel();

                if(clickedPart =="Exam"){
                    clickedPartCommentList = groupList.get(0);
                }else if(clickedPart == "Lectures"){
                    clickedPartCommentList = groupList.get(1);
                }else if(clickedPart == "Lessons"){
                    clickedPartCommentList = groupList.get(2);
                }else if(clickedPart == "Laboratory"){
                    clickedPartCommentList = groupList.get(3);
                }else if(clickedPart == "Seminar"){
                    clickedPartCommentList = groupList.get(4);
                }else if(clickedPart == "Project"){
                    clickedPartCommentList = groupList.get(5);
                }else if(clickedPart == "Homeassignment"){
                    clickedPartCommentList = groupList.get(6);
                }else if(clickedPart == "Case"){
                    clickedPartCommentList = groupList.get(7);
                }
                if(clickedPart == "Usefulness"){}
                else if(clickedPart == "Difficulty"){}
                else{
                    ((Main)getActivity()).createCoursePartFragment(courseName, clickedPart, clickedPartCommentList);
                }
            }
        });

        return rootView;
    }

    private void getComments(ArrayList<ArrayList<String>> groupList, JSONObject json) {
        try {
            ArrayList<String> examCommentsArrayList = new ArrayList<String>();
            JSONArray examCommentsArray = (JSONArray) json.get("examComments");
            for (int i = 0; i < examCommentsArray.length(); i++) {
                JSONObject object = examCommentsArray.getJSONObject(i);
                examCommentsArrayList.add(object.getString("examComment"));
            }
            groupList.add(examCommentsArrayList);

            ArrayList<String> lectureCommentsArrayList = new ArrayList<String>();
            JSONArray lectureCommentsArray = (JSONArray) json.get("lectureComments");
            for (int i = 0; i < lectureCommentsArray.length(); i++) {
                JSONObject object = lectureCommentsArray.getJSONObject(i);
                lectureCommentsArrayList.add(object.getString("lectureComment"));
            }
            groupList.add(lectureCommentsArrayList);

            ArrayList<String> lessonCommentsArrayList = new ArrayList<String>();
            JSONArray lessonCommentsArray = (JSONArray) json.get("lessonComments");
            for (int i = 0; i < lessonCommentsArray.length(); i++) {
                JSONObject object = lessonCommentsArray.getJSONObject(i);
                lessonCommentsArrayList.add(object.getString("lessonComment"));
            }
            groupList.add(lessonCommentsArrayList);

            ArrayList<String> laboratoryCommentsArrayList = new ArrayList<String>();
            JSONArray laboratoryCommentsArray = (JSONArray) json.get("laboratoryComments");
            for (int i = 0; i < laboratoryCommentsArray.length(); i++) {
                JSONObject object = laboratoryCommentsArray.getJSONObject(i);
                laboratoryCommentsArrayList.add(object.getString("laboratoryComment"));
            }
            groupList.add(laboratoryCommentsArrayList);

            ArrayList<String> seminarCommentsArrayList = new ArrayList<String>();
            JSONArray seminarCommentsArray = (JSONArray) json.get("seminarComments");
            for (int i = 0; i < seminarCommentsArray.length(); i++) {
                JSONObject object = seminarCommentsArray.getJSONObject(i);
                seminarCommentsArrayList.add(object.getString("seminarComment"));
            }
            groupList.add(seminarCommentsArrayList);

            ArrayList<String> projectCommentsArrayList = new ArrayList<String>();
            JSONArray projectCommentsArray = (JSONArray) json.get("projectComments");
            for (int i = 0; i < projectCommentsArray.length(); i++) {
                JSONObject object = projectCommentsArray.getJSONObject(i);
                projectCommentsArrayList.add(object.getString("projectComment"));
            }
            groupList.add(projectCommentsArrayList);

            ArrayList<String> homeassignmentCommentsArrayList = new ArrayList<String>();
            JSONArray homeassignmentCommentsArray = (JSONArray) json.get("homeassignmentComments");
            for (int i = 0; i < homeassignmentCommentsArray.length(); i++) {
                JSONObject object = homeassignmentCommentsArray.getJSONObject(i);
                homeassignmentCommentsArrayList.add(object.getString("homeassignmentComment"));
            }
            groupList.add(homeassignmentCommentsArrayList);

            ArrayList<String> caseCommentsArrayList = new ArrayList<String>();
            JSONArray caseCommentsArray = (JSONArray) json.get("caseComments");
            for (int i = 0; i < caseCommentsArray.length(); i++) {
                JSONObject object = caseCommentsArray.getJSONObject(i);
                caseCommentsArrayList.add(object.getString("caseComment"));
            }
            groupList.add(caseCommentsArrayList);

        }catch (Exception e){
            Log.e("GetComments", e.toString());
        }
    }


    /*
     * Adds the course part with the right value from database to informationRatingArrayList
     * Also calculates the average score and sets the text to that score
     */
    private void createInformationList(ArrayList<Float> informationRatingArrayList, ArrayList<ItemRating> informationArrayList, ArrayAdapter adapter) {
        float sumOfValues= 0.0f;
        int numberOfValues = 0;
        if(informationRatingArrayList.get(3)!=0){
            informationArrayList.add(new ItemRating("Usefulness", informationRatingArrayList.get(3)));
             //getSum(informationRatingArrayList.get(3));
            sumOfValues = sumOfValues+informationRatingArrayList.get(3);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(7)!=0){
            informationArrayList.add(new ItemRating("Difficulty", informationRatingArrayList.get(7)));
            // This values should not inflict the average points of the course
        }
        if(informationRatingArrayList.get(5)!=0){
            informationArrayList.add(new ItemRating("Exam", informationRatingArrayList.get(5)));
            //getSum(informationRatingArrayList.get(5));
            sumOfValues = sumOfValues+informationRatingArrayList.get(5);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(6)!=0){
            informationArrayList.add(new ItemRating("Lectures", informationRatingArrayList.get(6)));
            //getSum(informationRatingArrayList.get(6));
            sumOfValues = sumOfValues+informationRatingArrayList.get(6);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(0)!=0){
            informationArrayList.add(new ItemRating("Lessons", informationRatingArrayList.get(0)));
            //getSum(informationRatingArrayList.get(0));
            sumOfValues = sumOfValues+informationRatingArrayList.get(0);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(2)!=0){
            informationArrayList.add(new ItemRating("Laboratory", informationRatingArrayList.get(2)));
            //getSum(informationRatingArrayList.get(2));
            sumOfValues = sumOfValues+informationRatingArrayList.get(2);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(9)!=0){
            informationArrayList.add(new ItemRating("Seminar", informationRatingArrayList.get(8)));
            //getSum(informationRatingArrayList.get(8));
            sumOfValues = sumOfValues+informationRatingArrayList.get(8);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(8)!=0){
            informationArrayList.add(new ItemRating("Project", informationRatingArrayList.get(7)));
            //getSum(informationRatingArrayList.get(7));
            sumOfValues = sumOfValues+informationRatingArrayList.get(7);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(1)!=0){
            informationArrayList.add(new ItemRating("Homeassignment", informationRatingArrayList.get(1)));
            //getSum(informationRatingArrayList.get(1));
            sumOfValues = sumOfValues+informationRatingArrayList.get(1);
            numberOfValues++;
        }
        if(informationRatingArrayList.get(4)!=0){
            informationArrayList.add(new ItemRating("Case", informationRatingArrayList.get(4)));
            //getSum(informationRatingArrayList.get(4));
            sumOfValues = sumOfValues+informationRatingArrayList.get(4);
            numberOfValues++;
        }
        Float averageScore;
        averageScore = sumOfValues/numberOfValues;
        double result = (double) Math.round(averageScore*10)/10;
        String resultText = String.valueOf(result);
            courseAverageRatingTextView.setText(resultText);

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