package com.marine.ViteLaRecette.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marine on 02/10/2014.
 */
public class AdapterSteps extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    boolean checkAll_flag = false;

    public AdapterSteps(Activity context, List<Model> list) {
        super(context, R.layout.list_itemcheck, list);
        this.context = context;
        this.list = list;

    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {

            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.list_itemcheck, null);

            viewHolder = new ViewHolder();

            viewHolder.text = (TextView) convertView.findViewById(R.id.listName);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.listCheck);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    list.get(getPosition).setSelected(buttonView.isChecked());
                }



            });

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.listName, viewHolder.text);
            convertView.setTag(R.id.listCheck, viewHolder.checkbox);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setTag(position);
        viewHolder.text.setText(list.get(position).getName());
        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        return convertView;
    }
}