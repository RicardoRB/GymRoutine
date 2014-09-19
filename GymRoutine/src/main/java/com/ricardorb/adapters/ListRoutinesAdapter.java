package com.ricardorb.adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ricardorb.gymroutine.R;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class ListRoutinesAdapter extends BaseAdapter {
    private Context mContext;
    private String[] nameFilesDirectory;
    private File[] filesDirectory;
    private Date[] dateFiles;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

    private File directory;

    public ListRoutinesAdapter(Context c) {
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Reading files
        boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            try {
                directory = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "GymRoutines");

                if (!directory.exists()) {
                    directory.mkdir();
                }
                filesDirectory = directory.listFiles();

                //Taking all the files in directory
                nameFilesDirectory = directory.list();

                //Counting how many files with .gym
                int j = 0;
                for (String i : nameFilesDirectory) {
                    if (i.endsWith(".gym")) {
                        addItem(i.substring(0,i.indexOf(".")));
                        j++;
                    }
                }

                //Taking the date of files with .gym in filter
                dateFiles = new Date[j];

                j = 0;
                for (String i : nameFilesDirectory) {
                    if (i.endsWith(".gym")) {
                        dateFiles[j] = new Date(filesDirectory[j].lastModified());
                        j++;
                    }
                }

            } catch (Exception e) {
                Toast.makeText(mContext, "Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(
                    mContext,
                    c.getResources().getString(R.string.alert_message_errorSD), Toast.LENGTH_SHORT)
                    .show();
        }

        /*
        ****future feature****
        *
        addSeparatorItem("Internal");

        for(String i:mContext.getFilesDir().list()){
            addItem(i);
        }
        */

    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public int getCount() {
        return mData.size();
    }

    public String getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.expandable_list_item, null);
                    holder.textViewNam = (TextView) convertView.findViewById(R.id.nameFile);
                    holder.textViewSub = (TextView) convertView.findViewById(R.id.lastModified);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, mContext.getResources().getConfiguration().locale);
        holder.textViewNam.setText(mData.get(position));
        holder.textViewNam.setTag(position);
        holder.textViewSub.setText(df.format(dateFiles[position]));
        return convertView;
    }

    private static class ViewHolder {
        public TextView textViewNam;
        public TextView textViewSub;
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
