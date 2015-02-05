package com.marine.ViteLaRecette.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.ActivityAddRecipeStep3;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Categorie;
import com.marine.ViteLaRecette.dao.Ingredient;
import com.marine.ViteLaRecette.dao.Mesure;
import com.marine.ViteLaRecette.dao.Quantite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Marine on 05/02/2015.
 */
public class AddRecipeStep2Fragment extends Fragment {

    private Button buttonNext;
    private Button buttonAdd;

    private EditText edittextQuantity;
    private AutoCompleteTextView autoUnit;
    private AutoCompleteTextView autoIngredient;

    private Ingredient newIngredient;
    private ArrayList<String> listAutoIngredients;
    private List<Ingredient> listRequestIngredients;
    private ArrayAdapter<String> adapterIngredients;

    private Mesure newUnit;
    private ArrayList<String> listAutoUnits;
    private List<Mesure> listRequestUnits;
    private ArrayAdapter<String> adapterUnits;

    private ListView listviewIngredients;
    private ArrayList<String> listFullIngredients;
    private ArrayAdapter adapterListviewIngredients;

    private Mesure requestUnit;

    private ArrayList<String> dbListQuantities;
    private ArrayList<String> dbListUnits;
    private ArrayList<String> dbListIngredients;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_add_recipe_step2, container, false);
        buttonNext = (Button) rootView.findViewById(R.id.buttonNext);
        buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        edittextQuantity = (EditText) rootView.findViewById(R.id.edittextQuantity);
        autoUnit = (AutoCompleteTextView) rootView.findViewById(R.id.autoUnit);
        autoIngredient = (AutoCompleteTextView)rootView.findViewById(R.id.autoIngredient);
        listviewIngredients = (ListView) rootView.findViewById(R.id.listviewIngredients);

        initUI(getActivity());

        return rootView;
    }


    protected void initUI(Activity a) {

        //Init buttons
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(dbListIngredients.size()==0){
                    alertIngredientMissing();}else {

                    addPreferences();

                    Fragment fragment;
                    FragmentManager fragmentManager;

                    fragment = new AddRecipeStep3Fragment();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickButtonIngredient();

            }
        });


        //init the elements of the UI
        edittextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);


        //for the auto complete
        listAutoUnits = new ArrayList();
        listAutoIngredients = new ArrayList();


        //for the listview
        listFullIngredients = new ArrayList();
        adapterListviewIngredients = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listFullIngredients);
        listviewIngredients.setAdapter(adapterListviewIngredients);

        //for the preferences & database
        dbListQuantities = new ArrayList<String>();
        dbListUnits = new ArrayList<String>();
        dbListIngredients = new ArrayList<String>();

        //preparation of the lists for the auto complete edit text
        initUnits();
        initIngredients();

        //init the listview and its alertdialog to delete items
        listviewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

                AlertDialog alertDialogDeleteItem = new AlertDialog.Builder(getActivity()).create();

                alertDialogDeleteItem.setMessage("Retirer de la liste ?");
                alertDialogDeleteItem.setButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialogDeleteItem.setButton2("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listFullIngredients.remove(arg2);
                                adapterListviewIngredients.notifyDataSetChanged();
                            }
                        });

                alertDialogDeleteItem.show();
            }
        });





    }


    private void clickButtonIngredient() {

        newIngredient = MainActivity.ingredientDao.queryBuilder().where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Nom.eq(autoIngredient.getText())).unique();

        if (autoIngredient.length() == 0) {
            newIngredient = null;
            alertIngredientMissing();
        }


        //Add an new newIngredient
        else if (newIngredient == null) {

            AlertDialog alertDialogNewIngredient = new AlertDialog.Builder(getActivity()).create();
            alertDialogNewIngredient.setMessage("L'ingrédient n'existe pas, l'ajouter ? ");

            final Spinner spinnerCategory = new Spinner(getActivity());

            List<String> listCategory = new ArrayList<String>();
            final List<Categorie> listRequestCategory = MainActivity.categorieDao.queryBuilder().list();
            for (int i = 0; i < listRequestCategory.size(); i++) {
                listCategory.add(listRequestCategory.get(i).getNom());
            }

            //init the spinner of categories
            ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listCategory);
            adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapterCategory);

            alertDialogNewIngredient.setView(spinnerCategory);

            alertDialogNewIngredient.setButton("Retour",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            //confirm the new ingredient, add full ingredient in the database
            alertDialogNewIngredient.setButton2("Confirmer",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Categorie selectedCategory = listRequestCategory.get(spinnerCategory.getSelectedItemPosition());

                            MainActivity.db.beginTransaction();
                            try {
                                MainActivity.ingredientDao.insert(new Ingredient(null, autoIngredient.getText().toString(), 0, selectedCategory.getId()));
                                MainActivity.db.setTransactionSuccessful();
                            } finally {
                                MainActivity.db.endTransaction();
                            }

                            initIngredients();
                            addFullIngredient();
                        }
                    });
            alertDialogNewIngredient.show();
        }

        else {
            addFullIngredient();
        }
    }

    private void addFullIngredient(){

        //handle quantity
        if(edittextQuantity.getText().length()==0){
            dbListQuantities.add("0");
        }
        else{
            //dbIngredient.setQuantite((float) Integer.parseInt(edittextQuantity.getText().toString()));
            dbListQuantities.add(edittextQuantity.getText().toString());
        }


        //handle unit
        if (autoUnit.length() == 0) {
            listFullIngredients.add(edittextQuantity.getText().toString() + " " + autoIngredient.getText().toString());
            //dbIngredient.setMesureId((long) 0);
            dbListUnits.add("0");
        }

        else{
            //search the unit in the database
            requestUnit = MainActivity.mesureDao.queryBuilder().where(com.marine.ViteLaRecette.dao.MesureDao.Properties.Nom.eq(autoUnit.getText())).unique();

            //if no unit in the database exists
            if (requestUnit == null) {

                newUnit = new Mesure(null, autoUnit.getText().toString());
                MainActivity.mesureDao.insert(newUnit);
                initUnits();
                //dbIngredient.setMesureId(newUnit.getId());
                dbListUnits.add(""+newUnit.getId());
            }

            else{
                //dbIngredient.setMesureId(requestUnit.getId());
                dbListUnits.add(""+requestUnit.getId());
            }

            listFullIngredients.add(edittextQuantity.getText().toString() + " " + autoUnit.getText().toString() + " " + autoIngredient.getText().toString());
        }

        //handle ingredient
        long iD = MainActivity.ingredientDao.queryBuilder().where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Nom.eq(autoIngredient.getText())).unique().getId();
        dbListIngredients.add(""+ iD);


        //update the adapter
        adapterListviewIngredients.notifyDataSetChanged();

        //resize listview because of the scrollview
        resizeListviewIngredients();

        //clear the view
        autoUnit.getText().clear();
        edittextQuantity.getText().clear();
        autoIngredient.getText().clear();
    }

    private void initUnits(){

        listAutoUnits.clear();
        listRequestUnits = MainActivity.mesureDao.queryBuilder().list();
        for (int i = 0; i < listRequestUnits.size(); i++) {
            listAutoUnits.add(listRequestUnits.get(i).getNom());
        }
        Collections.sort(listAutoUnits);
        adapterUnits = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, listAutoUnits);
        autoUnit.setThreshold(1);
        autoUnit.setAdapter(adapterUnits);

    }

    private void initIngredients(){

        listAutoIngredients.clear();
        listRequestIngredients = MainActivity.ingredientDao.queryBuilder().list();
        for (int i = 0; i < listRequestIngredients.size(); i++) {
            listAutoIngredients.add(listRequestIngredients.get(i).getNom());
        }
        Collections.sort(listAutoIngredients);
        adapterIngredients = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, listAutoIngredients);
        autoIngredient.setThreshold(2);
        autoIngredient.setAdapter(adapterIngredients);

    }

    private void resizeListviewIngredients(){

        int totalHeight = listviewIngredients.getPaddingTop() + listviewIngredients.getPaddingBottom();
        for (int i = 0; i < adapterListviewIngredients.getCount(); i++) {
            View listItem = adapterListviewIngredients.getView(i, null, listviewIngredients);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listviewIngredients.getLayoutParams();
        params.height = totalHeight + (listviewIngredients.getDividerHeight() * (adapterListviewIngredients.getCount() - 1));
        listviewIngredients.setLayoutParams(params);

    }

    private void alertIngredientMissing() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Veuillez indiquer un ingrédient");
        alertDialog.setButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        alertDialog.show();

    }

    private void addPreferences(){

        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putInt("Quantities_size", dbListIngredients.size());
        for (int i = 0; i < dbListIngredients.size(); i++) {
            editor.remove("Quantities_" + i);
            editor.putString("Quantities_" + i, dbListQuantities.get(i).toString());
            editor.remove("Units_" + i);
            editor.putString("Units_" + i, dbListUnits.get(i).toString());
            editor.remove("Ingredients_" + i);
            editor.putString("Ingredients_" + i, dbListIngredients.get(i).toString());
        }
        editor.commit();
    }
}