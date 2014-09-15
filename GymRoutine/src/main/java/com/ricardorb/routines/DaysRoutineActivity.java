package com.ricardorb.routines;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;

import com.ricardorb.adapters.PagerAdapter;
import com.ricardorb.gymroutine.MainActivity;
import com.ricardorb.gymroutine.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

public class DaysRoutineActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mSectionsPagerAdapter;
    private boolean checkedMuscles[];
    private boolean checkedExercises[][][];
    private List<Fragment> fragments = new Vector<Fragment>();
    private int numDays;
    private String nameRoutine;
    private boolean fromMusclesFragments;
    private boolean modify;

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
        modify = getIntent().getExtras().getBoolean("modify");
        numDays = getIntent().getExtras().getInt("numDays");

        //Name of the file
        nameRoutine = getIntent().getExtras().getString("nameRoutine");
        if (modify) {
            try {
                nameRoutine = nameRoutine.substring(0, nameRoutine.indexOf("."));
            } catch (IndexOutOfBoundsException e) {
                //Can not substring when is modifying exercises
            }
        }
        if (getIntent().getExtras().getBooleanArray("checkedBoolean") != null) {
            this.fromMusclesFragments = false;
            if (savedInstanceState != null) {
                checkedMuscles = savedInstanceState.getBooleanArray("checkedMuscles");

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
                checkedMuscles = getIntent().getExtras().getBooleanArray("checkedBoolean");
                checkedExercises = new boolean[numDays][getResources().getStringArray(R.array.array_muscles).length][numMaxExercises()];
                if (modify) {
                    readXMLBooleansExercises();
                }
            }

            for (int i = 0; i < numDays; i++) {
                fragments.add(Fragment.instantiate(this, ChooseExercisesActivity.class.getName()));
            }

        } else {
            if (modify && numDays < 1) {
                readXMLNumMaxDays();
            }
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
            if (modify) {
                checkedExercises = new boolean[numDays][getResources().getStringArray(R.array.array_muscles).length][numMaxExercises()];
                readXMLBooleansMuscles();
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

    private int numMaxExercises() {
        int maxNumExercises = 0;
        int maxExercises[] = {getResources().getStringArray(R.array.array_legs_exercises).length, getResources().getStringArray(R.array.array_abdominals_exercises).length
                , getResources().getStringArray(R.array.array_back_exercises).length, getResources().getStringArray(R.array.array_biceps_exercises).length
                , getResources().getStringArray(R.array.array_biceps_exercises).length, getResources().getStringArray(R.array.array_cardio_exercises).length
                , getResources().getStringArray(R.array.array_chest_exercises).length, getResources().getStringArray(R.array.array_triceps_exercises).length
                , getResources().getStringArray(R.array.array_forearms_exercises).length, getResources().getStringArray(R.array.array_shoulders_exercises).length};
        for (int max : maxExercises) {
            if (max > maxNumExercises) {
                maxNumExercises = max;
            }
        }
        return maxNumExercises;
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        if (getIntent().getExtras().getBooleanArray("checkedBoolean") != null) {
            Intent i = new Intent(this, DaysRoutineActivity.class);
            i.putExtra("numDays", this.numDays);
            i.putExtra("nameRoutine", this.nameRoutine);
            i.putExtra("modify", this.modify);
            return i;
        } else {
            if (modify) {
                return new Intent(this, MainActivity.class);
            } else {
                return new Intent(this, AddRoutineActivity.class);
            }
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
            i.putExtra("modify", this.modify);
            startActivity(i);
            return true;
        } else if (id == R.id.action_routineDone && !fromMusclesFragments) {
            createXML();
            startActivity(new Intent(this, MainActivity.class));
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
        int countMuscle = 0;
        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
        for (int i = 0; i < numDays; i++) {
            if ((fragmentDay - 1) == i) {
                int indexDayArray = (i == 0 ? 0 : (((i + 1) * numMuscles) - numMuscles));
                for (int j = indexDayArray; j < ((i + 1) * numMuscles); j++) {
                    if (checkedMuscles[j]) {
                        switch (countMuscle) {
                            case 0:
                                String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                                for (int c = 0; c < chest.length; c++) {
                                    if (nameExercise.equals(chest[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }

                                break;
                            case 1:
                                String[] back = getResources().getStringArray(R.array.array_back_exercises);
                                for (int c = 0; c < back.length; c++) {
                                    if (nameExercise.equals(back[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 2:
                                String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                                for (int c = 0; c < biceps.length; c++) {
                                    if (nameExercise.equals(biceps[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 3:
                                String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                                for (int c = 0; c < triceps.length; c++) {
                                    if (nameExercise.equals(triceps[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 4:
                                String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                                for (int c = 0; c < shoulders.length; c++) {
                                    if (nameExercise.equals(shoulders[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 5:
                                String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                                for (int c = 0; c < legs.length; c++) {
                                    if (nameExercise.equals(legs[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 6:
                                String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                                for (int c = 0; c < forearms.length; c++) {
                                    if (nameExercise.equals(forearms[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            case 7:
                                String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                                for (int c = 0; c < abdominals.length; c++) {
                                    if (nameExercise.equals(abdominals[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                            default:
                                String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);

                                for (int c = 0; c < cardio.length; c++) {
                                    if (nameExercise.equals(cardio[c])) {
                                        checkedExercises[i][countMuscle][c] = check;
                                    }
                                }
                                break;
                        }
                    }
                    countMuscle++;
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


    private void readXMLNumMaxDays() {

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "GymRoutines" + File.separator + this.nameRoutine + ".gym");
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

    public void readXMLBooleansMuscles(){

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "GymRoutines" + File.separator + nameRoutine + ".gym");
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }

        try {

            XmlPullParser parser = Xml.newPullParser();
            int numDay = -1;
            int numMuscle = -1;

            parser.setInput(fin, "UTF-8");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                //I get the name
                String name = parser.getName();
                //Always Day will be the first tag to found
                if (name.equals("Day")) {
                    //I take what number of day is that day
                    numDay = Integer.parseInt(parser.getAttributeValue(null, "num"));
                } else if (name.equals("Muscle")) {
                    //Taking the numMuscle of every muscle I seek,
                    //the parser will not coming back here until
                    //each exercise of this muscle is read
                    numMuscle = Integer.parseInt(parser.getAttributeValue(null, "num"));
                    int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
                    int indexDayArray = (((numDay+1) * numMuscles) - numMuscles);
                    checkedMuscles[indexDayArray + numMuscle] = true;
                }
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


    public void readXMLBooleansExercises() {

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "GymRoutines" + File.separator + nameRoutine + ".gym");
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }

        try {

            XmlPullParser parser = Xml.newPullParser();
            int numDay = -1;
            int numMuscle = -1;

            parser.setInput(fin, "UTF-8");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                //I get the name
                String name = parser.getName();
                //Always Day will be the first tag to found
                if (name.equals("Day")) {
                    //I take what number of day is that day
                    numDay = Integer.parseInt(parser.getAttributeValue(null, "num"));
                } else if (name.equals("Muscle")) {
                    //Taking the numMuscle of every muscle I seek,
                    //the parser will not coming back here until
                    //each exercise of this muscle is read
                    numMuscle = Integer.parseInt(parser.getAttributeValue(null, "num"));
                } else if (name.equals("Exercise")) {
                    int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
                    int indexDayArray = (((numDay+1) * numMuscles) - numMuscles);
                    if (checkedMuscles[indexDayArray + numMuscle]) {
                        int numExercise = Integer.parseInt(parser.getAttributeValue(null, "num"));
                        checkedExercises[numDay][numMuscle][numExercise] = true;
                    }
                }
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

    public void createXML() {

        FileOutputStream fout = null;

        boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        try {
            if (isSDPresent) {
                fout = new FileOutputStream(Environment.getExternalStorageDirectory()
                        + File.separator + "GymRoutines" + File.separator + nameRoutine + ".gym");
            } else {
                fout = openFileOutput(nameRoutine + ".gym", Context.MODE_PRIVATE);
            }
        } catch (FileNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorCreatingFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorCreatingFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }

        final XmlSerializer serializer = Xml.newSerializer();

        try {
            serializer.setOutput(fout, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
            int countMuscle = 0;

            serializer.startTag(null, "GymRoutine");

            for (int i = 0; i < numDays; i++) {
                serializer.startTag(null, "Day");
                serializer.attribute(null, "num", String.valueOf(i));

                int indexDayArray = (i == 0 ? 0 : (((i + 1) * numMuscles) - numMuscles));

                for (int j = indexDayArray; j < ((i + 1) * numMuscles); j++) {
                    if (checkedMuscles[j]) {
                        serializer.startTag(null, "Muscle");
                        serializer.attribute(null, "num", String.valueOf(countMuscle));
                        switch (countMuscle) {
                            case 0:
                                String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                                for (int c = 0; c < chest.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }

                                break;
                            case 1:
                                String[] back = getResources().getStringArray(R.array.array_back_exercises);
                                for (int c = 0; c < back.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 2:
                                String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                                for (int c = 0; c < biceps.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 3:
                                String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                                for (int c = 0; c < triceps.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 4:
                                String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                                for (int c = 0; c < shoulders.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 5:
                                String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                                for (int c = 0; c < legs.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 6:
                                String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                                for (int c = 0; c < forearms.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            case 7:
                                String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                                for (int c = 0; c < abdominals.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                            default:
                                String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                                for (int c = 0; c < cardio.length; c++) {
                                    if (checkedExercises[i][countMuscle][c]) {
                                        serializer.startTag(null, "Exercise");
                                        serializer.attribute(null, "num", String.valueOf(c));
                                        serializer.endTag(null, "Exercise");
                                    }
                                }
                                break;
                        }
                        serializer.endTag(null, "Muscle");
                    }
                    countMuscle++;
                }
                countMuscle = 0;
                serializer.endTag(null, "Day");
            }

            serializer.endTag(null, "GymRoutine");

            serializer.endDocument();
            serializer.flush();
            fout.close();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DaysRoutineActivity.this);
            builder.setTitle(getResources().getString(R.string.alert_title_errorCreatingFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorCreatingFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }
    }

}
