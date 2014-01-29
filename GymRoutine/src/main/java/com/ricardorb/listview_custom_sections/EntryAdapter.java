package com.ricardorb.listview_custom_sections;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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


                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //First, I need to take the resources from the default resources
                            //If I do not do this, will throw me errors about name of file
                            //Because muscle array is different in every language
                            Resources standardResources = mContext.getResources();
                            AssetManager assets = standardResources.getAssets();
                            DisplayMetrics metrics = standardResources.getDisplayMetrics();
                            Configuration config = new Configuration(standardResources.getConfiguration());
                            config.locale = Locale.UK;
                            Resources defaultResources = new Resources(assets, metrics, config);

                            //Taking the picture for that exercise
                            String[] muscles = defaultResources.getStringArray(R.array.array_muscles);
                            String mDrawableName = (muscles[ei.getNumMuscle()] + "_" + ei.getNumExercise()).toLowerCase();
                            int resID = standardResources.getIdentifier(mDrawableName, "drawable", mContext.getPackageName());
                            Drawable drawable = standardResources.getDrawable(resID);

                            Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_image);
                            ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                            image.setImageDrawable(drawable);
                            dialog.show();
                        }
                    });

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

                    v.setOnClickListener(new View.OnClickListener() {
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