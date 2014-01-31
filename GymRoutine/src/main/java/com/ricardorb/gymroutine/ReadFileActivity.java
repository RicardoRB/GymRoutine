package com.ricardorb.gymroutine;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Xml;
import android.view.Menu;

import com.ricardorb.adapters.PagerAdapter;
import com.ricardorb.routines.SeeExercisesActivity;

import org.xmlpull.v1.XmlPullParser;

public class ReadFileActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    PagerAdapter mSectionsPagerAdapter;
    List<Fragment> fragments = new Vector<Fragment>();
    private String nameFile;
    int numDays = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Name of the file
        nameFile = getIntent().getExtras().getString("nameFile");

        readXML();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        for (int i = 0; i < numDays; i++) {
            fragments.add(Fragment.instantiate(this, SeeExercisesActivity.class.getName()));
        }
        mSectionsPagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), fragments);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(getResources().getString(R.string.tab_day) + " " + (i + 1))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read_file, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private void readXML() {

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "GymRoutines" + File.separator + nameFile);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(fin, "UTF-8");
            int event = parser.next();
            while (event != XmlPullParser.END_DOCUMENT) {
                //Days
                if (event == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("Day")) {
                        this.numDays++;
                    }
                }
                event = parser.next();
            }
            fin.close();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }
    }

    public int getFragmentDay(Fragment fragment) {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).equals(fragment)) {
                return i;
            }
        }
        return -1;
    }

    public String getNameFile() {
        return nameFile;
    }

}
