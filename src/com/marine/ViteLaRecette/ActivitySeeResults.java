package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.QuantiteDao.Properties;
import com.marine.ViteLaRecette.dao.Recette;

public class ActivitySeeResults extends ListActivity {

	private String typeOfDish;
	private long ingredientAId;
	private long ingredientBId;
	private long ingredientCId;
	private Cursor cursor;
	private int flag;
	private ArrayList<Recette> listRecipes;
	private List<Quantite> listMatchingRecipes;
	private Spinner spinnerChangeOrder;
	private AdapterPersonalSearch adapter;
	private List<String> listOptions;
	private ArrayAdapter<String> adapterOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		typeOfDish = this.getIntent().getStringExtra("Type");
		ingredientAId = this.getIntent().getLongExtra("IngredientA", -1);
		ingredientBId = this.getIntent().getLongExtra("IngredientB", -1);
		ingredientCId = this.getIntent().getLongExtra("IngredientC", -1);

		setContentView(R.layout.activity_see_results);

		findRecettes();
		initListRecipes();
		initScore();
		initAdapter();
		fulfilSpinnerChangeOrder();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		id = listRecipes.get(position).getId();
		Intent intent = new Intent(ActivitySeeResults.this,ActivityDetailRecipe.class);
		intent.putExtra("ID", (int) id);
		startActivity(intent);
	}


	private void putInOrder(List<Recette> list, int p1, int p2, int p3, int p4) {

		int i = 0;
		int place;
		Recette recTemp;

		while (i < list.size()) {

			place = i;
			recTemp = list.get(i);

			for (int j = i; j < list.size(); j++) {

				if ((condition(recTemp, list.get(j), p1))
						|| (condition(recTemp, list.get(j), p1 + 2) && condition(
								recTemp, list.get(j), p2))
						|| (condition(recTemp, list.get(j), p1 + 2)
								&& condition(recTemp, list.get(j), p2 + 2) && condition(
									recTemp, list.get(j), p3))
						|| (condition(recTemp, list.get(j), p1 + 2)
								&& condition(recTemp, list.get(j), p2 + 2)
								&& condition(recTemp, list.get(j), p3 + 2) && condition(
									recTemp, list.get(j), p4))) {
					place = j;
					recTemp = list.get(j);
				}

			}

			list.set(place, list.get(i));
			list.set(i, recTemp);

			i++;

		}

	}


	private boolean condition(Recette R1, Recette R2, int c) {

		switch (c) {

		case 0:
			return false;

		case 'I':
			return R1.getScore() < R2.getScore();

		case 'I' + 1:
			return R1.getScore() > R2.getScore();

		case 'I' + 2:
			return R1.getScore().equals(R2.getScore());

		case 'I' + 3:
			return R1.getScore().equals(R2.getScore());

		case 'T':
			return R1.getCuisson() + R1.getPreparation() > R2.getCuisson()
					+ R2.getPreparation();

		case 'T' + 1:
			return R1.getCuisson() + R1.getPreparation() < R2.getCuisson()
					+ R2.getPreparation();

		case 'T' + 2:
			return R1.getCuisson() + R1.getPreparation() == R2.getCuisson()
					+ R2.getPreparation();

		case 'T' + 3:
			return R1.getCuisson() + R1.getPreparation() == R2.getCuisson()
					+ R2.getPreparation();

		case 'D':
			return R1.getDifficulte() > R2.getDifficulte();

		case 'D' + 1:
			return R1.getDifficulte() < R2.getDifficulte();

		case 'D' + 2:
			return R1.getDifficulte().equals(R2.getDifficulte());

		case 'D' + 3:
			return R1.getDifficulte().equals(R2.getDifficulte());

		case 'P':
			return R1.getPrix() > R2.getPrix();

		case 'P' + 1:
			return R1.getPrix() < R2.getPrix();

		case 'P' + 2:
			return R1.getPrix().equals(R2.getPrix());

		case 'P' + 3:
			return R1.getPrix().equals(R2.getPrix());

		default:
			return false;

		}

	}


	public void fulfilSpinnerChangeOrder() {

		spinnerChangeOrder = (Spinner) findViewById(R.id.spinnerChangeOrder);
		listOptions = new ArrayList<String>();

		listOptions.add("Ranger par occurence d'ingredients");
		listOptions.add("Ranger par temps croissant");
		listOptions.add("Ranger par temps decroissant");
		listOptions.add("Ranger par difficulte croissante");
		listOptions.add("Ranger par difficulte decroissante");
		listOptions.add("Ranger par prix croissant");
		listOptions.add("Ranger par prix decroissant");

		adapterOptions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listOptions);

		adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerChangeOrder.setAdapter(adapterOptions);

		spinnerChangeOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                switch (position) {

                    case 0:
                        putInOrder(listRecipes, 'I', 'T', 'D', 'P');
                        break;

                    case 1:
                        putInOrder(listRecipes, 'T', 'I', 'D', 'P');
                        break;

                    case 2:
                        putInOrder(listRecipes, 'T' + 1, 'I', 'D', 'P');
                        break;

                    case 3:
                        putInOrder(listRecipes, 'D', 'I', 'T', 'P');
                        break;

                    case 4:
                        putInOrder(listRecipes, 'D' + 1, 'I', 'T', 'P');
                        break;

                    case 5:
                        putInOrder(listRecipes, 'P', 'I', 'T', 'D');
                        break;

                    case 6:
                        putInOrder(listRecipes, 'P' + 1, 'I', 'T', 'D');
                        break;

                    default:
                        putInOrder(listRecipes, 'I', 'T', 'D', 'P');
                        break;

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });

	}
	

	

	private void findRecettes() {

        String MY_QUERY;

        if (typeOfDish.equals("Peu importe")) {

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
                + "WHERE RECETTE.TYPE = '" + typeOfDish + "' " +
                "AND RECETTE.FAVORIS <> -1 ";
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
                + "AND INGREDIENT.FAVORIS = -1 ) " + "GROUP BY RECETTE._id ";

        cursor = MainActivity.db.rawQuery(MY_QUERY, null);
		
	}
	

	private void initListRecipes(){

		listRecipes = new ArrayList<Recette>();
		if (cursor.moveToFirst()) {
			do {
				listRecipes.add(MainActivity.recetteDao.load((long) cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}
	

	private void initScore(){
		
		if ((ingredientAId >= 0) || (ingredientBId >= 0) || (ingredientCId >= 0)) {
			listMatchingRecipes = MainActivity.quantiteDao
					.queryBuilder()
					.whereOr(Properties.IngredientId.eq(ingredientAId),
							Properties.IngredientId.eq(ingredientBId),
							Properties.IngredientId.eq(ingredientCId)).list();

			for (int i = 0; i < listRecipes.size(); i++) {

				for (int j = 0; j < listMatchingRecipes.size(); j++) {
					if (listRecipes.get(i).getId() == listMatchingRecipes.get(j).getRecetteId()) {
						flag = flag + 1;
					}
				}

				listRecipes.get(i).setScore(flag);

			}
		}
		
	}
	

	private void initAdapter(){
		adapter = new AdapterPersonalSearch(this, listRecipes);
		setListAdapter(adapter);
	}
	
	

}
