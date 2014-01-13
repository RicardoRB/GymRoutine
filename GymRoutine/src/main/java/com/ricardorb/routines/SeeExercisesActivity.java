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

import java.io.File;
import java.io.FileInputStream;
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
        XmlPullParser parser = Xml.newPullParser();
        try {

            ArrayList<Item> items = new ArrayList<Item>();

            String[] muscles = getResources().getStringArray(R.array.array_muscles);

            int numMuscle = 0;

            int numDay = -1;

            parser.setInput(fin, "UTF-8");
            int event = parser.next();
            while (event != XmlPullParser.END_DOCUMENT) {
                //Days
                if (event == XmlPullParser.START_TAG) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (i == 0) {
                            //What num day is
                            numDay = Integer.parseInt(parser.getAttributeValue(i));
                        }
                    }
                    if (fragmentDay == numDay) {
                        int day = parser.next();
                        //Taking all the muscles
                        while (day != XmlPullParser.END_TAG) {
                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                if (i == 0) {
                                    //What num muscle is
                                    numMuscle = Integer.parseInt(parser.getAttributeValue(i));
                                    items.add(new SectionItem(muscles[numMuscle]));
                                }
                            }
                            if (day == XmlPullParser.START_TAG) {
                                int muscle = parser.next();
                                //Taking all the exercises
                                while (muscle != XmlPullParser.END_TAG) {
                                    if (muscle == XmlPullParser.START_TAG) {
                                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                                            if (i == 0) {
                                                switch (numMuscle) {
                                                    case 0:
                                                        String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                                                        for (int c = 0; c < chest.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(chest[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 1:
                                                        String[] back = getResources().getStringArray(R.array.array_back_exercises);
                                                        for (int c = 0; c < back.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(back[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 2:
                                                        String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                                                        for (int c = 0; c < biceps.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(biceps[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 3:
                                                        String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                                                        for (int c = 0; c < triceps.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(triceps[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 4:
                                                        String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                                                        for (int c = 0; c < shoulders.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(shoulders[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 5:
                                                        String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                                                        for (int c = 0; c < legs.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(legs[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 6:
                                                        String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                                                        for (int c = 0; c < forearms.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(forearms[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case 7:
                                                        String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                                                        for (int c = 0; c < abdominals.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(abdominals[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    default:
                                                        String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                                                        for (int c = 0; c < cardio.length; c++) {
                                                            if (Integer.parseInt(parser.getAttributeValue(i)) == c) {
                                                                items.add(new EntryItem(cardio[c], numMuscle, c));
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                    muscle = parser.next();
                                }
                            }
                            day = parser.next();
                        }
                        adapter = new EntryAdapter(getActivity(), items, true);
                        break;
                    }
                }
                event = parser.next();
            }
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
