package com.marine.ViteLaRecette;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * Created by mcommercon on 08/10/14.
 */
public class ActivityAddRecipeStep1 extends Activity {

    private EditText editTextName;
    private EditText editTextNumber;
    private EditText editTextTimePrepa;
    private EditText editTextTimeCooking;
    private Spinner spinnerType;
    private Spinner spinnerDifficulty;
    private Spinner spinnerPrice;
    Button buttonNext;

    private SharedPreferences preferences;

    private String name;
    private String type;
    private String number;
    private String timePrepa;
    private String timeCooking;
    private String difficulty;
    private String price;

    private String errorDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_step1);
        initUI(this);

    }


    protected void initUI(Activity a) {

        //Name of the recipe
        editTextName = (EditText) findViewById(R.id.edittextName);



        //Spinner Type
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        List<String> listType = new ArrayList<String>();

        listType.add("Plat principal");
        listType.add("Entree");
        listType.add("Dessert");
        listType.add("Sauce");
        listType.add("Accompagnement");
        listType.add("Aperitif");
        listType.add("Boisson");

        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listType);
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapterType);


        //Number
        editTextNumber = (EditText) findViewById(R.id.edittextNumber);

        //Time of preparation
        editTextTimePrepa = (EditText) findViewById(R.id.edittextTimePrepa);

        //Time of cooking
        editTextTimeCooking = (EditText) findViewById(R.id.edittextTimeCooking);

        //Spinner difficulty
        spinnerDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);
        List<String> listDifficulty = new ArrayList<String>();

        listDifficulty.add("Facile");
        listDifficulty.add("Moyen");
        listDifficulty.add("Difficile");
        listDifficulty.add("Tres difficle");

        ArrayAdapter<String> dataAdapterDifficulty = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listDifficulty);
        dataAdapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(dataAdapterDifficulty);


        //Spinner price
        spinnerPrice = (Spinner) findViewById(R.id.spinnerPrice);
        List<String> listPrice = new ArrayList<String>();

        listPrice.add("Bon marche");
        listPrice.add("Moyen");
        listPrice.add("Tres cher");

        ArrayAdapter<String> dataAdapterPrice = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listPrice);
        dataAdapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(dataAdapterPrice);





        //Init button
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resetPrefs();
                readValues();


               if (checkErrors()!=""){

                   AlertDialog alertDialog = new AlertDialog.Builder(ActivityAddRecipeStep1.this).create();

                   alertDialog.setMessage("Ces élements ne sont pas valides : "+errorDoc);
                   alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                       }
                   });

                   alertDialog.show();

                                  }
               else {
                    addPreferences();
                    Intent intent = new Intent(ActivityAddRecipeStep1.this, ActivityAddRecipeStep2.class);
                    startActivity(intent);

                }
            }
        });

    }





    private void readValues(){
        name = editTextName.getText().toString();
        type = spinnerType.getSelectedItem().toString();
        number = editTextNumber.getText().toString();
        timePrepa = editTextTimePrepa.getText().toString();
        timeCooking = editTextTimeCooking.getText().toString();
        difficulty = spinnerDifficulty.getSelectedItem().toString();
        price =  spinnerPrice.getSelectedItem().toString();
    }


    private String checkErrors(){

        errorDoc = "";

        if(name.equals("")){
            errorDoc = errorDoc + "\n-Le nom de la recette";
            editTextName.getText().clear();
        }

        if(number.equals("") || number.equals("0")){
            errorDoc = errorDoc + "\n-Le nombre de personnes";
            editTextNumber.getText().clear();
        }

        if(timePrepa.equals("") && timeCooking.equals("") || timePrepa.equals("0") && timeCooking.equals("0") ||
           timePrepa.equals("") && timeCooking.equals("0") || timePrepa.equals("0") && timeCooking.equals("") ){
            errorDoc = errorDoc + "\n-Au moins un temps de preparation ou de cuisson";

            editTextTimePrepa.getText().clear();
            editTextTimeCooking.getText().clear();
        }

        return errorDoc;
    }

    private void addPreferences(){

        preferences =  getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_NAME", name);
        editor.putString("ADD_RECIPE_TYPE", type);
        editor.putString("ADD_RECIPE_NUMBER", number);
        editor.putString("ADD_RECIPE_TIMEPREPA", number);
        editor.putString("ADD_RECIPE_TIMECOOKING", number);
        editor.putString("ADD_RECIPE_DIFFICULTY", difficulty);
        editor.putString("ADD_RECIPE_PRICE", price);
        editor.commit();
    }



    private void resetPrefs(){
        preferences =  getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_NAME", "");
        editor.putString("ADD_RECIPE_TYPE", "");
        editor.putString("ADD_RECIPE_NUMBER", "");
        editor.putString("ADD_RECIPE_TIMEPREPA", "");
        editor.putString("ADD_RECIPE_TIMECOOKING", "");
        editor.putString("ADD_RECIPE_DIFFICULTY", "");
        editor.putString("ADD_RECIPE_PRICE", "");
        editor.commit();

    }

}
