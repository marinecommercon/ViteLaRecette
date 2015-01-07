package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Categorie;
import com.marine.ViteLaRecette.dao.Ingredient;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;

public class ActivityPreferences extends Activity {

	private Button buttonAddCategory;

    //get_GC
    private List<Categorie> newList;

    //createSpinner_GC
    private List<String> listCategoryNames;
    private Spinner spinnerCategories;
    private ArrayAdapter<String> spinnerCategoriesAdapter;

    //initListview_BC
    private ListView listviewBannedCategories;
    private List<Categorie> listBannedCategoriesItems;
    private ArrayList<String> listBannedCategories;
	private ArrayAdapter<String> bannedCategoriesAdapter;

    //initFavoriteRecipes
    private ListView listviewFavoriteRecipes;
    private AdapterPersonalSearch favoriteRecipesAdapter;
    private List<Recette> listFavoriteRecipes;

    //initBannedRecipes
    private ListView listviewBannedRecipes;
    private AdapterPersonalSearch bannedRecipesAdapter;
    private List<Recette> listBannedRecipes;

    //initListview_BI
    private ListView listviewBannedIngredients;
    private List<Ingredient> listBannedIngredientsItems;
    private ArrayList<String> listBannedIngredients;
    private ArrayAdapter<String> bannedIngredientsAdapter;

    private AlertDialog alert;






    @Override
    protected void onResume() {
        super.onResume();
        updateFavoriteRecipes();
        updateBannedRecipes();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);

        buttonAddCategory = (Button) findViewById(R.id.buttonAddCategory);
        listviewBannedCategories = (ListView) findViewById(R.id.bannedCategoriesList);
        listviewFavoriteRecipes = (ListView) findViewById(R.id.favoriteRecipesList);
        listviewBannedRecipes = (ListView) findViewById(R.id.bannedRecipesList);
        listviewBannedIngredients = (ListView) findViewById(R.id.bannedIngredientsList);

        initListview_BC();
        setOnclick_BC();
        initButton_GC();

        updateFavoriteRecipes();
        initFavoriteRecipes();

        updateBannedRecipes();
        initBannedRecipes();

        initListview_BI();
        initBannedIngredients();
	}


    /******************************************************************************************************************
     * ****************************************************************************************************************
     * **********************************************CATEGORIES********************************************************
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */


	private void initButton_GC(){

		buttonAddCategory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                createSpinner_GC(get_GC());

                AlertDialog alertDialog = new AlertDialog.Builder(ActivityPreferences.this).create();
                alertDialog.setMessage("Bannir catégorie : ");
                alertDialog.setView(spinnerCategories);

                alertDialog.setButton("Retour",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alertDialog.setButton2("Confirmer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Find the selected in the newList (get_GC)
                                Categorie categorie = get_GC().get(spinnerCategories.getSelectedItemPosition());
                                categorie.setFavoris(-1);
                                MainActivity.categorieDao.update(categorie);

                                //The listView of BC will change
                                initListview_BC();
                            }
                        });

                alertDialog.show();

            }
        });
    }

    //Create the spinner
    private void createSpinner_GC(List<Categorie> listCategoryItems){

        spinnerCategories = new Spinner(ActivityPreferences.this);
        listCategoryNames = new ArrayList<String>();

        for (int i = 0; i < listCategoryItems.size(); i++) {
            listCategoryNames.add(listCategoryItems.get(i).getNom());
        }


        spinnerCategoriesAdapter = new ArrayAdapter<String>(ActivityPreferences.this,android.R.layout.simple_spinner_item, listCategoryNames);
        spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(spinnerCategoriesAdapter);

    }

    //Get categories names from database (<>-1)
    private List<Categorie> get_GC(){

        //List all banned categories
        newList = MainActivity.categorieDao.queryBuilder().where(com.marine.ViteLaRecette.dao.CategorieDao.Properties.Favoris.eq(0)).list();

        return newList;

    }

    // Banned categories
    private void initListview_BC(){

    listBannedCategoriesItems = MainActivity.categorieDao.queryBuilder().where(com.marine.ViteLaRecette.dao.CategorieDao.Properties.Favoris.eq(-1)).list();
    listBannedCategories = new ArrayList<String>();

        for (int i = 0; i < listBannedCategoriesItems.size(); i++) {
            listBannedCategories.add(listBannedCategoriesItems.get(i).getNom());
        }

        bannedCategoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listBannedCategories);
        listviewBannedCategories.setAdapter(bannedCategoriesAdapter);
    }

    private void setOnclick_BC() {

        listviewBannedCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityPreferences.this).create();
                alertDialog.setMessage("Retirer de la liste ?");

                alertDialog.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alertDialog.setButton2("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                listBannedCategoriesItems.get(arg2).setFavoris(0);
                                MainActivity.categorieDao.update(listBannedCategoriesItems.get(arg2));

                                initListview_BC();
                            }
                        });

                alertDialog.show();
            }
        });
    }


    /******************************************************************************************************************
     * ****************************************************************************************************************
     * **********************************************FAVOURITE RECIPES*************************************************
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */

    private void initFavoriteRecipes(){

        listviewFavoriteRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                alert = new AlertDialog.Builder(ActivityPreferences.this).create();
                alert.setMessage("Retirer de la liste ?");

                alert.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alert.setButton2("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                listFavoriteRecipes.get(arg2).setFavoris(0);
                                MainActivity.recetteDao.update(listFavoriteRecipes.get(arg2));

                                updateFavoriteRecipes();
                            }
                        });

                alert.setButton3("Detail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent1 = new Intent(ActivityPreferences.this, ActivityDetailRecipe.class);
                                intent1.putExtra("ID", listFavoriteRecipes.get(arg2).getId().intValue());
                                startActivity(intent1);
                            }
                        });

                alert.show();
            }
        });
    }


    private void updateFavoriteRecipes(){

        listFavoriteRecipes = MainActivity.recetteDao.queryBuilder().where(RecetteDao.Properties.Favoris.eq(1)).list();
        favoriteRecipesAdapter = new AdapterPersonalSearch(this, listFavoriteRecipes);
        listviewFavoriteRecipes.setAdapter(favoriteRecipesAdapter);

    }


    /******************************************************************************************************************
     * ****************************************************************************************************************
     * **********************************************BANNED RECIPES****************************************************
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */

    private void initBannedRecipes(){

        listviewBannedRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                alert = new AlertDialog.Builder(ActivityPreferences.this).create();
                alert.setMessage("Retirer de la liste ?");

                alert.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alert.setButton2("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                listBannedRecipes.get(arg2).setFavoris(0);
                                MainActivity.recetteDao.update(listBannedRecipes.get(arg2));

                                updateBannedRecipes();
                            }
                        });

                alert.setButton3("Detail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent1 = new Intent(ActivityPreferences.this, ActivityDetailRecipe.class);
                                intent1.putExtra("ID", listBannedRecipes.get(arg2).getId().intValue());
                                startActivity(intent1);
                            }
                        });

                alert.show();
            }
        });
    }

    private void updateBannedRecipes(){

        listBannedRecipes = MainActivity.recetteDao.queryBuilder().where(RecetteDao.Properties.Favoris.eq(-1)).list();
        bannedRecipesAdapter = new AdapterPersonalSearch(this, listBannedRecipes);
        listviewBannedRecipes.setAdapter(bannedRecipesAdapter);

    }

    /******************************************************************************************************************
     * ****************************************************************************************************************
     * **********************************************BANNED INGREDIENTS************************************************
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */

    private void initBannedIngredients(){

        listviewBannedIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ActivityPreferences.this).create();
                alertDialog.setMessage("Retirer de la liste ?");
                alertDialog.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                alertDialog.setButton2("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                listBannedIngredientsItems.get(arg2).setFavoris(0);
                                MainActivity.ingredientDao.update(listBannedIngredientsItems.get(arg2));

                                initListview_BI();
                            }
                        });

                alertDialog.show();
            }
        });



    }

    private void initListview_BI(){

        listBannedIngredientsItems = MainActivity.ingredientDao.queryBuilder().where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Favoris.eq(-1)).list();
        listBannedIngredients = new ArrayList<String>();

        for (int i = 0; i < listBannedIngredientsItems.size(); i++) {
            listBannedIngredients.add(listBannedIngredientsItems.get(i).getNom());
        }

        bannedIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listBannedIngredients);
        listviewBannedIngredients.setAdapter(bannedIngredientsAdapter);

    }

}
