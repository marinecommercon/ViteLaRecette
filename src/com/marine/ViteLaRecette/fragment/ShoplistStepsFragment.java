package com.marine.ViteLaRecette.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.adapter.Model;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marine on 05/02/2015.
 */

public class ShoplistStepsFragment extends Fragment {

    private ListView mListView;
    private int position;
    private int numberOfPeople;

    private Recette recipe;


    //getListIngredientsString
    List<Quantite> listQuantities;
    private ArrayList<String> listIngredientsString;

    private List<Liste> recipesItems;

    //setAdapter
    ArrayAdapter<Model> adapter;
    ArrayList<Model> list = new ArrayList<Model>();

    //Preferences
    private SharedPreferences prefs;
    private final String TAG = getClass().getSimpleName().toString();
    private String recipeTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init pref
        prefs = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        //Get extras
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("POS", 0);
            numberOfPeople = bundle.getInt("NB", 0);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_shoplist_steps, container, false);
        mListView = (ListView) rootView.findViewById(R.id.shoplistlistViewDescriptionItemsID);

        //init Arraylist
        listIngredientsString = new ArrayList<String>();
        listQuantities = new ArrayList<Quantite>();

        //Load the recipe
        recipesItems = MainActivity.listeDao.loadAll();
        recipe = recipesItems.get(position).getRecette();
        recipeTag = recipesItems.get(position).getTag();

        getListIngredientsString(numberOfPeople);
        setAdapter(getActivity());

        return rootView;
    }

    private void getListIngredientsString(int numberOfPeople){

        int numberOfQuantities = 0;
        int numberOfPeopleDefault = 0;
        int numberOfPeopleChosen = 0;
        String calculatedQuantity = "";
        String suffix = "";

        int count = 0;

        listQuantities = recipe.getQuantDeRec();
        numberOfQuantities = recipe.getQuantDeRec().size();
        numberOfPeopleDefault = recipe.getNombre();
        numberOfPeopleChosen = numberOfPeople;

        for (int j = 0; j < numberOfQuantities; j++) {

            calculatedQuantity = Integer.toString((int) Math.ceil((numberOfPeopleChosen * listQuantities.get(count).getQuantite())/numberOfPeopleDefault));
            suffix = "" + listQuantities.get(count).getSuffixe();

            if (listQuantities.get(count).getQuantite() == 0.0) {
                calculatedQuantity = "";
            }
            if (suffix.trim().equals("null")) {
                suffix = "";
            }

            listIngredientsString.add("" + calculatedQuantity + " " +
                    listQuantities.get(count).getMesure().getNom() + " " +
                    listQuantities.get(count).getIngredient().getNom() + " " +
                    suffix);

            count = count + 1;
        }
    }

    protected void setAdapter(Activity a) {
        adapter = new AdapterSteps(getActivity(),getModel(listIngredientsString));
        mListView.setAdapter(adapter);
    }

    //Prepare list for the adapter
    private List<Model> getModel(ArrayList<String> description) {

        for (int i = 0 ; i < description.size() ; i++){
            list.add(new Model(""+description.get(i)));
        }
        return list;
    }

    private void savePrefs(){

        for (int i = 0 ; i < list.size() ; i++){

            SharedPreferences.Editor e = prefs.edit();

            if(list.get(i).isSelected()==true){
                e.putBoolean("" + list.get(i).getName().hashCode() + "_" + recipeTag, true);

            }else{
                e.putBoolean("" + list.get(i).getName().hashCode() + "_" + recipeTag, false);
            }
            e.commit();
        }
    }


    private void restorePrefs(){

        for (int i = 0 ; i < list.size() ; i++){

            if(prefs.getBoolean("" + list.get(i).getName().hashCode() + "_" + recipeTag, false)==true){
                list.get(i).setSelected(true);
            }
            if(prefs.getBoolean("" + list.get(i).getName().hashCode() + "_" + recipeTag, false)==false){
                list.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savePrefs();
    }

    @Override
    public void onResume() {
        super.onResume();
        restorePrefs();
    }

}