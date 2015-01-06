package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.QuantiteDao.Properties;
import com.marine.ViteLaRecette.dao.Recette;

public class ActivitySeeResults extends ListActivity {

    private String typeOfDish;
    private long ingredientAId;
    private long ingredientBId;
    private long ingredientCId;
    private Cursor cursor;
    private int flag;
    private ArrayList<Recette> listRecipes;
    private List<Quantite> listMatchingRecipes;
    private Spinner spinnerChangeOrder;
    private AdapterPersonalSearch adapter;
    private List<String> listOptions;
    private ArrayAdapter<String> adapterOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        typeOfDish = this.getIntent().getStringExtra("Type");
        ingredientAId = this.getIntent().getLongExtra("IngredientA", -1);
        ingredientBId = this.getIntent().getLongExtra("IngredientB", -1);
        ingredientCId = this.getIntent().getLongExtra("IngredientC", -1);

        setContentView(R.layout.activity_see_results);

        findRecettes();
        initListRecipes();

        if(listRecipes.size()==0) {
            Toast.makeText(getApplicationContext(), "Aucune recette n'a été trouvée", Toast.LENGTH_SHORT).show();
            finish();
        }

        else {
            initAdapter();
            fulfilSpinnerChangeOrder();
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        id = listRecipes.get(position).getId();
        Intent intent = new Intent(ActivitySeeResults.this,ActivityDetailRecipe.class);
        intent.putExtra("ID", (int) id);
        startActivity(intent);
    }


    // Main request (all recipes included preferences)
    private void findRecettes() {

        String MY_QUERY;

        if (typeOfDish.equals("Peu importe")) {

            //Query = Get the quantities to compare them with the ingredients
            MY_QUERY = "SELECT RECETTE._id " + "FROM RECETTE "
                    + "INNER JOIN QUANTITE "
                    + "ON RECETTE._id = QUANTITE.RECETTE_ID "
                    + "INNER JOIN INGREDIENT "
                    + "ON QUANTITE.INGREDIENT_ID = INGREDIENT._id "
                    + "AND RECETTE.FAVORIS <> -1 ";
        } else {

            MY_QUERY = "SELECT RECETTE._id " + "FROM RECETTE "
                    + "INNER JOIN QUANTITE "
                    + "ON RECETTE._id = QUANTITE.RECETTE_ID "
                    + "INNER JOIN INGREDIENT "
                    + "ON QUANTITE.INGREDIENT_ID = INGREDIENT._id "
                    + "WHERE RECETTE.TYPE = '" + typeOfDish + "' "
                    + "AND RECETTE.FAVORIS <> -1 ";
        }

        if ((ingredientAId >= 0) || (ingredientBId >= 0) || (ingredientCId >= 0)) {
            MY_QUERY = MY_QUERY + "AND QUANTITE.INGREDIENT_ID IN ("
                    + ingredientAId + "," + ingredientBId + "," + ingredientCId
                    + ") ";
        }

        MY_QUERY = MY_QUERY + "AND RECETTE._id NOT IN ( " + "SELECT RECETTE._id "
                + "FROM RECETTE " + "INNER JOIN QUANTITE "
                + "ON RECETTE._id = QUANTITE.RECETTE_ID "
                + "INNER JOIN INGREDIENT "
                + "ON QUANTITE.INGREDIENT_ID = INGREDIENT._id "
                + "INNER JOIN CATEGORIE "
                + "ON INGREDIENT.CATEGORIE_ID = CATEGORIE._id "
                + "WHERE CATEGORIE.FAVORIS = -1 "
                + "OR INGREDIENT.FAVORIS = -1 ) "
                + "GROUP BY RECETTE._id ";

        cursor = MainActivity.db.rawQuery(MY_QUERY, null);

    }

    private void initListRecipes(){

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

    public void fulfilSpinnerChangeOrder() {

        spinnerChangeOrder = (Spinner) findViewById(R.id.spinnerChangeOrder);
        listOptions = new ArrayList<String>();

        listOptions.add("Ranger par temps croissant");
        listOptions.add("Ranger par difficulté croissante");
        listOptions.add("Ranger par prix croissant");

        adapterOptions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listOptions);

        adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChangeOrder.setAdapter(adapterOptions);

        spinnerChangeOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                switch (position) {

                    case 0:
                        getScore('T');
                        break;

                    case 1:
                        getScore('D');
                        break;

                    case 2:
                        getScore('P');
                        break;

                    default:
                        getScore('T');
                        break;

                }

                Collections.sort(listRecipes, new Comparator<Recette>() {
                    @Override
                    public int compare(Recette recipe1, Recette recipe2) {
                        return recipe1.getScore().compareTo(recipe2.getScore());
                    }
                });

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }


    //Get the score for the matching with ingredients.
    private void getScore(int choice) {

        int scoreFinal = 0;
        int scoreIngredients = 0;
        int scoreTime = 0;
        int scoreDifficulty = 0;
        int scorePrice = 0;

        for (int i = 0; i < listRecipes.size(); i++) {
            scoreIngredients = 0;

        if ((ingredientAId >= 0) || (ingredientBId >= 0) || (ingredientCId >= 0)) {
            listMatchingRecipes = MainActivity.quantiteDao
                    .queryBuilder()
                    .whereOr(Properties.IngredientId.eq(ingredientAId),
                            Properties.IngredientId.eq(ingredientBId),
                            Properties.IngredientId.eq(ingredientCId)).list();



                for (int j = 0; j < listMatchingRecipes.size(); j++) {
                    if (listRecipes.get(i).getId() == listMatchingRecipes.get(j).getRecetteId()) {
                        scoreIngredients = scoreIngredients + 1;
                    }
                }
            }

            scoreTime = listRecipes.get(i).getPreparation() + listRecipes.get(i).getCuisson();
            scoreDifficulty = listRecipes.get(i).getDifficulte();
            scorePrice = listRecipes.get(i).getPrix();

            switch (choice) {

                case 'T':
                    scoreFinal = (3 - scoreIngredients) * 100 + scoreTime;
                    listRecipes.get(i).setScore(scoreFinal);
                    break;

                case 'D':
                    scoreFinal = (3 - scoreIngredients) * 100 + scoreDifficulty;
                    listRecipes.get(i).setScore(scoreFinal);
                    break;

                case 'P':
                    scoreFinal = (3 - scoreIngredients) * 100 + scorePrice;
                    listRecipes.get(i).setScore(scoreFinal);
                    break;
            }
        }
    }







}
