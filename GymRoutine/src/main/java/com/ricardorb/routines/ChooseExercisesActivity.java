package com.ricardorb.routines;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    boolean checkedExercises[][][];
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
        if (savedInstanceState != null) {
            fragmentDay = savedInstanceState.getInt("fragmentDay");
        } else {
            fragmentDay = ((DaysRoutineActivity) getActivity()).getFragmentDay(this);
        }
        checked = ((DaysRoutineActivity) getActivity()).getCheckedMuscles();
        checkedExercises = ((DaysRoutineActivity) getActivity()).getCheckedExercises().clone();

        int numMuscles = getResources().getStringArray(R.array.array_muscles).length;
        String[] muscles = getResources().getStringArray(R.array.array_muscles);

        int indexDayArray = (fragmentDay == 1 ? 0 : ((fragmentDay * numMuscles) - numMuscles));
        int j = 0; //Count what muscle is in that moment
        for (int i = indexDayArray; i < (fragmentDay * numMuscles); i++) {
            if (checked[i]) {
                items.add(new SectionItem(muscles[j]));
                switch (j) {
                    case 0:
                        String[] chest = getResources().getStringArray(R.array.array_chest_exercises);
                        for (int c = 0; c < chest.length; c++) {
                            items.add(new EntryItem(chest[c], j, c));
                        }
                        break;
                    case 1:
                        String[] back = getResources().getStringArray(R.array.array_back_exercises);
                        for (int c = 0; c < back.length; c++) {
                            items.add(new EntryItem(back[c], j, c));
                        }
                        break;
                    case 2:
                        String[] biceps = getResources().getStringArray(R.array.array_biceps_exercises);
                        for (int c = 0; c < biceps.length; c++) {
                            items.add(new EntryItem(biceps[c], j, c));
                        }
                        break;
                    case 3:
                        String[] triceps = getResources().getStringArray(R.array.array_triceps_exercises);
                        for (int c = 0; c < triceps.length; c++) {
                            items.add(new EntryItem(triceps[c], j, c));
                        }
                        break;
                    case 4:
                        String[] shoulders = getResources().getStringArray(R.array.array_shoulders_exercises);
                        for (int c = 0; c < shoulders.length; c++) {
                            items.add(new EntryItem(shoulders[c], j, c));
                        }
                        break;
                    case 5:
                        String[] legs = getResources().getStringArray(R.array.array_legs_exercises);
                        for (int c = 0; c < legs.length; c++) {
                            items.add(new EntryItem(legs[c], j, c));
                        }
                        break;
                    case 6:
                        String[] forearms = getResources().getStringArray(R.array.array_forearms_exercises);
                        for (int c = 0; c < forearms.length; c++) {
                            items.add(new EntryItem(forearms[c], j, c));
                        }
                        break;
                    case 7:
                        String[] abdominals = getResources().getStringArray(R.array.array_abdominals_exercises);
                        for (int c = 0; c < abdominals.length; c++) {
                            items.add(new EntryItem(abdominals[c], j, c));
                        }
                        break;
                    default:
                        String[] cardio = getResources().getStringArray(R.array.array_cardio_exercises);
                        for (int c = 0; c < cardio.length; c++) {
                            items.add(new EntryItem(cardio[c], j, c));
                        }
                        break;
                }
            }
            j++;
        }
        EntryAdapter adapter = new EntryAdapter(getActivity(), items, checkedExercises[fragmentDay - 1], fragmentDay,false);
        lv.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray("checked", this.checked);
        outState.putInt("fragmentDay", this.fragmentDay);
        super.onSaveInstanceState(outState);
    }

}
