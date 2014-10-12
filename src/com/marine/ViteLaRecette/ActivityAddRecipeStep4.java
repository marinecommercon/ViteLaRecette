package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.marine.ViteLaRecette.dao.Mesure;
import com.marine.ViteLaRecette.dao.Quantite;

import java.util.ArrayList;

/**
 * Created by mcommercon on 09/10/14.
 */
public class ActivityAddRecipeStep4 extends Activity {

    private Button buttonCancel;
    private Button buttonAdd;

    private SharedPreferences preferences;

    private String name;
    private String type;
    private int number;
    private int timePrepa;
    private int timeCooking;
    private String difficulty;
    private String price;

    private ArrayList<String> dbListQuantities;
    private ArrayList<String> dbListUnits;
    private ArrayList<String> dbListIngredients;
    private Quantite fullIngredient;
    private ArrayList<Quantite> ListFullIngredients;

    private String steps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_step4);

        Intent intent = getIntent();

        dbListQuantities = intent.getStringArrayListExtra("DB_QUANTITIES");
        dbListUnits = intent.getStringArrayListExtra("DB_UNITS");
        dbListIngredients = intent.getStringArrayListExtra("DB_INGREDIENTS");


        initUI(this);

    }


    protected void initUI(Activity a) {

        //Init button
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddRecipeStep4.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        initValues();
        getPrefs();
        addFullIngredient(0);



    }




    private void initValues(){
        name = "";
        type = "";
        number = 0;
        timePrepa = 0;
        timeCooking = 0;
        difficulty = "";
        price = "";

        ListFullIngredients = new ArrayList<Quantite>();

        steps = "";
    }

    private void getPrefs(){
        preferences =  getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        name = preferences.getString("ADD_RECIPE_NAME", "");
        type = preferences.getString("ADD_RECIPE_TYPE", "");
        number = preferences.getInt("ADD_RECIPE_NUMBERE", 0);
        timePrepa = preferences.getInt("ADD_RECIPE_TIMEPREPA", 0);
        timeCooking = preferences.getInt("ADD_RECIPE_TIMECOOKING", 0);
        difficulty = preferences.getString("ADD_RECIPE_DIFFICULTY", "");
        price = preferences.getString("ADD_RECIPE_PRICE", "");
        steps = preferences.getString("ADD_RECIPE_STEPS", "");
    }

    private void addFullIngredient(int i){

        fullIngredient = new Quantite();

        dbListQuantities.get(i);
        dbListIngredients.get(i);

        fullIngredient.setQuantite((float) Integer.parseInt(dbListUnits.get(i)));
        fullIngredient.setMesureId((long) Integer.parseInt(dbListQuantities.get(i)));
        fullIngredient.setIngredientId((long) Integer.parseInt(dbListIngredients.get(i)));

        ListFullIngredients.add(fullIngredient);

    }

}