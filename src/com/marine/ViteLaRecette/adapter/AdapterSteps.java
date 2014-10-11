package com.marine.ViteLaRecette.adapter;

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
public class AdapterSteps extends SimpleAdapter {

    public final static String KEY_LIST_TITLE = "title";
    public final static String KEY_LIST_CHECK = "check";
    private LayoutInflater	mInflater;

    public AdapterSteps(Context context, List<? extends Map<String, ?>> data,
                        int resource, String[] from, int[] to)
    {
        super (context, data, resource, from, to);
        mInflater = LayoutInflater.from (context);

    }

    @Override
    public Object getItem (int position)
    {
        return super.getItem (position);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate (R.layout.list_itemcheck, null);
            CheckBox cb = (CheckBox) convertView.findViewById (R.id.listCheck);
            cb.setTag (position);
        }
        return super.getView (position, convertView, parent);
    }

}