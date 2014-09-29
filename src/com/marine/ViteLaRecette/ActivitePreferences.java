package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.marine.ViteLaRecette.adapter.AdapterPersonalSearch;
import com.marine.ViteLaRecette.dao.Categorie;
import com.marine.ViteLaRecette.dao.Ingredient;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao.Properties;

public class ActivitePreferences extends Activity {

	private ImageButton boutonBack;
	private ImageButton boutonAjoutCategorieBannie;
	private ListView listViewRecettes;
	private List<Recette> listeRecettes;
	private AdapterPersonalSearch adapter;
	private ListView listViewRecettesB;
	private List<Recette> listeRecettesB;
	private AdapterPersonalSearch adapterB;
	private ListView listViewIngredients;
	private List<Ingredient> listeIngredients;
	private ArrayList<String> listeIngreNoms;
	private ArrayAdapter<String> adapterI;
	private ListView listViewCategories;
	private List<Categorie> listeCategories;
	private ArrayList<String> listeCateNoms;
	private ArrayAdapter<String> adapterC;
	private Spinner spinnerCat;
	private List<String> listCat;
	private List<Categorie> listeCat;
	private ArrayAdapter<String> spinnerCatAdapter;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_preferences);

		initBoutonBack();

		initSpinnerCategorie();
		
		initBoutonAjouterCategorie();
		
		initListeRecettesFavoris();
		
		initListeRecettesBannies();
		
		initListeIngredients();

		initListeCategorie();

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
	
	/*
	 * Cette m�thode ajoute le bouton ajouter cat�gorie.
	 * Ce bouton permet d'ajouter une cat�gorie aux ind�sirables via un alertDialog.
	 */
	private void initBoutonAjouterCategorie(){
		
		boutonAjoutCategorieBannie = (ImageButton) findViewById(R.id.boutonAjoutCategorieBannie);
		boutonAjoutCategorieBannie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog alertDialog = new AlertDialog.Builder(
						ActivitePreferences.this).create();
				alertDialog.setMessage("Bannir cat�gorie : ");

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
								categorie.setFavoris(-1);
								MainActivity.categorieDao.update(categorie);
								listeCategories.add(categorie);
								listeCateNoms.add(categorie.getNom());
								adapterC.notifyDataSetChanged();
							}
						});

				alertDialog.show();

			}
		});
		
	}
	
	/*
	 * Initialise les spinner cat�gorie en vue de l'ajout d'une cat�gorie.
	 */
	private void initSpinnerCategorie(){
		
		spinnerCat = new Spinner(ActivitePreferences.this);

		listCat = new ArrayList<String>();
		listeCat = MainActivity.categorieDao
				.queryBuilder().list();
		for (int i = 0; i < listeCat.size(); i++) {
			listCat.add(listeCat.get(i).getNom());
		}

		spinnerCatAdapter = new ArrayAdapter<String>(
				ActivitePreferences.this,
				android.R.layout.simple_spinner_item, listCat);
		spinnerCatAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCat.setAdapter(spinnerCatAdapter);
		
	}
	
	/*
	 * Initialise la liste des recettes favorites et d�finit l'alertDialog a afficher en cas
	 * de selection.
	 */
	private void initListeRecettesFavoris(){
		listViewRecettes = (ListView) findViewById(R.id.recettesList);
		listViewRecettes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				alert = new AlertDialog.Builder(
						ActivitePreferences.this).create();
				alert.setMessage("Retirer de la liste ?");
				alert.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alert.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listeRecettes.get(arg2).setFavoris(0);
								MainActivity.recetteDao.update(listeRecettes
										.get(arg2));
								listeRecettes.remove(arg2);
								adapter.notifyDataSetChanged();
							}
						});
				alert.setButton3("Detail",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent1 = new Intent(
										ActivitePreferences.this,
										ActivityDetailRecipe.class);
								intent1.putExtra("ID", listeRecettes.get(arg2)
										.getId());
								startActivity(intent1);
							}
						});

				alert.show();
			}
		});

		listeRecettes = MainActivity.recetteDao.queryBuilder()
				.where(Properties.Favoris.eq(1)).list();

		adapter = new AdapterPersonalSearch(this, listeRecettes);
		listViewRecettes.setAdapter(adapter);
	}
	
	/*
	 * Initialise, remplit et g�re la suppression des �l�ments de la liste : Recettes bannies.
	 */
	private void initListeRecettesBannies(){
		
		listViewRecettesB = (ListView) findViewById(R.id.recettesBList);
		listViewRecettesB.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ActivitePreferences.this).create();
				alertDialog.setMessage("Retirer de la liste ?");
				alertDialog.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alertDialog.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listeRecettesB.get(arg2).setFavoris(0);
								MainActivity.recetteDao.update(listeRecettesB
										.get(arg2));
								listeRecettesB.remove(arg2);
								adapterB.notifyDataSetChanged();
							}
						});

				alertDialog.show();
			}
		});
		listeRecettesB = MainActivity.recetteDao.queryBuilder()
				.where(Properties.Favoris.eq(-1)).list();

		adapterB = new AdapterPersonalSearch(this, listeRecettesB);
		listViewRecettesB.setAdapter(adapterB);
		
	}
	
	/*
	 * Initialise, remplit et g�re la suppression des �l�ments de la liste : Ingr�dients bannis.
	 */
	private void initListeIngredients(){
		
		listViewIngredients = (ListView) findViewById(R.id.ingredientsList);
		listViewIngredients.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ActivitePreferences.this).create();
				alertDialog.setMessage("Retirer de la liste ?");
				alertDialog.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alertDialog.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listeIngredients.get(arg2).setFavoris(0);
								MainActivity.ingredientDao
										.update(listeIngredients.get(arg2));
								listeIngredients.remove(arg2);
								listeIngreNoms.remove(arg2);
								adapterI.notifyDataSetChanged();
							}
						});

				alertDialog.show();
			}
		});
		listeIngredients = MainActivity.ingredientDao
				.queryBuilder()
				.where(com.marine.ViteLaRecette.dao.IngredientDao.Properties.Favoris
						.eq(-1)).list();

		listeIngreNoms = new ArrayList<String>();

		for (int i = 0; i < listeIngredients.size(); i++) {
			listeIngreNoms.add(listeIngredients.get(i).getNom());
		}

		adapterI = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				listeIngreNoms);
		listViewIngredients.setAdapter(adapterI);
		
	}
	
	/*
	 * Initialise, remplit et g�re la suppression des �l�ments de la liste : Cat�gories bannies.
	 */
	private void initListeCategorie(){
		listViewCategories = (ListView) findViewById(R.id.categoriesList);
		listViewCategories.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ActivitePreferences.this).create();
				alertDialog.setMessage("Retirer de la liste ?");
				alertDialog.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alertDialog.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listeCategories.get(arg2).setFavoris(0);
								MainActivity.categorieDao
										.update(listeCategories.get(arg2));
								listeCategories.remove(arg2);
								listeCateNoms.remove(arg2);
								adapterC.notifyDataSetChanged();
							}
						});

				alertDialog.show();
			}
		});

		listeCategories = MainActivity.categorieDao
				.queryBuilder()
				.where(com.marine.ViteLaRecette.dao.CategorieDao.Properties.Favoris
						.eq(-1)).list();

		listeCateNoms = new ArrayList<String>();

		for (int i = 0; i < listeCategories.size(); i++) {
			listeCateNoms.add(listeCategories.get(i).getNom());
		}

		adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				listeCateNoms);
		listViewCategories.setAdapter(adapterC);
	}

}
