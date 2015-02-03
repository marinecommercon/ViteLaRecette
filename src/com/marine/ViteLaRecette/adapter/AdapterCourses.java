package com.marine.ViteLaRecette.adapter;

import java.util.List;

import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Recette;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterCourses extends BaseAdapter {

	private static final Context Context = null;
	List<Liste> liste;
	LayoutInflater inflater;

	public AdapterCourses(Context context, List<Liste> liste) {

		inflater = LayoutInflater.from(context);

		this.liste = liste;

	}

	@Override
	public int getCount() {
		return liste.size();
	}

	@Override
	public Object getItem(int position) {
		return liste.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.item_courses, null);

			holder.nom_shoplist = (TextView) convertView.findViewById(R.id.nom_shoplist);
            holder.nb_shoplist = (TextView) convertView.findViewById(R.id.nb_shoplist);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

        if(!(liste.get(position).getRecette().getNom().equals(null))) {
            holder.nom_shoplist.setText(liste.get(position).getRecette().getNom());

        }


        if(!(liste.get(position).getNombre().toString().equals(null))) {
            holder.nb_shoplist.setText("pour " + liste.get(position).getNombre().toString() + " personnes");

        }


		return convertView;

	}

	private class ViewHolder {

		TextView nom_shoplist;
        TextView nb_shoplist;

	}

}
