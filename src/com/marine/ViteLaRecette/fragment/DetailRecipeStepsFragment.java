package com.marine.ViteLaRecette.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.adapter.Model;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marine on 04/02/2015.
 */
public class DetailRecipeStepsFragment extends ListFragment {


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recetteID = bundle.getInt("ID", 0);
            System.out.println("steps" + recetteID);
        }

        recipe = MainActivity.recetteDao.queryBuilder().where(RecetteDao.Properties.Id.eq(recetteID)).unique();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_detail_recipe_steps, container, false);
        mListView = (ListView) rootView.findViewById(R.id.detailListViewDescriptionItemsID);

        parserDescription((String) recipe.getDescription().toString().trim());
        adapter = new AdapterSteps(getActivity(),getModel(listDescription));
        mListView.setAdapter(adapter);

        return rootView;
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
    private List<Model> getModel(ArrayList<String> description) {

        for (int i = 0 ; i < description.size() ; i++){
            list.add(new Model(""+description.get(i)));
        }
        return list;
    }

    @Override
    public void onPause() {
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
    public void onResume() {
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }


}
