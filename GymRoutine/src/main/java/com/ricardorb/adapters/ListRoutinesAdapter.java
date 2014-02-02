package com.ricardorb.adapters;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;


import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricardorb.gymroutine.R;

public class ListRoutinesAdapter extends BaseAdapter {
    private Context mContext;
    private String[] nameFilesDirectory;
    private File[] filesDirectory;
    Date[] dateFiles;

    File directory;

    public ListRoutinesAdapter(Context c) {
        mContext = c;
        // Reading files
        boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            try {
                directory = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "GymRoutines");

                if (!directory.exists()) {
                    if (directory.mkdir()) {
                        Toast.makeText(
                                mContext,
                                c.getResources().getString(R.string.alert_message_createFolder)
                                        + directory.getPath(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                filesDirectory = directory.listFiles();

                //Taking all the files in directory
                nameFilesDirectory = directory.list();

                //Counting how many files with .gym
                int j = 0;
                for (String i : nameFilesDirectory) {
                    if (i.endsWith(".gym")) {
                        j++;
                    }
                }

                //Taking the name of files with .gym in filter and the date
                String[] filter = new String[j];
                dateFiles = new Date[j];

                j = 0;
                for (String i : nameFilesDirectory) {
                    if (i.endsWith(".gym")) {
                        dateFiles[j] = new Date(filesDirectory[j].lastModified());
                        filter[j] = i;
                        j++;
                    }
                }


                //Cloning array
                nameFilesDirectory = filter.clone();
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(mContext, "Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(
                    mContext,
                    c.getResources().getString(R.string.alert_message_errorSD), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nameFilesDirectory.length;
    }

    @Override
    public Object getItem(int posi) {
        // TODO Auto-generated method stub
        return posi;
    }

    @Override
    public long getItemId(int posi) {
        // TODO Auto-generated method stub
        return posi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) vi.inflate(R.layout.list_item_routines_files, null);
        TextView nameFile = (TextView) ll.findViewById(R.id.nameFile);
        TextView dateFile = (TextView) ll.findViewById(R.id.lastModified);
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, mContext.getResources().getConfiguration().locale);

        nameFile.setText(nameFilesDirectory[position].substring(0,nameFilesDirectory[position].indexOf(".")));
        nameFile.setTag(position);
        dateFile.setText(df.format(dateFiles[position]));

        return ll;
    }

}
