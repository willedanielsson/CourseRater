package com.example.william.courserater;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CourseFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = "SearchViewFilterMode";

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private final String[] mStrings = Universities.universitiesStrings;

    public CourseFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mSearchView = (SearchView) rootView.findViewById(R.id.search_view);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String data;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Test");
        arrayList.add("Test2");
        arrayList.add("TEST4");
        arrayList.add("Test3");


        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mStrings);
        mListView = (ListView) rootView.findViewById(R.id.list_view);

/*
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://www.ashiya.se/connect2.php");
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);
            Log.e("DATA ", data);
            System.out.println(data);

            try {
                JSONArray json = new JSONArray(data);
                Log.e("JSON", json.toString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject object = json.getJSONObject(i);
                    String universityName = object.getString("name");
                    Log.e("UNIVERSTITET NAMN", universityName);
                    arrayList.add(universityName);
                    mListView.setAdapter(mAdapter);
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
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);
        mListView.setVisibility(View.VISIBLE);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //Scroll stop, show search
                        mSearchView.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        mSearchView.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        setupSearchView();

        return rootView;

    }

    public void setupSearchView(){
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint(getString(R.string.university_hint));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(TextUtils.isEmpty(newText)){
           mListView.clearTextFilter();
           System.out.println("Tom");
            //mAdapter.getFilter().filter(null);
            mListView.setVisibility(View.INVISIBLE);
        }else{
            //mAdapter.getFilter().filter(newText);
            mListView.setVisibility(View.VISIBLE);
            mListView.setFilterText(newText.toString());
        }
        return true;
    }
}
