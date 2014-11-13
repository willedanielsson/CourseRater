package com.example.william.courserater;



import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CoursePartFragment extends Fragment {

    private TextView coursePartTextView;
    private ListView commentListView;


    public CoursePartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_course_part, container, false);

        String courseName = getArguments().getString("courseName");
        String coursePart = getArguments().getString("coursePart");

        ArrayList<String> commentsArrayList = getArguments().getStringArrayList("partComments");

        coursePartTextView = (TextView) rootView.findViewById(R.id.course_part_text_view);
        coursePartTextView.setText(courseName+" - "+coursePart);

        System.out.println(commentsArrayList);

        ArrayList<ItemComment> comments = new ArrayList<ItemComment>();

        final AdapterComment adapter = new AdapterComment(rootView.getContext(), comments);

        commentListView = (ListView) rootView.findViewById(R.id.comments_list_view);

        for(String string:commentsArrayList){
            comments.add(new ItemComment("2014", string));
        }
        commentListView.setAdapter(adapter);


        return rootView;
    }

    public static CoursePartFragment newInstance(String courseName, String coursePart, ArrayList<String> clickedPartCommentList){
        CoursePartFragment coursePartFragment = new CoursePartFragment();

        Bundle args = new Bundle();
        args.putString("courseName", courseName);
        args.putString("coursePart", coursePart);
        args.putStringArrayList("partComments", clickedPartCommentList);
        coursePartFragment.setArguments(args);
        return coursePartFragment;
    }
}
