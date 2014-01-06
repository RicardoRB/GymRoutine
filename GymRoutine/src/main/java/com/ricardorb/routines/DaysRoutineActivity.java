package com.ricardorb.routines;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.ricardorb.adapters.PagerAdapter;
import com.ricardorb.gymroutine.R;

public class DaysRoutineActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    PagerAdapter mSectionsPagerAdapter;
    boolean checked[];
    List<Fragment> fragments = new Vector<Fragment>();
    int numDays;
    String nameRoutine;
    boolean fromMusclesFragments;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_routine);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        numDays = getIntent().getExtras().getInt("numDays");
        //Name of the file
        nameRoutine = getIntent().getExtras().getString("nameRoutine");
        if (getIntent().getExtras().getBooleanArray("checkedBoolean") != null) {
            this.fromMusclesFragments = false;
            checked = getIntent().getExtras().getBooleanArray("checkedBoolean");
            for (int i = 0; i < numDays; i++) {
                fragments.add(Fragment.instantiate(this, ChooseExercisesActivity.class.getName()));
            }
        } else {
            fromMusclesFragments = true;
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            for (int i = 0; i < numDays; i++) {
                fragments.add(Fragment.instantiate(this, ChooseMusclesActivity.class.getName()));
            }

            checked = new boolean[numDays * getResources().getStringArray(R.array.array_muscles).length];
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
    public Intent getSupportParentActivityIntent() {
        if (getIntent().getExtras().getBooleanArray("checkedBoolean") != null) {
            Intent i = new Intent(this, DaysRoutineActivity.class);
            i.putExtra("numDays", this.numDays);
            i.putExtra("nameRoutine", this.nameRoutine);
            return i;
        }else{
            Intent i2 = new Intent(this, AddRoutineActivity.class);
            return i2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.days_routine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_routineDone && fromMusclesFragments) {
            Intent i = new Intent(this, DaysRoutineActivity.class);
            i.putExtra("checkedBoolean", this.checked);
            i.putExtra("numDays", this.numDays);
            i.putExtra("nameRoutine", this.nameRoutine);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void setChecked(int position, boolean check, Fragment fragment) {
        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;

        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).equals(fragment)) {
                checked[((i * numMuscles) + position)] = check;
                break;
            }
        }
    }

    public boolean[] getChecked() {
        return this.checked;
    }

    public int getFragmentDay(Fragment fragment) {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).equals(fragment)) {
                return (i + 1);
            }
        }
        return -1;
    }

}
