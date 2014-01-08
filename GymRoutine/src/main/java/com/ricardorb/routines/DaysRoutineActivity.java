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
    boolean checkedMuscles[];
    boolean checkedExercises[][][];
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
            if (savedInstanceState != null) {
                checkedMuscles = savedInstanceState.getBooleanArray("checkedMuscles");
            } else {
                checkedMuscles = getIntent().getExtras().getBooleanArray("checkedBoolean");
            }
            if (savedInstanceState != null) {

                int iLength = savedInstanceState.getInt("iLength");
                int jLength = savedInstanceState.getInt("jLength");
                int cLength = savedInstanceState.getInt("cLength");
                //Getting 3 dimensional array
                boolean exercises[][][] = new boolean[iLength][jLength][cLength];
                for (int i = 0; i < iLength; i++) {
                    for (int j = 0; j < savedInstanceState.getInt("j" + i); j++) {
                        boolean xExercises[] = savedInstanceState.getBooleanArray("checkedExercises" + i + " " + j);
                        exercises[i][j] = xExercises.clone();
                    }
                }
                this.checkedExercises = exercises.clone();

            } else {
                checkedExercises = new boolean[numDays][getResources().getStringArray(R.array.array_muscles).length][getResources().getStringArray(R.array.array_legs_exercises).length];
            }

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
            if (savedInstanceState != null) {
                checkedMuscles = savedInstanceState.getBooleanArray("checkedMuscles");
            } else {
                checkedMuscles = new boolean[numDays * getResources().getStringArray(R.array.array_muscles).length];
            }
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
        } else {
            return new Intent(this, AddRoutineActivity.class);
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
            i.putExtra("checkedBoolean", this.checkedMuscles);
            i.putExtra("numDays", this.numDays);
            i.putExtra("nameRoutine", this.nameRoutine);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Saving a 3 dimension array
        if (this.checkedExercises != null) {
            outState.putInt("iLength", checkedExercises.length);
            int jLength = 0;
            int cLength = 0;
            for (int i = 0; i < checkedExercises.length; i++) {
                outState.putInt("j" + i, checkedExercises[i].length);
                for (int j = 0; j < checkedExercises[i].length; j++) {
                    if (checkedExercises[i].length > jLength) {
                        jLength = checkedExercises[i].length;
                    }
                    if (this.checkedExercises[i][j].length > cLength) {
                        cLength = this.checkedExercises[i][j].length;
                    }
                    outState.putBooleanArray("checkedExercises" + i + " " + j, this.checkedExercises[i][j]);
                }
            }
            outState.putInt("jLength", jLength);
            outState.putInt("cLength", cLength);
        }
        outState.putBooleanArray("checkedMuscles", this.checkedMuscles);
        outState.putString("nameRoutine", this.nameRoutine);
        outState.putBoolean("fromMusclesFragments", this.fromMusclesFragments);

        super.onSaveInstanceState(outState);
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

    public void setCheckedMuscles(int position, boolean check, Fragment fragment) {
        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;

        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).equals(fragment)) {
                checkedMuscles[((i * numMuscles) + position)] = check;
                break;
            }
        }
    }

    public void setCheckedExercises(String nameExercise, boolean check, int fragmentDay) {
        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
        for (int i = 0; i < fragments.size(); i++) {
            if ((fragmentDay - 1) == i) {
                int indexDayArray = (i == 0 ? 0 : (((i + 1) * numMuscles) - numMuscles));

                for (int j = indexDayArray; j < ((i + 1) * numMuscles); j++) {
                    if (checkedMuscles[j]) {
                        switch (j) {
                            case 0:
                                String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                                for (int c = 0; c < chest.length; c++) {
                                    if (nameExercise.equals(chest[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }

                                break;
                            case 1:
                                String[] back = getResources().getStringArray(R.array.array_back_exercises);
                                for (int c = 0; c < back.length; c++) {
                                    if (nameExercise.equals(back[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 2:
                                String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                                for (int c = 0; c < biceps.length; c++) {
                                    if (nameExercise.equals(biceps[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 3:
                                String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                                for (int c = 0; c < triceps.length; c++) {
                                    if (nameExercise.equals(triceps[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 4:
                                String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                                for (int c = 0; c < shoulders.length; c++) {
                                    if (nameExercise.equals(shoulders[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 5:
                                String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                                for (int c = 0; c < legs.length; c++) {
                                    if (nameExercise.equals(legs[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 6:
                                String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                                for (int c = 0; c < forearms.length; c++) {
                                    if (nameExercise.equals(forearms[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            case 7:
                                String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                                for (int c = 0; c < abdominals.length; c++) {
                                    if (nameExercise.equals(abdominals[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                            default:
                                String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                                for (int c = 0; c < cardio.length; c++) {
                                    if (nameExercise.equals(cardio[c])) {
                                        checkedExercises[i][j][c] = check;
                                    }
                                }
                                break;
                        }
                    }
                }
                break;
            }
        }
    }

    public boolean[][][] getCheckedExercises() {
        return this.checkedExercises;
    }

    public boolean[] getCheckedMuscles() {
        return this.checkedMuscles;
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
