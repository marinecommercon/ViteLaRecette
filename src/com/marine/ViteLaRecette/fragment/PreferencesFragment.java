package com.marine.ViteLaRecette.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Categorie;
import com.marine.ViteLaRecette.dao.Ingredient;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marine on 04/02/2015.
 */
public class PreferencesFragment extends Fragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateFavoriteRecipes();
        updateBannedRecipes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_preferences, container, false);

        buttonAddCategory = (Button) rootView.findViewById(R.id.buttonAddCategory);
        listviewBannedCategories = (ListView) rootView.findViewById(R.id.bannedCategoriesList);
        listviewFavoriteRecipes = (ListView) rootView.findViewById(R.id.shoplistRecipes);
        listviewBannedRecipes = (ListView) rootView.findViewById(R.id.bannedRecipesList);
        listviewBannedIngredients = (ListView) rootView.findViewById(R.id.bannedIngredientsList);

        initListview_BC();
        setOnclick_BC();
        initButton_GC();

        updateFavoriteRecipes();
        initFavoriteRecipes();

        updateBannedRecipes();
        initBannedRecipes();

        initListview_BI();
        initBannedIngredients();

        return rootView;
    }

    /******************************************************************************************************************
     * ****************************************************************************************************************
     * **********************************************CATEGORIES********************************************************
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */

    // Banned categories
    private void initListview_BC(){

        listBannedCategoriesItems = MainActivity.categorieDao.queryBuilder().where(com.marine.ViteLaRecette.dao.CategorieDao.Properties.Favoris.eq(-1)).list();
        listBannedCategories = new ArrayList<String>();

        for (int i = 0; i < listBannedCategoriesItems.size(); i++) {
            listBannedCategories.add(listBannedCategoriesItems.get(i).getNom());
        }

        bannedCategoriesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listBannedCategories);
        listviewBannedCategories.setAdapter(bannedCategoriesAdapter);
    }

    private void setOnclick_BC() {

        listviewBannedCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    private void initButton_GC(){

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createSpinner_GC(get_GC());

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    //Get categories names from database (<>-1)
    private List<Categorie> get_GC(){

        //List all banned categories
        newList = MainActivity.categorieDao.queryBuilder().where(com.marine.ViteLaRecette.dao.CategorieDao.Properties.Favoris.eq(0)).list();
        return newList;

    }

    //Create the spinner
    private void createSpinner_GC(List<Categorie> listCategoryItems){

        spinnerCategories = new Spinner(getActivity());
        listCategoryNames = new ArrayList<String>();

        for (int i = 0; i < listCategoryItems.size(); i++) {
            listCategoryNames.add(listCategoryItems.get(i).getNom());
        }

        spinnerCategoriesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listCategoryNames);
        spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(spinnerCategoriesAdapter);

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

                alert = new AlertDialog.Builder(getActivity()).create();
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

                                Fragment fragment;
                                FragmentManager fragmentManager;
                                Bundle bundle = new Bundle();

                                bundle.putInt("ID", (int) listFavoriteRecipes.get(arg2).getId().intValue());

                                fragment = new DetailRecipeFragment();
                                fragment.setArguments(bundle);

                                fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                            }
                        });

                alert.show();
            }
        });
    }

    private void updateFavoriteRecipes(){

        listFavoriteRecipes = MainActivity.recetteDao.queryBuilder().where(RecetteDao.Properties.Favoris.eq(1)).list();
        favoriteRecipesAdapter = new AdapterPersonalSearch(getActivity(), listFavoriteRecipes);
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

                alert = new AlertDialog.Builder(getActivity()).create();
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

                alert.setButton3("Détail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Fragment fragment;
                                FragmentManager fragmentManager;
                                Bundle bundle = new Bundle();

                                bundle.putInt("ID", (int) listBannedRecipes.get(arg2).getId().intValue());

                                fragment = new DetailRecipeFragment();
                                fragment.setArguments(bundle);

                                fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                            }
                        });

                alert.show();
            }
        });
    }

    private void updateBannedRecipes(){

        listBannedRecipes = MainActivity.recetteDao.queryBuilder().where(RecetteDao.Properties.Favoris.eq(-1)).list();
        bannedRecipesAdapter = new AdapterPersonalSearch(getActivity(), listBannedRecipes);
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
                        getActivity()).create();
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

        bannedIngredientsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listBannedIngredients);
        listviewBannedIngredients.setAdapter(bannedIngredientsAdapter);

    }

}
