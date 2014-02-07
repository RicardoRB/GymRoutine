package com.ricardorb.gymroutine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ricardorb.adapters.ListRoutinesAdapter;
import com.ricardorb.routines.AddRoutineActivity;

import java.io.File;

public class RoutinesActivity extends Fragment {
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
            case R.id.action_refresh_routines:
                ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity());
                lv.setAdapter(adapter);
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_refresh_files), Toast.LENGTH_LONG).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_routines, container, false);
        lv = (ListView) rootView.findViewById(R.id.listRoutines);
        AdView adView = (AdView)rootView.findViewById(R.id.routines_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (view.findViewWithTag(position) != null) {
                    final String nameFile = ((TextView) view.findViewWithTag(position)).getText().toString() + ".gym";
                    Intent i = new Intent(getActivity(), ReadFileActivity.class);
                    i.putExtra("nameFile", nameFile);
                    startActivity(i);
                }
            }
        });
        //I create a AlertDialog with options when the user do a long click in the item
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, final View v, final int position, long id) {
                // TODO Auto-generated method stub
                if (v.findViewWithTag(position) != null) {
                    AlertDialog.Builder options =
                            new AlertDialog.Builder(getActivity());
                    String[] items = getResources().getStringArray(R.array.array_options_files);
                    options.setTitle(getResources().getString(R.string.alert_title_options_files)).setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            final String nameFile = ((TextView) v.findViewWithTag(position)).getText().toString() + ".gym";
                            if (item <= 0) {
                                Intent i = new Intent(getActivity(), ReadFileActivity.class);
                                i.putExtra("nameFile", nameFile);
                                startActivity(i);
                            } else if (item == 1) {
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
                                                    ListRoutinesAdapter adapter = new ListRoutinesAdapter(getActivity());
                                                    lv.setAdapter(adapter);
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
                            } else {
                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                                        + File.separator + "GymRoutines" + File.separator + nameFile)));
                                startActivity(Intent.createChooser(intent, "GymRoutines"));

                            }

                        }
                    });
                    options.create();
                    options.show();
                }
                return false;
            }
        });
        return rootView;
    }
}
