package com.marine.ViteLaRecette.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;

/**
 * Created by Marine on 05/02/2015.
 */
public class AddRecipeStep4Fragment extends Fragment {

    private Button buttonCancel;
    private Button buttonAdd;

    private SharedPreferences preferences;

    private String name;
    private String type;
    private int number;
    private int timePrepa;
    private int timeCooking;
    private String difficulty;
    private int difficultyInt;
    private String price;
    private int priceInt;

    private ArrayList<String> dbListQuantities;
    private ArrayList<String> dbListUnits;
    private ArrayList<String> dbListIngredients;
    private Quantite fullIngredient;
    private ArrayList<Quantite> listFullIngredients;
    private ArrayList<String> listFullIngredientsString;
    private String tempUnit;
    private String tempIngredient;

    private String steps;

    private TextView textViewName;
    private TextView textViewType;
    private TextView textViewNumber;
    private TextView textViewCookingTime;
    private TextView textViewPreparationTime;
    private TextView textViewDifficulty;
    private TextView textViewPrice;
    private ListView listViewIngredients;
    private ArrayAdapter<String> adapterListviewIngredients;
    private TextView textViewDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_add_recipe_step4, container, false);

        buttonCancel = (Button) rootView.findViewById(R.id.buttonCancel);
        buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        textViewName = (TextView) rootView.findViewById(R.id.textViewNameID);
        textViewType = (TextView) rootView.findViewById(R.id.textViewTypeID);
        textViewNumber = (TextView) rootView.findViewById(R.id.textViewNumberID);
        textViewPreparationTime = (TextView) rootView.findViewById(R.id.textViewPreparationTimeID);
        textViewCookingTime = (TextView) rootView.findViewById(R.id.textViewCookingTimeID);
        textViewDifficulty = (TextView) rootView.findViewById(R.id.textViewDifficultyID);
        textViewPrice = (TextView) rootView.findViewById(R.id.textViewPriceID);
        textViewDescription = (TextView) rootView.findViewById(R.id.textViewDescriptionID);
        listViewIngredients = (ListView)rootView.findViewById(R.id.listViewIngredientsID);

        initUI(getActivity());
        return rootView;
    }

    protected void initUI(Activity a) {

        //Init button
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addRecipe();
                Toast.makeText(getActivity().getApplicationContext(), "Recette ajoutée", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);

            }
        });

        initValues();

        dbListQuantities = new ArrayList<String>();
        dbListUnits = new ArrayList<String>();
        dbListIngredients = new ArrayList<String>();

        getPrefs();
        for (int i=0 ; i<dbListQuantities.size() ; i++){addFullIngredient(i);}


        textViewName.setText(name);
        textViewType.setText(type);
        textViewNumber.setText(""+number);
        textViewPreparationTime.setText(""+timePrepa);
        textViewCookingTime.setText(""+timeCooking);
        textViewDifficulty.setText(difficulty);
        textViewPrice.setText(price);
        textViewDescription.setText(steps);

        adapterListviewIngredients = new ArrayAdapter<String>(getActivity(), R.layout.item_petit, listFullIngredientsString);
        listViewIngredients.setAdapter(adapterListviewIngredients);
        resizeListviewIngredients();
    }

    private void initValues(){
        name = "";
        type = "";
        number = 0;
        timePrepa = 0;
        timeCooking = 0;
        difficulty = "";
        price = "";

        listFullIngredients = new ArrayList<Quantite>();
        listFullIngredientsString = new ArrayList<String>();

        steps = "";
    }

    private void getPrefs(){
        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        name = preferences.getString("ADD_RECIPE_NAME", "");
        type = preferences.getString("ADD_RECIPE_TYPE", "");
        number = preferences.getInt("ADD_RECIPE_NUMBER", 0);
        timePrepa = preferences.getInt("ADD_RECIPE_TIMEPREPA", 0);
        timeCooking = preferences.getInt("ADD_RECIPE_TIMECOOKING", 0);
        difficulty = preferences.getString("ADD_RECIPE_DIFFICULTY", "");
        price = preferences.getString("ADD_RECIPE_PRICE", "");
        steps = preferences.getString("ADD_RECIPE_STEPS", "");

        int sizeQuantities = preferences.getInt("Quantities_size", 0);
        dbListQuantities.clear();

        for (int i = 0; i < sizeQuantities; i++) {

            dbListQuantities.add(""+preferences.getString("Quantities_" + i, "").toString());
            dbListUnits.add(""+preferences.getString("Units_" + i, "").toString());
            dbListIngredients.add(""+preferences.getString("Ingredients_" + i, "").toString());
        }
    }

    private void addFullIngredient(int i){

        fullIngredient = new Quantite();

        fullIngredient.setQuantite((float) Integer.parseInt(dbListQuantities.get(i)));
        fullIngredient.setMesureId((long) Integer.parseInt(dbListUnits.get(i)));
        fullIngredient.setIngredientId((long) Integer.parseInt(dbListIngredients.get(i)));

        listFullIngredients.add(fullIngredient);

        //for the UI
        tempUnit = MainActivity.mesureDao.queryBuilder().where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Id.eq(dbListUnits.get(i))).unique().getNom();
        tempIngredient = MainActivity.ingredientDao.queryBuilder().where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Id.eq(dbListIngredients.get(i))).unique().getNom();

        listFullIngredientsString.add(dbListQuantities.get(i).toString() + " " + tempUnit.toString() + " " + tempIngredient.toString());

    }

    private void addRecipe(){

        long iD = 0;
        Recette recette = new Recette(null, name.toString(),
                type.toString(), (int) timeCooking,(int) timePrepa,
                steps.toString(), (int) translateToInt(price),
                (int) translateToInt(difficulty), (int) 0,
                (int) number, (int) 0);

        iD = MainActivity.recetteDao.insert(new Recette(null, name.toString(),
                type.toString(), (int) timeCooking,(int) timePrepa,
                steps.toString(), (int) translateToInt(price),
                (int) translateToInt(difficulty), (int) 0,
                (int) number, (int) 0));

        for (int i = 0; i < listFullIngredients.size(); i++) {
            listFullIngredients.get(i).setRecetteId(iD);
            MainActivity.daoSession.insert(listFullIngredients.get(i));
        }
    }

    private int translateToInt(String name){

        int rating = 0;

        if(name.equals("Facile")){rating = 1;}
        if(name.equals("Moyen")){rating = 2;}
        if(name.equals("Difficile")){rating = 3;}
        if(name.equals("Très difficile")){rating = 4;}
        if(name.equals("Bon marché")){rating = 1;}
        if(name.equals("Moyen")){rating = 2;}
        if(name.equals("Très cher")){rating = 3;}

        return rating;
    }

    private void resizeListviewIngredients(){

        int totalHeight = listViewIngredients.getPaddingTop() + listViewIngredients.getPaddingBottom();
        for (int i = 0; i < adapterListviewIngredients.getCount(); i++) {
            View listItem = adapterListviewIngredients.getView(i, null, listViewIngredients);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewIngredients.getLayoutParams();
        params.height = totalHeight + (listViewIngredients.getDividerHeight() * (adapterListviewIngredients.getCount() - 1));
        listViewIngredients.setLayoutParams(params);

    }
}