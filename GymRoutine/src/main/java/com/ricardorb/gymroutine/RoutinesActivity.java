package com.ricardorb.gymroutine;

import android.content.Intent;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ricardorb.adapters.ListRoutinesAdapter;
import com.ricardorb.routines.AddRoutineActivity;
import com.ricardorb.routines.ReadFileActivity;

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
                if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 )  {
                    startActivity(new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER));
                }
                else  {
                    startActivity(new Intent(Intent.CATEGORY_APP_MUSIC));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final ListView lv = (ListView) inflater.inflate(R.layout.fragment_routines, container, false);
            ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity().getBaseContext().getApplicationContext());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(getActivity(), ReadFileActivity.class);
                    i.putExtra("nameFile",((TextView)((LinearLayout) view).findViewWithTag(position)).getText().toString());
                    startActivity(i);
                }
            });
            lv.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
                    // TODO Auto-generated method stub

                    return false;
                }
            });
            return lv;
        }
}
