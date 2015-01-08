package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.adapter.Model;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao.Properties;

public class ActivityDetailRecipeSteps extends Activity implements AdapterView.OnItemClickListener {

    private Recette recipe;
    private final String TAG = getClass().getSimpleName().toString();
    private ArrayList<HashMap<String,Object>> mList = new ArrayList<HashMap<String,Object>>();
    private SharedPreferences prefs;
    private int recetteID = 0;
    private  ArrayList <String> listDescription;
    private ListView mListView;

    ArrayAdapter<Model> adapter;
    ArrayList<Model> list = new ArrayList<Model>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_recipe_steps);
        mListView = (ListView) findViewById(R.id.detailListViewDescriptionItemsID);
        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);

        initListDescription(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (int i = 0 ; i < list.size() ; i++){

            SharedPreferences.Editor e = prefs.edit();

            if(list.get(i).isSelected()==true){
                e.putBoolean("" + recetteID + i, true);
            }else{
                e.putBoolean("" + recetteID + i, false);
            }
            e.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0 ; i < list.size() ; i++){

            if(prefs.getBoolean("" + recetteID + i, false)==true){
                list.get(i).setSelected(true);
            }
            if(prefs.getBoolean("" + recetteID + i, false)==false){
                list.get(i).setSelected(false);
            }
        }
    }



    protected void initListDescription(Activity a) {

        recetteID = this.getIntent().getIntExtra("ID", 0);
        recipe = MainActivity.recetteDao.queryBuilder().where(Properties.Id.eq(recetteID)).unique();

        parserDescription((String) recipe.getDescription().toString().trim());

        adapter = new AdapterSteps(this,getModel(listDescription));
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }


     @Override
     public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

        CheckBox checkbox = (CheckBox) v.getTag(R.id.listCheck);
        int getPosition = (Integer) checkbox.getTag();

         if(list.get(getPosition).isSelected()==true){
             checkbox.setChecked(false);
         }else{
             checkbox.setChecked(true);
         }
    }



    private List<Model> getModel(ArrayList<String> description) {

        for (int i = 0 ; i < description.size() ; i++){
            list.add(new Model(""+description.get(i)));
        }
        return list;
    }

    private void parserDescription(String description){

        listDescription = new ArrayList<String>();
        String itemDescription;
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

}