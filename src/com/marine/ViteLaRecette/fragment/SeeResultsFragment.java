package com.marine.ViteLaRecette.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.ActivityDetailRecipe;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.QuantiteDao;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Marine on 04/02/2015.
 */
public class SeeResultsFragment extends ListFragment {

    private String typeOfDish;
    private long ingredientAId;
    private long ingredientBId;
    private long ingredientCId;
    private Cursor cursor;
    private ArrayList<Recette> listRecipes;
    private List<Quantite> listMatchingRecipes;
    private Spinner spinnerChangeOrder;
    private AdapterPersonalSearch adapter;
    private List<String> listOptions;
    private ArrayAdapter<String> adapterOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            typeOfDish = bundle.getString("Type");
            ingredientAId = bundle.getLong("IngredientA", -1);
            ingredientBId = bundle.getLong("IngredientB", -1);
            ingredientCId = bundle.getLong("IngredientC", -1);
        }

        findRecipes();
        initList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Fragment fragment;
        FragmentManager fragmentManager;
        Bundle bundle = new Bundle();

        id = listRecipes.get(position).getId();
        bundle.putInt("ID", (int) id);

        fragment = new DetailRecipeFragment();
        fragment.setArguments(bundle);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_see_results, container, false);
        spinnerChangeOrder = (Spinner) rootView.findViewById(R.id.spinnerChangeOrder);

        if(listRecipes.size()==0) {
            Toast.makeText(getActivity().getApplicationContext(), "Aucune recette n'a été trouvée", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        else {
            initAdapter();
            fulfilSpinnerChangeOrder();
        }


        return rootView;
    }


    private void findRecipes() {

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
        adapter = new AdapterPersonalSearch(getActivity(), listRecipes);
        setListAdapter(adapter);
    }

    public void fulfilSpinnerChangeOrder() {

        listOptions = new ArrayList<String>();

        listOptions.add("Ranger par temps croissant");
        listOptions.add("Ranger par difficulté croissante");
        listOptions.add("Ranger par prix croissant");

        adapterOptions = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listOptions);

        adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChangeOrder.setAdapter(adapterOptions);

        spinnerChangeOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
                        .whereOr(QuantiteDao.Properties.IngredientId.eq(ingredientAId),
                                QuantiteDao.Properties.IngredientId.eq(ingredientBId),
                                QuantiteDao.Properties.IngredientId.eq(ingredientCId)).list();



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
