package com.example.william.courserater;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CourseFragment extends Fragment{

    private EditText universityEditText;
    private ListView universityListView;

    private EditText courseEditText;
    private ListView courseListView;

    public CourseFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course, container, false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        universityEditText = (EditText) rootView.findViewById(R.id.university_edit_text);
        final ArrayList<String> universityArrayList = new ArrayList<String>();
        final ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universityArrayList);
        universityListView = (ListView) rootView.findViewById(R.id.university_list_view);

         courseEditText = (EditText) rootView.findViewById(R.id.course_edit_text);
         final ArrayList<String> courseArrayList = new ArrayList<String>();
            courseArrayList.add("Hej");
            courseArrayList.add("Hej2");
            courseArrayList.add("Hej3");
         final ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, courseArrayList);
         courseListView = (ListView) rootView.findViewById(R.id.course_list_view);
         courseListView.setAdapter(courseAdapter);

         try{
            HttpClient.get("connect.php", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String universityName = json.get(i).toString();
                            universityArrayList.add(universityName);
                            universityListView.setAdapter(universityAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                // Do something with the response
            });
        }catch (Exception e){
            Log.e("Fel", e.toString());
        }

        universityListView.setVisibility(View.INVISIBLE);

        universityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemText = (String)adapterView.getItemAtPosition(position);
                universityEditText.setText(itemText);
                universityListView.setVisibility(View.INVISIBLE);

            }
        });

        universityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                universityAdapter.getFilter().filter(charSequence);
                if(charSequence.length()==0){
                    universityListView.setVisibility(View.INVISIBLE);
                }else{
                    universityListView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });






        return rootView;

    }

}

/*

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://www.ashiya.se/app/connect.php");
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);

            try {
                JSONArray json = new JSONArray(data);
                for (int i = 0; i < json.length(); i++) {
                    String universityName = json.get(i).toString();
                    arrayList.add(universityName);
                    universityListView.setAdapter(mAdapter);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            } catch (ClientProtocolException e) {
                Log.d("HTTPCLIENT", e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("HTTPCLIENT", e.getLocalizedMessage());
         }
 */

   /*
        universityListView.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //Scroll stop, show search
                        universityEditText.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        universityEditText.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        */