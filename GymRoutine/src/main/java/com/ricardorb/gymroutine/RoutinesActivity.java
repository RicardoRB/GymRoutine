package com.ricardorb.gymroutine;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ricardorb.adapters.ListRoutinesAdapter;
import com.ricardorb.routines.AddRoutineActivity;

public class RoutinesActivity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.routines, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_routine:
                startActivity(new Intent(getActivity(),AddRoutineActivity.class));
                return true;
            case R.id.action_music_routines:
                startActivity(new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            ListView lv = (ListView) inflater.inflate(R.layout.fragment_routines, container, false);
            ListRoutinesAdapter adaptador = new ListRoutinesAdapter(getActivity().getBaseContext().getApplicationContext());
            lv.setAdapter(adaptador);
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity().getBaseContext().getApplicationContext(), "Has clickeado la pos "+position, Toast.LENGTH_SHORT).show();
                }
            });
            lv.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity().getBaseContext().getApplicationContext(), "Has long clickeado la pos "+position, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            return lv;
        }
}
