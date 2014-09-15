package com.ricardorb.gymroutine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ricardorb.adapters.ListRoutinesAdapter;
import com.ricardorb.routines.AddRoutineActivity;
import com.ricardorb.routines.DaysRoutineActivity;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import java.io.File;

public class RoutinesActivity extends Fragment {
    ActionSlideExpandableListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRoutines();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.routines, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_routine:
                startActivity(new Intent(getActivity(), AddRoutineActivity.class));
                return true;
            case R.id.action_music_routines:
                startActivity(new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshRoutines() {
        ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity());
        list.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_routines, container, false);
        list = (ActionSlideExpandableListView) rootView.findViewById(R.id.list);
        ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity());
        list.setAdapter(adapter);
        list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

            @Override
            public void onClick(View itemView, View clickedView, int position) {
                final int clickedId = clickedView.getId();
                final String nameFile = ((TextView) itemView.findViewWithTag(position)).getText().toString() + ".gym";
                if (clickedId == R.id.itemList || clickedId == R.id.btn_Open) {
                    //Open routine
                    Intent i = new Intent(getActivity(), ReadFileActivity.class);
                    i.putExtra("nameFile", nameFile);
                    startActivity(i);
                }else if(clickedId == R.id.btn_Modify){
                    //Modify button clicked
                    Intent i = new Intent(getActivity(), DaysRoutineActivity.class);
                    i.putExtra("nameRoutine", nameFile);
                    i.putExtra("modify",true);
                    getActivity().startActivity(i);
                }else if(clickedId == R.id.btn_Share){
                    //Share button clicked
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                            + File.separator + "GymRoutines" + File.separator + nameFile)));
                    startActivity(Intent.createChooser(intent, "GymRoutines"));
                }else{
                    //The user choose delete then I put another alert saying if he is sure
                    AlertDialog.Builder deleteDialog =
                            new AlertDialog.Builder(getActivity());

                    deleteDialog.setMessage(getResources().getString(R.string.alert_message_delete_file))
                            .setTitle(nameFile)
                            .setPositiveButton(getResources().getString(R.string.alert_buttons_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    File deleteFile = new File(Environment.getExternalStorageDirectory()
                                            + File.separator + "GymRoutines" + File.separator + nameFile);
                                    if (deleteFile.delete()) {
                                        refreshRoutines();
                                        Toast.makeText(getActivity(), nameFile + getResources().getString(R.string.toast_delete_file), Toast.LENGTH_LONG).show();
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.alert_buttons_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    deleteDialog.create();
                    deleteDialog.show();
                }
            }
        },R.id.itemList, R.id.btn_Open, R.id.btn_Modify, R.id.btn_Share, R.id.btn_Delete);
        AdView adView = (AdView) rootView.findViewById(R.id.routines_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return rootView;
    }

}
