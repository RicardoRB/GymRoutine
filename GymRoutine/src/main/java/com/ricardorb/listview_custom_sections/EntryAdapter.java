package com.ricardorb.listview_custom_sections;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ricardorb.gymroutine.R;
import com.ricardorb.routines.DaysRoutineActivity;

public class EntryAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private LayoutInflater vi;
    private Context mContext;
    private int fragmentDay;
    private boolean openFile;
    private boolean checkeds[][] = null;

    public EntryAdapter(Context context, ArrayList<Item> items, boolean checkeds[][], int fragmentDay, boolean openFile) {
        super(context, 0, items);
        this.items = items;
        this.mContext = context;
        this.checkeds = checkeds.clone();
        this.fragmentDay = fragmentDay;
        this.openFile = openFile;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public EntryAdapter(Context context, ArrayList<Item> items, boolean openFile) {
        super(context, 0, items);
        this.items = items;
        this.mContext = context;
        this.openFile = openFile;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item item = items.get(position);
        if (item != null) {
            if (item.isSection()) {
                SectionItem si = (SectionItem) item;
                v = vi.inflate(R.layout.list_item_section, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());
            } else {
                if (openFile) {
                    final EntryItem ei = (EntryItem) item;
                    v = vi.inflate(R.layout.list_item_see, null);
                    final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
                    if (title != null) {
                        title.setText(ei.title);
                    }
                } else {
                    final EntryItem ei = (EntryItem) item;
                    v = vi.inflate(R.layout.list_item_entry, null);
                    final CheckBox sub_cb = (CheckBox) v.findViewById(R.id.cb_list_item_entry_summary);

                    if (sub_cb != null) {
                        sub_cb.setText(ei.title);
                    }
                    if (checkeds != null) {
                        sub_cb.setChecked(checkeds[ei.getNumMuscle()][ei.getNumExercise()]);
                    }

                    sub_cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((DaysRoutineActivity) mContext).setCheckedExercises(ei.title, sub_cb.isChecked(), fragmentDay);
                        }
                    });
                }

            }
        }
        return v;
    }

}
