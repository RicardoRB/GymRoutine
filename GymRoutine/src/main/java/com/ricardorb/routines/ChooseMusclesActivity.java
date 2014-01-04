package com.ricardorb.routines;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.ricardorb.gymroutine.R;

public class ChooseMusclesActivity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_muscles, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lvExercises);
        String muscles[] = getResources().getStringArray(R.array.array_muscles);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice, muscles));
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CheckedTextView item = (CheckedTextView)view;
                if(item.isChecked()){
                    ((DaysRoutineActivity)getActivity()).setChecked(position,false,ChooseMusclesActivity.this);
                }else{
                    ((DaysRoutineActivity)getActivity()).setChecked(position,true,ChooseMusclesActivity.this);
                }
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

}
