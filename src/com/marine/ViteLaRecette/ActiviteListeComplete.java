package com.marine.ViteLaRecette;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.marine.ViteLaRecette.dao.RecetteDao;

/*
 * Cette activit� permet de charger toutes les recettes de la base de donn�es
 * et d'en s�l�ctionn�e une ensuite.
 */

public class ActiviteListeComplete extends ListActivity {

	private ImageButton boutonBack;
	private Cursor cursor;
	private String textColumn;
	private String orderBy;
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_liste_complete);

		initBoutonBack();

		findRecettes();
		
		initListe();

	}

	/*
	 * (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 * On attribut un item click listener � la liste
	 * il permet de lancer l'activit� detailRecette correspondant � la recette selectionn�e.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent1 = new Intent(ActiviteListeComplete.this,
				ActiviteDetailRecette.class);
		intent1.putExtra("ID", (int) id);
		startActivity(intent1);
		cursor.requery();
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
	 * Cette fonction permet d'effectuer une simple requete sur la bdd
	 * On en ressort un curseur contenant toutes les recettes rang�es par nom
	 * un cursor adapter est utilis�.
	 */
	private void findRecettes(){
		
		textColumn = RecetteDao.Properties.Nom.columnName;
		orderBy = textColumn + " COLLATE LOCALIZED ASC";
		cursor = MainActivity.db.query(MainActivity.recetteDao.getTablename(),
				MainActivity.recetteDao.getAllColumns(), null, null, null,
				null, orderBy);
		
	}
	
	/*
	 * A partir du curseur de la requete, on remplit la listActivity par le biais d'un cursor adapter.
	 */
	private void initListe(){
		String[] from = { textColumn, RecetteDao.Properties.Type.columnName };
		int[] to = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, from, to);
		setListAdapter(adapter);
	}
	
	//Liberation des ressources
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			
			cursor.close();
			
			
			super.onStop();
		}


}