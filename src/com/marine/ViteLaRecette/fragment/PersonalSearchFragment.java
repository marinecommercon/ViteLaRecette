package com.marine.ViteLaRecette.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import com.marine.ViteLaRecette.ActivitySeeResults;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marine on 04/02/2015.
 */
public class PersonalSearchFragment extends Fragment {

    private Button buttonSearch;

    private Spinner spinnerTypeOfDish;

    private AutoCompleteTextView autoCompIngredientA;
    private AutoCompleteTextView autoCompIngredientB;
    private AutoCompleteTextView autoCompIngredientC;

    private ArrayAdapter<String> adapterIngredients;
    private List<Ingredient> listIngredients;
    private ArrayList<String> listIngredientsNames;
    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_personal_search, container, false);
        buttonSearch = (Button) rootView.findViewById(R.id.buttonSearchID);
        spinnerTypeOfDish = (Spinner) rootView.findViewById(R.id.spinnerChangeOrder);
        autoCompIngredientA = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewIngredientA);
        autoCompIngredientB = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewIngredientB);
        autoCompIngredientC = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewIngredientC);

        initBoutonRecherche();
        initSpinnerType();
        initIngredients();

        return rootView;
    }

    private void initBoutonRecherche(){
           buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment;
                FragmentManager fragmentManager;

                Bundle bundle = new Bundle();
                bundle.putString("Type", String.valueOf(spinnerTypeOfDish.getSelectedItem()));

                fragment = new SeeResultsFragment();

                if (autoCompIngredientA.getText().length() > 0) {
                    bundle.putLong("IngredientA", ingredientExiste(autoCompIngredientA.getText()));
                }
                if (autoCompIngredientB.getText().length() > 0) {
                    bundle.putLong("IngredientB", ingredientExiste(autoCompIngredientB.getText()));
                }
                if (autoCompIngredientC.getText().length() > 0) {
                    bundle.putLong("IngredientC", ingredientExiste(autoCompIngredientC.getText()));
                }

                fragment.setArguments(bundle);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
    }

    private long ingredientExiste(Editable c) {

        Ingredient ingredient = MainActivity.ingredientDao
                .queryBuilder()
                .where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Nom
                        .eq(c)).unique();

        if (ingredient != null) {
            return ingredient.getId();
        }
        else {
            return -1;
        }
    }

    public void initSpinnerType() {

        List<String> list = new ArrayList<String>();

        list.add("Peu importe");
        list.add("Plat principal");
        list.add("Entrée");
        list.add("Dessert");
        list.add("Sauce");
        list.add("Accompagnement");
        list.add("Amuse-gueule");
        list.add("Boisson");

        ArrayAdapter<String> spinnerTypeOfDishAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);

        spinnerTypeOfDishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfDish.setAdapter(spinnerTypeOfDishAdapter);
    }

    private void initIngredients(){

        listIngredientsNames = new ArrayList<String>();

        listIngredients = MainActivity.ingredientDao.queryBuilder().list();

        for (int i = 0; i < listIngredients.size(); i++) {
            listIngredientsNames.add(listIngredients.get(i).getNom());
        }
        adapterIngredients = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, listIngredientsNames);

        autoCompIngredientA.setThreshold(2);
        autoCompIngredientA.setAdapter(adapterIngredients);

        autoCompIngredientB.setThreshold(2);
        autoCompIngredientB.setAdapter(adapterIngredients);

        autoCompIngredientC.setThreshold(2);
        autoCompIngredientC.setAdapter(adapterIngredients);

    }

}
