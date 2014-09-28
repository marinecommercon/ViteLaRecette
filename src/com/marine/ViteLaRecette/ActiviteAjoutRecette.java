package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.marine.ViteLaRecette.dao.Categorie;
import com.marine.ViteLaRecette.dao.Ingredient;
import com.marine.ViteLaRecette.dao.Mesure;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;


public class ActiviteAjoutRecette extends Activity {

	private ImageButton boutonBack;
	private ImageButton boutonAjoutIngredient;
	private ImageButton boutonAjoutRecette;
	private Cursor cursor;
	private ListView ingredients;
	private ArrayList<Quantite> quantites;
	private ArrayList<String> quantitesS;
	private EditText quantiteAuto;
	private EditText nombreAuto;
	private EditText detailAuto;
	private EditText preparationAuto;
	private EditText cuissonAuto;
	private EditText nomAuto;
	private RatingBar ratingPrix;
	private RatingBar ratingDifficulte;
	private AutoCompleteTextView mesureAuto;
	private AutoCompleteTextView ingredientAuto;
	private ArrayAdapter adapter;
	private Spinner spinnerType;
	private ArrayList<String> listIngredients;
	private List<Ingredient> listeIngredients;
	private ArrayAdapter<String> adapterA;
	private Ingredient ingredient;
	private Quantite quantite;
	private Mesure mesure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.activite_ajout_recette);

		initBoutonBack();

		boutonAjoutIngredient = (ImageButton) findViewById(R.id.boutonAjoutIngredient);
		boutonAjoutIngredient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ClickBoutonIngredient();

			}
		});

		boutonAjoutRecette = (ImageButton) findViewById(R.id.boutonAjoutRecette);
		boutonAjoutRecette.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ClickBoutonRecette();
			}
		});

		ingredients = (ListView) findViewById(R.id.ingredientList);

		ingredients.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ActiviteAjoutRecette.this).create();
				alertDialog.setMessage("Retirer de la liste ?");
				alertDialog.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alertDialog.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								quantites.remove(arg2);
								quantitesS.remove(arg2);
								adapter.notifyDataSetChanged();
							}
						});

				alertDialog.show();
			}
		});

		quantiteAuto = (EditText) findViewById(R.id.quantite);
		nombreAuto = (EditText) findViewById(R.id.nombreAuto);
		detailAuto = (EditText) findViewById(R.id.description);
		preparationAuto = (EditText) findViewById(R.id.preparationAuto);
		cuissonAuto = (EditText) findViewById(R.id.cuissonAuto);
		nomAuto = (EditText) findViewById(R.id.titreAuto);
		quantiteAuto.setInputType(InputType.TYPE_CLASS_NUMBER);
		mesureAuto = (AutoCompleteTextView) findViewById(R.id.mesure);
		ingredientAuto = (AutoCompleteTextView) findViewById(R.id.ingredient);
		ratingPrix = (RatingBar) findViewById(R.id.ratingPrix);
		ratingPrix.setNumStars(3);
		ratingDifficulte = (RatingBar) findViewById(R.id.ratingDifficulte);
		ratingDifficulte.setNumStars(4);

		listIngredients = new ArrayList();

		ArrayList<String> listMesures = new ArrayList();

		listeIngredients = MainActivity.ingredientDao.queryBuilder().list();

		List<Mesure> listeMesures = MainActivity.mesureDao.queryBuilder()
				.list();

		for (int i = 0; i < listeIngredients.size(); i++) {
			listIngredients.add(listeIngredients.get(i).getNom());
		}

		for (int i = 0; i < listeMesures.size(); i++) {
			listMesures.add(listeMesures.get(i).getNom());
		}

		adapterA = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, listIngredients);

		ArrayAdapter<String> adapterB = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, listMesures);

		ingredientAuto.setThreshold(2);
		ingredientAuto.setAdapter(adapterA);

		mesureAuto.setThreshold(1);
		mesureAuto.setAdapter(adapterB);

		quantites = new ArrayList();
		quantitesS = new ArrayList();

		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
				quantitesS);
		ingredients.setAdapter(adapter);

		spinnerType = (Spinner) findViewById(R.id.spinnerType);
		List<String> listType = new ArrayList<String>();

		listType.add(" Plat principal ");
		listType.add(" Entr�e ");
		listType.add(" Dessert ");
		listType.add(" Sauce ");
		listType.add(" Accompagnement ");
		listType.add(" Aperitif ");
		listType.add(" Boisson ");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listType);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(dataAdapter);

	}

	private void AlerteCreation(String nom) {

		AlertDialog alertDialog = new AlertDialog.Builder(
				ActiviteAjoutRecette.this).create();

		alertDialog.setMessage("Veuillez indiquer " + nom);

		alertDialog.setButton("Retour", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button

			}
		});

		alertDialog.show();

	}

	private void ClickBoutonIngredient() {

		if (quantiteAuto.length() == 0) {
			quantiteAuto.setText("0");
		}

		ingredient = MainActivity.ingredientDao
				.queryBuilder()
				.where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Nom
						.eq(ingredientAuto.getText())).unique();

		if (ingredientAuto.length() == 0) {
			ingredient = null;
			AlerteCreation("un ingr�dient.");
		}

		else if (ingredient == null) {

			AlertDialog alertDialog = new AlertDialog.Builder(
					ActiviteAjoutRecette.this).create();
			alertDialog.setMessage("L'ingr�dient n'existe pas, l'ajouter ? ");

			final Spinner spinnerCat = new Spinner(ActiviteAjoutRecette.this);

			List<String> listCat = new ArrayList<String>();
			final List<Categorie> listeCat = MainActivity.categorieDao
					.queryBuilder().list();
			for (int i = 0; i < listeCat.size(); i++) {
				listCat.add(listeCat.get(i).getNom());
			}

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					ActiviteAjoutRecette.this,
					android.R.layout.simple_spinner_item, listCat);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCat.setAdapter(dataAdapter);

			alertDialog.setView(spinnerCat);
			alertDialog.setButton("Retour",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			alertDialog.setButton2("Confirmer",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Categorie categorie = listeCat.get(spinnerCat
									.getSelectedItemPosition());
							ingredient = new Ingredient(null, ingredientAuto
									.getText().toString(), 0, categorie.getId());
							MainActivity.ingredientDao.insert(ingredient);
							listIngredients.add(ingredient.getNom());
							listeIngredients.add(ingredient);
							adapterA.notifyDataSetChanged();
						}
					});
			alertDialog.show();

		} else {

			quantite = new Quantite(null, (float) Integer.parseInt(quantiteAuto
					.getText().toString()), "", (long) -1, ingredient.getId(),
					(long) -1);

			mesure = MainActivity.mesureDao
					.queryBuilder()
					.where(com.marine.ViteLaRecette.dao.MesureDao.Properties.Nom
							.eq(mesureAuto.getText())).unique();

			if (mesureAuto.length() == 0) {
				mesure = null;
			}

			if (mesure == null) {
				quantite.setMesureId((long) 0);
				quantite.setSuffixe(mesureAuto.toString());
			} else {
				quantite.setMesureId(mesure.getId());
			}

			quantites.add(quantite);

			quantitesS.add(quantiteAuto.getText().toString() + " "
					+ mesureAuto.getText().toString() + " "
					+ ingredientAuto.getText().toString());

			adapter.notifyDataSetChanged();

			mesureAuto.setText("");
			quantiteAuto.setText("");
			ingredientAuto.setText("");
		}

	}

	private void ClickBoutonRecette() {
		if (nomAuto.getText().length() == 0) {
			AlerteCreation("un nom de recette.");
		} else if (nombreAuto.getText().length() == 0) {

			AlerteCreation("un nombre de personnes.");
		}

		else if (preparationAuto.getText().length() == 0) {
			AlerteCreation("un temp de pr�paration.");
		}

		else if (cuissonAuto.getText().length() == 0) {
			AlerteCreation("un temp de cuisson.");
		}

		else if (detailAuto.getText().length() == 0) {
			AlerteCreation("le d�tail de la recette.");
		}

		else if (quantites.size() == 0) {
			AlerteCreation("les ingr�dients utilis�s.");
		} else {
			long iD = 0;
			Recette recette = new Recette(null, nomAuto.getText().toString(),
					spinnerType.getSelectedItem().toString(),
					Integer.parseInt(cuissonAuto.getText().toString()),
					Integer.parseInt(preparationAuto.getText().toString()),
					detailAuto.getText().toString(),
					(int) ratingPrix.getRating(),
					(int) ratingDifficulte.getRating(), (int) 0,
					Integer.parseInt(nombreAuto.getText().toString()), (int) 0);

			iD = MainActivity.recetteDao.insert(new Recette(null, nomAuto
					.getText().toString(), spinnerType.getSelectedItem()
					.toString(), Integer.parseInt(cuissonAuto.getText()
					.toString()), Integer.parseInt(preparationAuto.getText()
					.toString()), detailAuto.getText().toString(),
					(int) ratingPrix.getRating(), (int) ratingDifficulte
							.getRating(), (int) 0, Integer.parseInt(nombreAuto
							.getText().toString()), (int) 0));



			for (int i = 0; i < quantites.size(); i++) {
				quantites.get(i).setRecetteId(iD);
				MainActivity.daoSession.insert(quantites.get(i));
			}


		}

		Toast.makeText(getApplicationContext(), "Recette ajout�e",
				Toast.LENGTH_SHORT).show();
		finish();

	}

	
	/*
	 * Initialisation du bouton retour
	 * et de son listener
	 */
	private void initBoutonBack(){
		boutonBack = (ImageButton) findViewById(R.id.boutonBack);
		boutonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}