package com.ricardorb.adapters;

import java.io.File;

import android.R;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListRoutinesAdapter extends BaseAdapter {
	private Context mContext;
	private String[] archivos = new String[0];

	private ImageView icon;
	private TextView text;
	LinearLayout ll;

	File directory;

	public ListRoutinesAdapter(Context c) {
		mContext = c;
		// archivos =
		//c.getResources().getStringArray(android.R.array.emailAddressTypes);
		// Funciones para leer los archivos
		boolean isSDPresent = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			try {
				directory = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "GymRoutines");

				if (!directory.exists()) {
					directory.mkdir();
					Toast.makeText(
							mContext,
							"Se ha creado una carpeta nueva en "
									+ directory.getPath(), Toast.LENGTH_SHORT)
							.show();
				}

				archivos = directory.list();

				int j = 0;
				for (int i = 0; i < archivos.length; i++) {
					if (archivos[i].endsWith(".gym")) {
						j++;
					}
				}

				String[] filtro = new String[j];
				j = 0;
				for (int i = 0; i < archivos.length; i++) {
					if (archivos[i].endsWith(".gym")) {
						filtro[j] = archivos[i];
						j++;
					}
				}
				archivos = filtro.clone();
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(mContext, "Error: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(
					mContext,
					"No hay sd ", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return archivos.length;
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
		ll = new LinearLayout(mContext);
		icon = new ImageView(mContext);
		text = new TextView(mContext);
		ll.setOrientation(LinearLayout.HORIZONTAL);

		// icon.setLayoutParams(new LinearLayout.LayoutParams(24, 24)); tama�o
		icon.setScaleType(ImageView.ScaleType.FIT_START); // escalar la imagen
		icon.setImageResource(R.drawable.ic_menu_upload);

		text.setGravity(Gravity.LEFT);
		text.setText(archivos[position]);
		ll.addView(icon);
		ll.addView(text);

		return ll;
	}

}
