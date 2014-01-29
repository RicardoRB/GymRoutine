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

    private void readMuscle(XmlPullParser parser, ArrayList<Item> items) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Muscle");
        String[] muscles = getResources().getStringArray(R.array.array_muscles);
        int numMuscle = -1;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                if (parser.getAttributeName(i).equals("num")) {
                    //What num muscle is
                    numMuscle = Integer.parseInt(parser.getAttributeValue(i));
                    items.add(new SectionItem(muscles[numMuscle]));
                }
            }
            String name = parser.getName();
            if (name.equals("Exercise")) {
                readExercise(parser, items, numMuscle);
            } else {
                skip(parser);
            }
        }
    }

    private void readExercise(XmlPullParser parser, ArrayList<Item> items, int numMuscle) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Exercise");
        String numExercise = parser.getAttributeValue(null, "num");
        switch (numMuscle) {
            case 0:
                String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                for (int c = 0; c < chest.length; c++) {
                    if (numExercise.equals(chest[c])) {
                        items.add(new EntryItem(chest[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 1:
                String[] back = getResources().getStringArray(R.array.array_back_exercises);
                for (int c = 0; c < back.length; c++) {
                    if (numExercise.equals(back[c])) {
                        items.add(new EntryItem(back[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 2:
                String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                for (int c = 0; c < biceps.length; c++) {
                    if (numExercise.equals(biceps[c])) {
                        items.add(new EntryItem(biceps[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 3:
                String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                for (int c = 0; c < triceps.length; c++) {
                    if (numExercise.equals(triceps[c])) {
                        items.add(new EntryItem(triceps[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 4:
                String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                for (int c = 0; c < shoulders.length; c++) {
                    if (numExercise.equals(shoulders[c])) {
                        items.add(new EntryItem(shoulders[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 5:
                String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                for (int c = 0; c < legs.length; c++) {
                    if (numExercise.equals(legs[c])) {
                        items.add(new EntryItem(legs[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 6:
                String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                for (int c = 0; c < forearms.length; c++) {
                    if (numExercise.equals(forearms[c])) {
                        items.add(new EntryItem(forearms[c], numMuscle, c));
                        break;
                    }
                }
                break;
            case 7:
                String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                for (int c = 0; c < abdominals.length; c++) {
                    if (numExercise.equals(abdominals[c])) {
                        items.add(new EntryItem(abdominals[c], numMuscle, c));
                        break;
                    }
                }
                break;
            default:
                String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                for (int c = 0; c < cardio.length; c++) {
                    if (numExercise.equals(cardio[c])) {
                        items.add(new EntryItem(cardio[c], numMuscle, c));
                        break;
                    }
                }
                break;
        }

        parser.require(XmlPullParser.END_TAG, null, "Exercise");
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
