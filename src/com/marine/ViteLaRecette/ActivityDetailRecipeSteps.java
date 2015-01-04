package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao.Properties;

public class ActivityDetailRecipeSteps extends Activity{

    private Recette recipe;
    private final String TAG = getClass().getSimpleName().toString();
    private ArrayList<HashMap<String,Object>> mList = new ArrayList<HashMap<String,Object>>();
    private SharedPreferences prefs;
    private int recetteID = 0;
    private  ArrayList <String> listDescription;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_steps);
        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);
        initUI(this);
    }


    protected void initUI(Activity a) {
        recetteID = this.getIntent().getIntExtra("ID", 0);
        recipe = MainActivity.recetteDao.queryBuilder()
                .where(Properties.Id.eq(recetteID))
                .unique();


        //Initialization
        parserDescription((String) recipe.getDescription().toString().trim());
        initCheckboxes(this);
    }


    private void parserDescription(String description){

        String itemDescription;
        listDescription = new ArrayList<String>();
        int cursorDescription = 0;

        while (description.length()>0){

            if(cursorDescription < description.length()) {

                if (description.substring(cursorDescription, cursorDescription + 1).equals(".")) {

                    itemDescription = description.substring(0, cursorDescription + 1);
                    listDescription.add(itemDescription);

                    //Delete the item from the Description
                    description = description.substring(cursorDescription + 1, description.length());

                    //Reinitialize the cursor
                    cursorDescription = 0;

                }//An item has been added

                else {
                    cursorDescription = cursorDescription + 1;
                }

            }
            else{

                itemDescription = description.substring(0, cursorDescription);
                listDescription.add(itemDescription);
                description="";
            }//Last item of the description
        }//End of the description

        for(int i=0 ; i<listDescription.size() ; i++){
            listDescription.remove(".");

        }

    }

    private void initCheckboxes(Activity a) {

        mListView = (ListView) a.findViewById(R.id.listViewDescriptionItemsID);

        for (int i = 0; i < listDescription.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(AdapterSteps.KEY_LIST_TITLE, listDescription.get(i).toString().trim());
            map.put(AdapterSteps.KEY_LIST_CHECK, prefs.getBoolean("" + recetteID + i, false));
            mList.add(map);
        }

        String[] from = new String[]{AdapterSteps.KEY_LIST_TITLE, AdapterSteps.KEY_LIST_CHECK};
        int[] to = new int[]{R.id.listName, R.id.listCheck};

        mListView.setAdapter(
                new AdapterSteps(this, mList, R.layout.list_itemcheck, from, to)
        );
    }


    public void MyHandler(View v) {
        CheckBox cb = (CheckBox) v;
        int position = Integer.parseInt(cb.getTag().toString());


        View o = mListView.getChildAt(position).findViewById(
                R.id.listCheck);

        if (cb.isChecked()) {
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean("" + recetteID + position, true);
            e.commit();
        } else {
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean("" + recetteID + position, false);
            e.commit();
        }
    }
}