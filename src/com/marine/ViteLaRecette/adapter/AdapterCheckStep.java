package com.marine.ViteLaRecette.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mcommercon on 30/09/14.
 */
public class AdapterCheckStep extends SimpleAdapter {

    public final static String KEY_LIST_STEP = "title";
    public final static String KEY_LIST_CHECK = "check";

    List<HashMap<String, Object>> map;
    String[] from;
    int layout;
    int[] to;
    Context context;
    LayoutInflater mInflater;
    public AdapterCheckStep(Context context, List<HashMap<String, Object>> data,
                            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        layout = resource;
        map = data;
        this.from = from;
        this.to = to;
        this.context = context;
    }

    public List<HashMap<String, Object>> getMap() {
        return map;
    }


    /* (non-Javadoc)
     * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return this.createViewFromResource(position, convertView, parent, layout);
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @param resource
     * @return
     */
    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        this.bindView(position, v);

        return v;
    }


    /**
     * @param position
     * @param view
     */
    private void bindView(int position, View view) {
        @SuppressWarnings("rawtypes")
        final Map dataSet = map.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = super.getViewBinder();
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else if (v instanceof TextView) {
                            // Note: keep the instanceof TextView check at the bottom of these
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                            setViewText((TextView) v, text);
                        } else if (v instanceof ImageView) {
                            if (data instanceof Integer) {
                                setViewImage((ImageView) v, (Integer) data);
                            }
                        }
                        else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " +
                                    (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else if (data instanceof Bitmap){
                            setViewImage((ImageView) v, (Bitmap)data);
                        } else {
                            setViewImage((ImageView) v, text);
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
    }



    /**
     * @param v
     * @param bmp
     */
    private void setViewImage(ImageView v, Bitmap bmp){
        v.setImageBitmap(bmp);
    }



}
