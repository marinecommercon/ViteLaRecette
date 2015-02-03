package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.adapter.Model;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marine on 03/02/2015.
 */
public class ActivityShoplistSteps extends Activity implements AdapterView.OnItemClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init UI
        setContentView(R.layout.activity_shoplist_steps);
        mListView = (ListView) findViewById(R.id.shoplistlistViewDescriptionItemsID);

        //init Arraylist
        listIngredientsString = new ArrayList<String>();
        listQuantities = new ArrayList<Quantite>();

        //init pref

        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);

        //Get extras
        position = this.getIntent().getIntExtra("POS", 0);
        numberOfPeople = this.getIntent().getIntExtra("NB", 0);

        //Load the recipe
        recipesItems = MainActivity.listeDao.loadAll();
        recipe = recipesItems.get(position).getRecette();
        recipeTag = recipesItems.get(position).getTag();

        getListIngredientsString(numberOfPeople);
        setAdapter(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restorePrefs();
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
        adapter = new AdapterSteps(this,getModel(listIngredientsString));
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckBox checkbox = (CheckBox) view.getTag(R.id.listCheck);
        int getPosition = (Integer) checkbox.getTag();

        if(list.get(getPosition).isSelected()==true){
            checkbox.setChecked(false);
        }else{
            checkbox.setChecked(true);
        }

    }
}
