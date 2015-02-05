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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.marine.ViteLaRecette.ActivityAddRecipeStep2;
import com.marine.ViteLaRecette.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marine on 05/02/2015.
 */
public class AddRecipeStep1Fragment extends Fragment {

    private EditText editTextName;
    private EditText editTextNumber;
    private EditText editTextTimePrepa;
    private EditText editTextTimeCooking;
    private Spinner spinnerType;
    private Spinner spinnerDifficulty;
    private Spinner spinnerPrice;
    private Button buttonNext;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_add_recipe_step1, container, false);

        editTextName = (EditText) rootView.findViewById(R.id.edittextName);
        spinnerType = (Spinner) rootView.findViewById(R.id.spinnerType);
        editTextNumber = (EditText) rootView.findViewById(R.id.edittextNumber);
        editTextTimePrepa = (EditText) rootView.findViewById(R.id.edittextTimePrepa);
        editTextTimeCooking = (EditText) rootView.findViewById(R.id.edittextTimeCooking);
        spinnerDifficulty = (Spinner) rootView.findViewById(R.id.spinnerDifficulty);
        spinnerPrice = (Spinner) rootView.findViewById(R.id.spinnerPrice);
        buttonNext = (Button) rootView.findViewById(R.id.buttonNext);

        initUI(getActivity());

        return rootView;
    }

    protected void initUI(Activity a) {

        //Spinner Type
        List<String> listType = new ArrayList<String>();

        listType.add("Plat principal");
        listType.add("Entrée");
        listType.add("Dessert");
        listType.add("Sauce");
        listType.add("Accompagnement");
        listType.add("Apéritif");
        listType.add("Boisson");

        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listType);
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapterType);

        //Number
        editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

        //Time of preparation
        editTextTimePrepa.setInputType(InputType.TYPE_CLASS_NUMBER);

        //Time of cooking
        editTextTimeCooking.setInputType(InputType.TYPE_CLASS_NUMBER);

        //Spinner difficulty
        List<String> listDifficulty = new ArrayList<String>();

        listDifficulty.add("Facile");
        listDifficulty.add("Moyen");
        listDifficulty.add("Difficile");
        listDifficulty.add("Très difficile");

        ArrayAdapter<String> dataAdapterDifficulty = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listDifficulty);
        dataAdapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(dataAdapterDifficulty);


        //Spinner price
        List<String> listPrice = new ArrayList<String>();

        listPrice.add("Bon marché");
        listPrice.add("Moyen");
        listPrice.add("Très cher");

        ArrayAdapter<String> dataAdapterPrice = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listPrice);
        dataAdapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(dataAdapterPrice);





        //Init button
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resetPrefs();
                readValues();


                if (checkErrors()!=""){

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                    alertDialog.setMessage("Ces élements ne sont pas valides : "+errorDoc);
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    alertDialog.show();

                }
                else {
                    addPreferences();

                    Fragment fragment;
                    FragmentManager fragmentManager;

                    fragment = new AddRecipeStep2Fragment();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

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

        if(name.equals("") || name.substring(0,1).equals(" ")){
            errorDoc = errorDoc + "\n- Le nom de la recette";
            editTextName.getText().clear();
        }

        if(number.equals("") || number.equals("0")){
            errorDoc = errorDoc + "\n- Le nombre de personnes";
            editTextNumber.getText().clear();
        }

        if(timePrepa.equals("") && timeCooking.equals("") || timePrepa.equals("0") && timeCooking.equals("0") ||
                timePrepa.equals("") && timeCooking.equals("0") || timePrepa.equals("0") && timeCooking.equals("") ){
            errorDoc = errorDoc + "\n- Au moins un temps de préparation ou de cuisson";

            editTextTimePrepa.getText().clear();
            editTextTimeCooking.getText().clear();
        }

        return errorDoc;
    }

    private void addPreferences(){

        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_NAME", name);
        editor.putString("ADD_RECIPE_TYPE", type);
        editor.putInt("ADD_RECIPE_NUMBER", Integer.parseInt(number));

        //timePrepa becomes integer
        if(timePrepa.equals("")){
            editor.putInt("ADD_RECIPE_TIMEPREPA", 0);
        }else{
            editor.putInt("ADD_RECIPE_TIMEPREPA", Integer.parseInt(timePrepa));
        }

        //timeCooking becomes integer
        if(timeCooking.equals("")){
            editor.putInt("ADD_RECIPE_TIMECOOKING", 0);
        }else{
            editor.putInt("ADD_RECIPE_TIMECOOKING", Integer.parseInt(timeCooking));
        }

        editor.putString("ADD_RECIPE_DIFFICULTY", difficulty);
        editor.putString("ADD_RECIPE_PRICE", price);

        editor.commit();
    }

    private void resetPrefs(){
        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_NAME", "");
        editor.putString("ADD_RECIPE_TYPE", "");
        editor.putInt("ADD_RECIPE_NUMBER", 0);
        editor.putInt("ADD_RECIPE_TIMEPREPA", 0);
        editor.putInt("ADD_RECIPE_TIMECOOKING", 0);
        editor.putString("ADD_RECIPE_DIFFICULTY", "");
        editor.putString("ADD_RECIPE_PRICE", "");
        editor.putBoolean("RECIPE_COMPLETE",false);
        editor.commit();

    }

}