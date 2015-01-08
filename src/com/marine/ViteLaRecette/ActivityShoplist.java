package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.marine.ViteLaRecette.adapter.AdapterCourses;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.adapter.Model;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

public class ActivityShoplist extends Activity implements AdapterView.OnItemClickListener {


    //initShoplist
    private List<Liste> recipesItems;
    private ArrayList<Recette> listRecipes;
    private AdapterCourses recipesAdapter;

    //setOnclick_Recipes()
    private ListView listviewRecipes;

    //getListIngredientsString
    ArrayList<Quantite> listQuantities;
    private ArrayList<String> listIngredientsString;


	private TextView textviewIngredients;
    private ListView mListView;
    private SharedPreferences prefs;

    private final String TAG = getClass().getSimpleName().toString();

    ArrayAdapter<Model> adapter;
    ArrayList<Model> list = new ArrayList<Model>();




    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init UI
        setContentView(R.layout.activity_shoplist);
        listviewRecipes = (ListView) findViewById(R.id.shoplistRecipes);
        mListView = (ListView) findViewById(R.id.shoplistlistViewDescriptionItemsID);

        //init Arraylist
        listIngredientsString = new ArrayList<String>();
        listQuantities = new ArrayList<Quantite>();

        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);


        loadRecipes();
        setOnclick_Recipes();
        getListIngredientsString();
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

    private void savePrefs(){

        for (int i = 0 ; i < list.size() ; i++){

            SharedPreferences.Editor e = prefs.edit();

            if(list.get(i).isSelected()==true){
                e.putBoolean("" + list.get(i).getName().hashCode(), true);

            }else{
                e.putBoolean("" + list.get(i).getName().hashCode(), false);
            }
            e.commit();
        }
    }


    private void restorePrefs(){

        for (int i = 0 ; i < list.size() ; i++){

            System.out.println("test"+ list.get(i).getName().hashCode() );

            if(prefs.getBoolean("" + list.get(i).getName().hashCode(), false)==true){
                list.get(i).setSelected(true);
            }
            if(prefs.getBoolean("" + list.get(i).getName().hashCode(), false)==false){
                list.get(i).setSelected(false);
            }
        }
    }

    private void loadRecipes(){

        recipesItems = MainActivity.listeDao.loadAll();
        listRecipes = new ArrayList<Recette>();

        for (int i = 0; i < recipesItems.size(); i++) {
            listRecipes.add(recipesItems.get(i).getRecette());
        }

        recipesAdapter = new AdapterCourses(this, listRecipes);
        listviewRecipes.setAdapter(recipesAdapter);

    }

    private void setOnclick_Recipes() {

        listviewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ActivityShoplist.this).create();

                alertDialog.setMessage("Retirer de la liste ?");

                alertDialog.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alertDialog.setButton2("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        list.clear();
                        listIngredientsString.clear();
                        MainActivity.listeDao.delete(recipesItems.get(arg2));
                        loadRecipes();
                        getListIngredientsString();
                        setAdapter(ActivityShoplist.this);

                    }
                });
                alertDialog.setButton3("Detail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent1 = new Intent(ActivityShoplist.this, ActivityDetailRecipe.class);
                                intent1.putExtra("ID", listRecipes.get(arg2).getId().intValue());
                                startActivity(intent1);
                            }
                        });

                alertDialog.show();
            }
        });
    }

    // Make Strings from quantities +ingredients + suffix
    private void getListIngredientsString(){

        int numberOfQuantities = 0;
        int numberOfPeopleDefault = 0;
        int numberOfPeopleChosen = 0;
        String calculatedQuantity = "";
        String suffix = "";

        int count = 0;

        for(int i=0; i<recipesItems.size() ; i++){
            listQuantities.addAll(MainActivity.quantiteDao._queryRecette_QuantDeRec(listRecipes.get(i).getId()));
        }

        for( int i=0; i<recipesItems.size() ; i++ ) {

            numberOfQuantities = recipesItems.get(i).getRecette().getQuantDeRec().size();
            numberOfPeopleDefault = recipesItems.get(i).getRecette().getNombre();
            numberOfPeopleChosen = recipesItems.get(i).getNombre();

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


    //Enable click on item as click on checkbox
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        CheckBox checkbox = (CheckBox) v.getTag(R.id.listCheck);
        int getPosition = (Integer) checkbox.getTag();

        if(list.get(getPosition).isSelected()==true){
            checkbox.setChecked(false);
        }else{
            checkbox.setChecked(true);
        }
    }


}
