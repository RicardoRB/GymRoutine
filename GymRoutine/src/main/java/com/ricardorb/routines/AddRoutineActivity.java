package com.ricardorb.routines;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.ads.*;

import com.google.android.gms.ads.AdView;
import com.ricardorb.gymroutine.R;

import java.io.File;

public class AddRoutineActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_routine, menu);
        return true;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        Spinner sp;
        EditText et;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_routine, container, false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerDays);
            et = (EditText) rootView.findViewById(R.id.editTextNameRoutine);
            Button next = (Button) rootView.findViewById(R.id.btn_Next);
            AdView adView = (AdView)rootView.findViewById(R.id.add_routine_adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            final boolean isSDPresent = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File fileRoutine = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "GymRoutines"+File.separator+et.getText().toString()+".gym");


                    if (et.getText().length() <= 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(R.string.alert_title_nameRoutine));
                        builder.setMessage(getResources().getString(R.string.alert_message_nameRoutine));
                        builder.setPositiveButton("OK", null);
                        builder.setIcon(R.drawable.ic_launcher);
                        builder.create().show();
                    }else if(!isSDPresent){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(R.string.alert_title_errorSD));
                        builder.setMessage(getResources().getString(R.string.alert_message_errorSD));
                        builder.setPositiveButton("OK", null);
                        builder.setIcon(R.drawable.ic_launcher);
                        builder.create().show();
                    }else if(fileRoutine.exists()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(R.string.alert_title_fileExist));
                        builder.setMessage(getResources().getString(R.string.alert_message_fileExist));
                        builder.setPositiveButton("OK", null);
                        builder.setIcon(R.drawable.ic_launcher);
                        builder.create().show();
                    } else {
                        //I pass the days number select by the user
                        //and plus it +1 because the position start in 0
                        Intent i = new Intent(getActivity(), DaysRoutineActivity.class);
                        i.putExtra("numDays", sp.getSelectedItemPosition() + 1);
                        i.putExtra("nameRoutine", et.getText().toString().replace(" ", ""));
                        getActivity().startActivity(i);
                    }
                }
            });
            return rootView;
        }

    }

}
