package com.ricardorb.routines;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ricardorb.gymroutine.R;
import com.ricardorb.listview_custom_sections.EntryAdapter;
import com.ricardorb.listview_custom_sections.EntryItem;
import com.ricardorb.listview_custom_sections.Item;
import com.ricardorb.listview_custom_sections.SectionItem;

import java.util.ArrayList;

public class ChooseExercisesActivity extends Fragment {

    boolean checked[];
    int fragmentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_exercises, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lvExercises);
        ArrayList<Item> items = new ArrayList<Item>();
        checked = ((DaysRoutineActivity) getActivity()).getChecked();
        fragmentDay = ((DaysRoutineActivity) getActivity()).getFragmentDay(this);
        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
        String[] muscles = getResources().getStringArray(R.array.array_muscles);

        int j = 0;
        for (int i = ((fragmentDay * numMuscles) - numMuscles); i < (fragmentDay * numMuscles); i++) {
            if (checked[i]) {
                items.add(new SectionItem(muscles[j]));
                switch (j) {
                    case 0:
                        String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                        for (String c : chest) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 1:
                        String[] back = getResources().getStringArray(R.array.array_back_exercises);
                        for (String c : back) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 2:
                        String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                        for (String c : biceps) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 3:
                        String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                        for (String c : triceps) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 4:
                        String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                        for (String c : shoulders) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 5:
                        String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                        for (String c : legs) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 6:
                        String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                        for (String c : forearms) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    case 7:
                        String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                        for (String c : abdominals) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                    default:
                        String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                        for (String c : cardio) {
                            items.add(new EntryItem(c, ""));
                        }
                        break;
                }
            }
            j++;
        }
        EntryAdapter adapter = new EntryAdapter(getActivity(), items);
        lv.setAdapter(adapter);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
