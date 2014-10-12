package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.marine.ViteLaRecette.adapter.AdapterCourses;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;

public class ActivityShoplist extends Activity implements OnClickListener {

	private ImageButton boutonBack;
	private ListView listRecettes;
	private ArrayList<Recette> listeRecettes;

	private AdapterCourses adapter;
	private List<Liste> courses;
	ArrayList<Quantite> quantite;
	private TextView info1;
	private int[] listeNbInitial = new int[200];// Tableau de taille NT pour les
												// diff�rents
												// "Nombres de personnes" de
												// chaque recette
	private int[] listeNbFinal = new int[200];// Tableau de taille NT pour les
												// diff�rents
												// "Nombres de personnes" en
	private String[] QUA = new String[200];// Tableau de taille NT pour les
											// diff�rentes quantit�s
	private String[] MES = new String[200];// Tableau de taille NT pour les
											// diff�rentes mesures
	private String[] ING = new String[200];// Tableau de taille NT pour les
											// diff�rents ingr�dients
	private String[][] data = new String[200][3];// Tableau final contenant
													// toutes les donn�es utiles

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoplist);

		boutonBack = (ImageButton) findViewById(R.id.boutonBack);
		boutonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		info1 = (TextView) findViewById(R.id.info1);

		listeRecettes = new ArrayList<Recette>();
		listRecettes = (ListView) findViewById(R.id.recettesList);
		courses = MainActivity.listeDao.loadAll();
		for (int i = 0; i < courses.size(); i++) {
			listeRecettes.add(courses.get(i).getRecette());
		}

		adapter = new AdapterCourses(this, listeRecettes);
		listRecettes.setAdapter(adapter);
		listRecettes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> arg0, View arg1,
					final int arg2, final long arg3) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ActivityShoplist.this).create();
				alertDialog.setMessage("Retirer de la liste ?");
				alertDialog.setButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
							}
						});
				alertDialog.setButton2("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
								MainActivity.listeDao.delete(courses.get(arg2));
								listeRecettes.remove(arg2);
								courses.remove(arg2);
								info1.setText("");
								Recommence();
								adapter.notifyDataSetChanged();
							}
						});
				alertDialog.setButton3("Detail",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
								Intent intent1 = new Intent(
										ActivityShoplist.this,
										ActivityDetailRecipe.class);
								intent1.putExtra("ID", listeRecettes.get(arg2)
										.getId().intValue());

								startActivity(intent1);

							}
						});

				alertDialog.show();
			}
		});

		quantite = new ArrayList<Quantite>();
		Recommence();

	}

	public void Recommence() {

		int compteur = 0;// Compteur permettant cr�er un tableau de taille NT �
							// partir des nombre de personnes entr�s par
							// l'utilisateur
		int quantiteSize = 0;// Compte le nombre de quantit�s par recette
		int NT = 0;// Le nombre total de quantit�s
		int nombreFinal = 0;// Variable qui permet de stocker le nombre de
							// personnes entr�s par l'utilisateur pour une
							// recette

		quantite.clear();

		for (int j = 0; j < courses.size(); j++) {
			quantite.addAll(MainActivity.quantiteDao
					._queryRecette_QuantDeRec(listeRecettes.get(j).getId()));
		}
		NT = quantite.size();

		// Cr�ation d'un tableau de taille NT (listeNbFinal[]) � partir des
		// nombre de personnes entr�s par l'utilisateur
		for (int j = 0; j < courses.size(); j++) {
			quantiteSize = courses.get(j).getRecette().getQuantDeRec().size();
			nombreFinal = courses.get(j).getNombre();

			for (int i = compteur; i < quantiteSize + compteur; i++) {
				listeNbFinal[i] = nombreFinal;
				Log.d("test", "1" + listeNbFinal[i]);
			}
			compteur = compteur + quantiteSize;
		}

		// Remplissage de data[][] avec ajustement des quantit�s
		for (int i = 0; i < NT; i++) {

			listeNbInitial[i] = quantite.get(i).getRecette().getNombre();
			QUA[i] = ""
					+ Math.round(quantite.get(i).getQuantite()
							* listeNbFinal[i] / listeNbInitial[i]);
			MES[i] = quantite.get(i).getMesure().getNom();
			ING[i] = quantite.get(i).getIngredient().getNom();

			if (QUA[i].equals("0")) {
				QUA[i] = "";
			}

			data[i][1] = ING[i];
			data[i][0] = QUA[i] + " " + MES[i];
		}

		// Tri des lignes de data par ordre alphab�tique du nom de l'ingr�dient
		for (int i = 0; i < NT; i++) {
			for (int j = 0; j < NT; j++) {
				if (data[i][1].compareTo(data[j][1]) < 0) {
					String a = data[j][1];
					String b = data[j][0];
					data[j][1] = data[i][1];
					data[j][0] = data[i][0];
					data[i][1] = a;
					data[i][0] = b;
				}
			}
		}

		// Affichage dans le TextView
		for (int i = 0; i < NT; i++) {
			info1.append("" + data[i][0] + " " + data[i][1] + "\n");
		}
		
		
		
		
		
		
		

		compteur = 0;// Compteur permettant cr�er un tableau de taille NT �
		// partir des nombre de personnes entr�s par
		// l'utilisateur
		quantiteSize = 0;// Compte le nombre de quantit�s par recette
		NT = 0;// Le nombre total de quantit�s
		nombreFinal = 0;// Variable qui permet de stocker le nombre de
		// personnes entr�s par l'utilisateur pour une
		// recette

		
		/*
		quantite.clear();
		quantites = new ArrayList<String>();

		for (int j = 0; j < courses.size(); j++) {
			quantite.addAll(MainActivity.quantiteDao
					._queryRecette_QuantDeRec(listeRecettes.get(j).getId()));
		}
		NT = quantite.size();
		
		for(int i = 0; i < NT; i++){
			
			quantite.get(i).setQuantite(quantite.get(i).getQuantite()*MainActivity.listeDao.queryBuilder()
					.where(fr.hei.iti4.cookeasy.dao.ListeDao.Properties.RecetteId.eq(quantite.get(i).getRecetteId()))
					.unique().getNombre());
			
		}
		
		int place;
		Quantite quantiteUn;
		
		for(int i = 0; i < NT; i++){
			place=i;
			for(int j = i; j < NT; j++){
				if(quantite.get(i).getIngredient().getNom().charAt(0) > quantite.get(j).getIngredient().getNom().charAt(0)){
					place = j;
				}
			}
			quantiteUn = quantite.get(i);
			quantite.set(i, quantite.get(place));
			quantite.set(place, quantiteUn);
			
			quantites.add(""+quantite.get(i).getQuantite()+quantite.get(i).getMesure().getNom()+quantite.get(i).getSuffixe()+quantite.get(i).getIngredient().getNom());
			
			adapterQ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					quantites);
			
			ListView listViewQuantite = (ListView)findViewById(R.id.quantitesList);
			
			listViewQuantite.setAdapter(adapterQ);
			
		}
		*/
		
		
		
		
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
