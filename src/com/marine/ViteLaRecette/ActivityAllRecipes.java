package com.marine.ViteLaRecette;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;

/**
 * Created by Marine on 02/02/2015.
 */
public class ActivityAllRecipes extends ListActivity {


    private Cursor cursor;
    private String textColumn;
    private String orderBy;
    private AdapterPersonalSearch adapter;
    private ArrayList<Recette> listRecipes;
    private int position;
    private int globalHeight;
    private double letterHeight;
    private Recette recipe;
    private String[] alphabeticalList = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};






    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);




        addLetters();
        findRecipes();
        initList();
        initAdapter();
    }




    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        id = listRecipes.get(position).getId();
        Intent intent = new Intent(ActivityAllRecipes.this,ActivityDetailRecipe.class);
        intent.putExtra("ID", (int) id);
        startActivity(intent);
    }


    private void findRecipes(){


        String MY_QUERY;


        //Query = Get the quantities to compare them with the ingredients
        MY_QUERY = "SELECT RECETTE._id " + "FROM RECETTE "
                + "INNER JOIN QUANTITE "
                + "ON RECETTE._id = QUANTITE.RECETTE_ID "
                + "INNER JOIN INGREDIENT "
                + "ON QUANTITE.INGREDIENT_ID = INGREDIENT._id "
                + "AND RECETTE.FAVORIS <> -1 ";


        MY_QUERY = MY_QUERY + "AND RECETTE._id NOT IN ( " + "SELECT RECETTE._id "
                + "FROM RECETTE " + "INNER JOIN QUANTITE "
                + "ON RECETTE._id = QUANTITE.RECETTE_ID "
                + "INNER JOIN INGREDIENT "
                + "ON QUANTITE.INGREDIENT_ID = INGREDIENT._id "
                + "INNER JOIN CATEGORIE "
                + "ON INGREDIENT.CATEGORIE_ID = CATEGORIE._id "
                + "WHERE CATEGORIE.FAVORIS = -1 "
                + "OR INGREDIENT.FAVORIS = -1 ) "
                + "GROUP BY RECETTE._id "
                + "ORDER BY RECETTE.NOM COLLATE NOCASE ASC";


        cursor = MainActivity.db.rawQuery(MY_QUERY, null);
    }




    private void initList(){


        listRecipes = new ArrayList<Recette>();
        if (cursor.moveToFirst()) {
            do {
                listRecipes.add(MainActivity.recetteDao.load((long) cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    private void initAdapter(){
        adapter = new AdapterPersonalSearch(this, listRecipes);
        setListAdapter(adapter);
    }




    private void addLetters(){


        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        globalHeight = metrics.heightPixels;
        letterHeight = Math.floor((double) (globalHeight/27 * 10)) / 10;


        LinearLayout linearLayoutLetters = (LinearLayout) findViewById(R.id.linearLayoutLettersID);


        final TextView[] textViewLetter = new TextView[26];
        for(int i=0; i<26; i++) {
            textViewLetter[i] = new TextView(this);
            textViewLetter[i].setHeight((int) letterHeight);
            textViewLetter[i].setText(alphabeticalList[i].toString());
            textViewLetter[i].setTextSize(15);
            textViewLetter[i].setGravity(Gravity.CENTER_HORIZONTAL);
            textViewLetter[i].setTag(alphabeticalList[i].toString());
            linearLayoutLetters.addView(textViewLetter[i]);


            textViewLetter[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findPosition(v, null);
                    v.startAnimation(anim);






                }
            });


        }
    }




    private void findPosition(View v, String tag) {


        int i = 0;
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);


        while ( !listRecipes.get(i).getNom().substring(0, 1).equalsIgnoreCase(v.getTag().toString()) && i < listRecipes.size()-1) {
            i = i + 1;
        }


        if(listRecipes.get(listRecipes.size()-1).getNom().substring(0, 1).equalsIgnoreCase(v.getTag().toString())){
            getListView().setSelection(i);
        }
        else {


            if (i < listRecipes.size() - 1) {
                getListView().setSelection(i);


            }


            else{
                final Toast toast = Toast.makeText(getApplicationContext(), "Aucune recette pour cette lettre", Toast.LENGTH_SHORT);
                toast.show();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 500);


            }
        }


    }





}
