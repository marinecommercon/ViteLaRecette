package com.marine.ViteLaRecette;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;


public class ActivityAllRecipes extends ListActivity {

	private Cursor cursor;
	private String textColumn;
	private String orderBy;
	private SimpleCursorAdapter adapter;
    private int position;
    private int globalHeight;
    private double letterHeight;
    private Recette recipe;
    private String[] alphabeticalList = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};


    @Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_recipes);

        addLetters();
		findRecipes();
		initList();
	}


    @Override
    protected void onPause() {
        super.onPause();
        position = cursor.getPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findRecipes();
        initList();
        getListView().setSelection(position);
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(ActivityAllRecipes.this,ActivityDetailRecipe.class);
		intent.putExtra("ID", (int) id);
		startActivity(intent);
		cursor.requery();
	}

	private void findRecipes(){
		
		textColumn = RecetteDao.Properties.Nom.columnName;
		orderBy = textColumn + " COLLATE LOCALIZED ASC";
		cursor = MainActivity.db.query(MainActivity.recetteDao.getTablename(),
				MainActivity.recetteDao.getAllColumns(), null, null, null,
				null, orderBy);
			}
	

	private void initList(){
		String[] from = { textColumn, RecetteDao.Properties.Type.columnName };
		int[] to = { R.id.textViewRecipeNameID, R.id.textViewRecipeTypeID };
		adapter = new SimpleCursorAdapter(this, R.layout.activity_all_recipes_list, cursor, from, to);
		setListAdapter(adapter);


       }


    private void addLetters(){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        globalHeight = metrics.heightPixels;
        letterHeight = Math.floor((double) (globalHeight/27 * 10)) / 10;

        LinearLayout linearLayoutLetters = (LinearLayout) findViewById(R.id.linearLayoutLettersID);

        final TextView[] textViewLetter = new TextView[26];
                for(int i=0; i<26; i++) {
                    textViewLetter[i] = new TextView(this);
                    textViewLetter[i].setHeight((int) letterHeight);
                    textViewLetter[i].setText(alphabeticalList[i].toString());
                    textViewLetter[i].setTextSize(15);
                    textViewLetter[i].setGravity(Gravity.CENTER_HORIZONTAL);
                    textViewLetter[i].setTag(alphabeticalList[i].toString());
                    linearLayoutLetters.addView(textViewLetter[i]);
                    textViewLetter[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    findPosition(v, null);
                                    }
                            });

                }
    }


    private void findPosition(View v, String tag) {
        int i =0;

        recipe = MainActivity.recetteDao.queryBuilder()
                .where(RecetteDao.Properties.Id.eq(getListAdapter().getItemId(0)))
                .unique();

        if(!v.equals(null)) {
            while (!recipe.getNom().substring(0, 1).equals(v.getTag()) && i < getListView().getCount()) {

                i = i + 1;
                recipe = MainActivity.recetteDao.queryBuilder()
                        .where(RecetteDao.Properties.Id.eq(getListAdapter().getItemId(i)))
                        .unique();
            }
        }

        if (i>=getListView().getCount()){

            Toast toast = Toast.makeText(getApplicationContext(), "Desole, \nPas de recette pour cette lettre", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            getListView().setSelection(i);}
    }


    @Override
		protected void onStop() {
			// TODO Auto-generated method stub
			cursor.close();
			super.onStop();
		}


}