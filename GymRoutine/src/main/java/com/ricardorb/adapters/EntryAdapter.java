package com.ricardorb.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricardorb.gymroutine.R;
import com.ricardorb.listview_custom_sections.EntryItem;
import com.ricardorb.listview_custom_sections.Item;
import com.ricardorb.listview_custom_sections.SectionItem;
import com.ricardorb.routines.DaysRoutineActivity;

import java.util.ArrayList;

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

        final Item item = items.get(position);
        if (item != null) {
            if (item.isSection()) {
                SectionItem si = (SectionItem) item;
                convertView = vi.inflate(R.layout.list_item_section, parent, false);

                convertView.setOnClickListener(null);
                convertView.setOnLongClickListener(null);
                convertView.setLongClickable(false);

                final TextView sectionView = (TextView) convertView.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());
            } else {
                if (openFile) {
                    final EntryItem ei = (EntryItem) item;
                    convertView = vi.inflate(R.layout.list_item_see, parent, false);
                    final TextView title = (TextView) convertView.findViewById(R.id.list_item_entry_title);
                    if (title != null) {
                        title.setText(ei.title);
                    }


                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Resources standardResources = mContext.getResources();

                            //Taking the picture for that exercise
                            final String[] muscles = mContext.getResources().getStringArray(R.array.array_muscles);
                            final String mDrawableName = (muscles[ei.getNumMuscle()] + "_" + ei.getNumExercise()).toLowerCase();
                            int resID = standardResources.getIdentifier(mDrawableName, "drawable", mContext.getPackageName());

                            final Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_image);

                            final ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                            dialog.setCanceledOnTouchOutside(true);

                            try {
                                Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), resID);
                                image.setImageBitmap(bm);
                            }catch (OutOfMemoryError ex){
                                final BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 8;
                                Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), resID, options);
                                image.setImageBitmap(bm);
                            }

                            dialog.show();
                        }
                    });

                } else {
                    final EntryItem ei = (EntryItem) item;
                    convertView = vi.inflate(R.layout.list_item_entry, parent, false);
                    final CheckBox sub_cb = (CheckBox) convertView.findViewById(R.id.cb_list_item_entry_summary);
                    final ImageView image  = (ImageView) convertView.findViewById(R.id.imageViewExercise);

                    final Resources standardResources = mContext.getResources();
                    //Taking the picture for that exercise
                    final String[] muscles = mContext.getResources().getStringArray(R.array.array_muscles);
                    final String mDrawableName = (muscles[ei.getNumMuscle()] + "_" + ei.getNumExercise()).toLowerCase();
                    final int resID = standardResources.getIdentifier(mDrawableName, "drawable", mContext.getPackageName());

                    try {
                        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), resID);
                        image.setImageBitmap(bm);
                    }catch (OutOfMemoryError ex){
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), resID, options);
                        image.setImageBitmap(bm);
                    }


                    if (sub_cb != null) {
                        sub_cb.setText(ei.title);
                    }

                    sub_cb.setChecked(checkeds[ei.getNumMuscle()][ei.getNumExercise()]);

                    sub_cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((DaysRoutineActivity) mContext).setCheckedExercises(ei.title, sub_cb.isChecked(), fragmentDay);
                        }
                    });
                }

            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView checkbox;
        View view;
    }
}