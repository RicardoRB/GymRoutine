package com.ricardorb.routines;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ricardorb.gymroutine.R;

public class ChooseMusclesActivity extends Fragment {

    private int fragmentDay;
    private boolean checked[];
    private int indexDayArray;
    private int numMuscles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_muscles, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lvMuscles);
        String muscles[] = getResources().getStringArray(R.array.array_muscles);
        if (savedInstanceState != null) {
            fragmentDay = savedInstanceState.getInt("fragmentDay");
        } else {
            fragmentDay = ((DaysRoutineActivity) getActivity()).getFragmentDay(this);
        }
        numMuscles = muscles.length;
        indexDayArray = (fragmentDay == 1 ? 0 : ((fragmentDay * numMuscles) - numMuscles));
        checked = ((DaysRoutineActivity) getActivity()).getCheckedMuscles();

        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, muscles));
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for(int i = 0;i < numMuscles; i++){
            lv.setItemChecked(i, checked[indexDayArray + i]);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ((DaysRoutineActivity) getActivity()).setCheckedMuscles(position, !checked[indexDayArray + position], ChooseMusclesActivity.this);
            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragmentDay", this.fragmentDay);
        super.onSaveInstanceState(outState);
    }

}
