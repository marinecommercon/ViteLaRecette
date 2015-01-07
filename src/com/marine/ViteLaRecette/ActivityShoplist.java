package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.marine.ViteLaRecette.adapter.AdapterCourses;
import com.marine.ViteLaRecette.adapter.AdapterSteps;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

public class ActivityShoplist extends Activity implements OnClickListener {


    //initShoplist
    private List<Liste> recipesItems;
    private ArrayList<Recette> listRecipes;
    private AdapterCourses recipesAdapter;

    //setOnclick_Recipes()
    private ListView listviewRecipes;

    //getListQuantitiesId
    ArrayList<Quantite> listQuantities;
    ArrayList<String> listQuantitiesText;

	private TextView textviewIngredients;
    private ListView mListView;
    private ArrayList<String> listDescription;
    private ArrayList<HashMap<String,Object>> mList = new ArrayList<HashMap<String,Object>>();
    private SharedPreferences prefs;

    private final String TAG = getClass().getSimpleName().toString();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoplist);
        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);
        listviewRecipes = (ListView) findViewById(R.id.shoplistRecipes);
        textviewIngredients = (TextView) findViewById(R.id.textviewIngredients);

        initShoplist();
        setOnclick_Recipes();
        getListQuantitiesId();
        initCheckboxes(this);

    }



    private void initShoplist(){

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

                        MainActivity.listeDao.delete(recipesItems.get(arg2));
                        initShoplist();
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


    private void getListQuantitiesId(){


        listQuantities = new ArrayList<Quantite>();
        listQuantitiesText = new ArrayList<String>();
        listDescription = new ArrayList<String>();

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

                listDescription.add("" + calculatedQuantity + " " +
                        listQuantities.get(count).getMesure().getNom() + " " +
                        listQuantities.get(count).getIngredient().getNom() + " " +
                        suffix);



                count = count + 1;
            }





        }

        System.out.println("" + Math.abs(listDescription.hashCode()) );


    }



    private void initCheckboxes(Activity a) {

        mListView = (ListView) a.findViewById(R.id.listViewDescriptionItemsID);

        for (int i = 0; i < listDescription.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(AdapterSteps.KEY_LIST_TITLE, listDescription.get(i).toString().trim());
            map.put(AdapterSteps.KEY_LIST_CHECK, prefs.getBoolean("" + Math.abs(listDescription.hashCode()) + i, false));
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


        View o = mListView.getChildAt(position).findViewById(R.id.listCheck);

        if (cb.isChecked()) {
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean("" + Math.abs(listDescription.hashCode()) + position, true);
            e.commit();
        } else {
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean("" + Math.abs(listDescription.hashCode()) + position, false);
            e.commit();
        }
    }




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
