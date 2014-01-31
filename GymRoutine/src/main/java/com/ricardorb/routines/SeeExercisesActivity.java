package com.ricardorb.routines;

import android.app.AlertDialog;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ricardorb.gymroutine.R;
import com.ricardorb.gymroutine.ReadFileActivity;
import com.ricardorb.listview_custom_sections.EntryAdapter;
import com.ricardorb.listview_custom_sections.EntryItem;
import com.ricardorb.listview_custom_sections.Item;
import com.ricardorb.listview_custom_sections.SectionItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SeeExercisesActivity extends Fragment {

    int fragmentDay;
    EntryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_exercises, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lvSeeExercises);
        if (savedInstanceState != null) {
            fragmentDay = savedInstanceState.getInt("fragmentDay");
        } else {
            fragmentDay = ((ReadFileActivity) getActivity()).getFragmentDay(this);
        }
        readXML();
        lv.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragmentDay", this.fragmentDay);
        super.onSaveInstanceState(outState);
    }

    public void readXML() {

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "GymRoutines" + File.separator + ((ReadFileActivity) getActivity()).getNameFile());
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }

        try {

            XmlPullParser parser = Xml.newPullParser();
            ArrayList<Item> items = new ArrayList<Item>();
            int numDay = -1;
            int numMuscle = -1;
            String[] muscles = getResources().getStringArray(R.array.array_muscles);

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
                } else if (name.equals("Muscle") && numDay == fragmentDay) {
                    //Taking the numMuscle of every muscle I seek,
                    //the parser will not coming back here until
                    //each exercise of this muscle is read
                    numMuscle = Integer.parseInt(parser.getAttributeValue(null, "num"));
                    items.add(new SectionItem(muscles[numMuscle]));
                } else if (name.equals("Exercise") && numDay == fragmentDay) {
                    String numExercise = parser.getAttributeValue(null, "num");
                    switch (numMuscle) {
                        case 0:
                            String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                            items.add(new EntryItem(chest[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 1:
                            String[] back = getResources().getStringArray(R.array.array_back_exercises);
                            items.add(new EntryItem(back[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 2:
                            String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                            items.add(new EntryItem(biceps[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 3:
                            String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                            items.add(new EntryItem(triceps[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 4:
                            String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                            items.add(new EntryItem(shoulders[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 5:
                            String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                            items.add(new EntryItem(legs[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 6:
                            String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                            items.add(new EntryItem(forearms[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        case 7:
                            String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                            items.add(new EntryItem(abdominals[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                        default:
                            String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                            items.add(new EntryItem(cardio[Integer.parseInt(numExercise)], numMuscle, Integer.parseInt(numExercise)));
                            break;
                    }
                }

            }
            adapter = new EntryAdapter(getActivity(), items, true);
            fin.close();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.alert_title_errorOpenFile));
            builder.setMessage(getResources().getString(R.string.alert_message_errorOpenFile) + " " + e.getMessage());
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.ic_launcher);
            builder.create().show();
        }

    }
}
