package com.example.william.courserater;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Main extends Activity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();

        // Initialise the drawer
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        /*
        * If you want to add fragments, here is the DrawerItems
         */
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.book, getResources().getString(R.string.courses));
        drawerItem[1] = new ObjectDrawerItem(R.drawable.user, getResources().getString(R.string.my_profile));
        drawerItem[2] = new ObjectDrawerItem(R.drawable.settings, getResources().getString(R.string.settings));
        drawerItem[3] = new ObjectDrawerItem(R.drawable.about, getResources().getString(R.string.about));

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
                ){
            /** Called when a drawer has settled in a completely closed state*/
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state */
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }

    /*/

     */
    public void startNewCourseFragment(String course){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment newCourseInformationFragment = CourseInformationFragment.newInstance(course);
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, newCourseInformationFragment);
        transaction.commit();
    }

    public void createAddCourseFragment(String country, String university, ArrayList<String> countryArrayList, ArrayList<String> universityArrayList){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment addCourseFragment = AddCourseFragment.newInstance(country, university, countryArrayList, universityArrayList);
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, addCourseFragment);
        transaction.commit();
    }

    public void createCoursePartFragment(String courseName, String coursePart, ArrayList<String> clickedPartCommentList){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment coursePartFragment = CoursePartFragment.newInstance(courseName, coursePart, clickedPartCommentList);
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, coursePartFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    /*
    * If you want to add fragment, do it here
    *
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                 fragment = new CourseFragment();
                break;
            case 1:
                //fragment = new MyProfileFragment();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case 2:
                fragment = new SettingsFragment();
                break;
            case 3:
                fragment = new AboutFragment();
                break;

           default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}