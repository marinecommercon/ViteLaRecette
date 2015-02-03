package com.marine.ViteLaRecette.adapter;

import java.util.ArrayList;
import java.util.List;

import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterIngredients extends BaseAdapter {

	private static final Context Context = null;
	ArrayList<Quantite> liste;
	LayoutInflater inflater;

	public AdapterIngredients(Context context, ArrayList<Quantite> quantites) {

		inflater = LayoutInflater.from(context);

		this.liste = quantites;

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

			convertView = inflater.inflate(R.layout.item_ingredients, null);

			holder.nom = (TextView) convertView.findViewById(R.id.nom_ingredients);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		holder.nom.setText(liste.get(position).getIngredient().getNom());

		return convertView;

	}

	private class ViewHolder {

		TextView nom;

	}

}
