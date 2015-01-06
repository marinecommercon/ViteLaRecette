package com.marine.ViteLaRecette.adapter;

import java.util.List;

import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Recette;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterPersonalSearch extends BaseAdapter {

	List<Recette> liste;
	LayoutInflater inflater;

	public AdapterPersonalSearch(Context context, List<Recette> liste) {

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
			convertView = inflater.inflate(R.layout.item_adapter_personal_search, null);

			holder.nom = (TextView) convertView.findViewById(R.id.nom);
            holder.type = (TextView) convertView.findViewById(R.id.type);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.nom.setText(liste.get(position).getNom());
        holder.type.setText(liste.get(position).getType());

		return convertView;

	}

	private class ViewHolder {
		TextView nom;
        TextView type;
    }

}
