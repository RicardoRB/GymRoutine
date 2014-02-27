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
import android.widget.Button;
import com.ricardorb.modify_widgets.Chronometer;
import android.os.SystemClock;
import android.view.View.OnClickListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ChronometerActivity extends Fragment {
    private Chronometer crono;
    private boolean running = false;
    private boolean pause = false;
    private long time;
    private boolean cronoRun = true;
    private Button btnReset;
    private Button btnStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.chronometer, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chronometer, container, false);
        AdView adView = (AdView)rootView.findViewById(R.id.chronometer_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        this.crono = (Chronometer) rootView.findViewById(R.id.cm_chronometer);

        this.btnStart = (Button) rootView.findViewById(R.id.btn_start);
        this.btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    crono.stop();
                    time = SystemClock.elapsedRealtime();
                    btnStart.setText(getResources().getString(R.string.btn_start));
                    running = false;
                    pause = true;
                } else {
                    if (pause) {
                        crono.setStarted(true);
                    } else {
                        crono.start();
                    }
                    btnStart.setText(getResources().getString(R.string.btn_pause));
                    running = true;
                    pause = false;
                }
            }
        });

        this.btnReset = (Button) rootView.findViewById(R.id.btn_reset);
        this.btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                crono.setBase(SystemClock.elapsedRealtime());
                pause = false;
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_run_crono:
                if (item.isChecked()) {
                    item.setChecked(false);
                    this.cronoRun = false;
                    item.setIcon(android.R.drawable.checkbox_off_background);
                } else {
                    item.setChecked(true);
                    this.cronoRun = true;
                    item.setIcon(android.R.drawable.checkbox_on_background);
                }
                return true;
            case R.id.action_music_routines:
                startActivity(new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
