package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.marine.ViteLaRecette.dao.Ingredient;

public class ActivityPersonalSearch extends Activity {


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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.activity_personal_search);

		
		initBoutonRecherche();
		initSpinnerType();
		initIngredients();

	}

	
	public void initSpinnerType() {

		spinnerTypeOfDish = (Spinner) findViewById(R.id.spinnerChangeOrder);
		List<String> list = new ArrayList<String>();

        list.add("Peu importe");
        list.add("Plat principal");
        list.add("Entrée");
		list.add("Dessert");
		list.add("Sauce");
		list.add("Accompagnement");
		list.add("Amuse-gueule");
		list.add("Boisson");

		ArrayAdapter<String> spinnerTypeOfDishAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);

        spinnerTypeOfDishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTypeOfDish.setAdapter(spinnerTypeOfDishAdapter);
	}

	
	private long ingredientExiste(Editable c) {

		Ingredient ingredient = MainActivity.ingredientDao
				.queryBuilder()
				.where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Nom
						.eq(c)).unique();

		if (ingredient != null) {

			return ingredient.getId();

		} else {
			return -1;
		}

	}
	
	
	
	private void initIngredients(){
		
		listIngredientsNames = new ArrayList<String>();

		listIngredients = MainActivity.ingredientDao.queryBuilder().list();

		for (int i = 0; i < listIngredients.size(); i++) {
			listIngredientsNames.add(listIngredients.get(i).getNom());
		}

		adapterIngredients = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, listIngredientsNames);

		autoCompIngredientA = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewIngredientA);
		autoCompIngredientA.setThreshold(2);
		autoCompIngredientA.setAdapter(adapterIngredients);

		autoCompIngredientB = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewIngredientB);
		autoCompIngredientB.setThreshold(2);
		autoCompIngredientB.setAdapter(adapterIngredients);

		autoCompIngredientC = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewIngredientC);
		autoCompIngredientC.setThreshold(2);
		autoCompIngredientC.setAdapter(adapterIngredients);
		
	}
	
	

	
	private void initBoutonRecherche(){
		buttonSearch = (Button) findViewById(R.id.buttonSearchID);
		buttonSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(ActivityPersonalSearch.this,
                        ActivitySeeResults.class);

                intent.putExtra("Type",String.valueOf(spinnerTypeOfDish.getSelectedItem()));

                if (autoCompIngredientA.getText().length() > 0) {
                    intent.putExtra("IngredientA",
                            ingredientExiste(autoCompIngredientA.getText()));
                }

                if (autoCompIngredientB.getText().length() > 0) {
                    intent.putExtra("IngredientB",
                            ingredientExiste(autoCompIngredientB.getText()));
                }

                if (autoCompIngredientC.getText().length() > 0) {
                    intent.putExtra("IngredientC",
                            ingredientExiste(autoCompIngredientC.getText()));
                }

                startActivity(intent);
            }

        });
	}

}
