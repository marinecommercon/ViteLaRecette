package com.marine.ViteLaRecette;

import android.widget.Button;
import com.marine.ViteLaRecette.dao.CategorieDao;
import com.marine.ViteLaRecette.dao.DaoMaster;
import com.marine.ViteLaRecette.dao.DaoSession;
import com.marine.ViteLaRecette.dao.IngredientDao;
import com.marine.ViteLaRecette.dao.ListeDao;
import com.marine.ViteLaRecette.dao.MesureDao;
import com.marine.ViteLaRecette.dao.QuantiteDao;
import com.marine.ViteLaRecette.dao.RecetteDao;
import com.marine.ViteLaRecette.dao.DaoMaster.DevOpenHelper;
import com.marine.ViteLaRecette.database.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;




public class MainActivity extends Activity implements OnClickListener {

	private MyDatabase dbImp;

	static SQLiteDatabase db;

	private DaoMaster daoMaster;
	static DaoSession daoSession;
	static RecetteDao recetteDao;
	static QuantiteDao quantiteDao;
	static IngredientDao ingredientDao;
	static CategorieDao categorieDao;
	static ListeDao listeDao;
	static MesureDao mesureDao;

	private Button buttonPersonalSearch;
    private Button buttonAllRecipes;
    private Button buttonPreferences;
    private Button buttonShoplist;
    private Button buttonAddRecipe;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		buttonPersonalSearch = (Button) findViewById(R.id.buttonPersonalSearchID);
		buttonPersonalSearch.setOnClickListener(this);

        buttonAllRecipes = (Button) findViewById(R.id.buttonAllRecipesID);
        buttonAllRecipes.setOnClickListener(this);

        buttonPreferences = (Button) findViewById(R.id.buttonPreferencesID);
        buttonPreferences.setOnClickListener(this);

        buttonShoplist = (Button) findViewById(R.id.buttonShoplistID);
        buttonShoplist.setOnClickListener(this);

        buttonAddRecipe = (Button) findViewById(R.id.buttonAddRecipeID);
        buttonAddRecipe.setOnClickListener(this);

		importBdd();

		DevOpenHelper helper = new DevOpenHelper(this, "cookeasybdd",
				null);

		db = helper.getWritableDatabase();

		//Initialisation des Daos
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		recetteDao = MainActivity.daoSession.getRecetteDao();
		quantiteDao = MainActivity.daoSession.getQuantiteDao();
		ingredientDao = MainActivity.daoSession.getIngredientDao();
		categorieDao = MainActivity.daoSession.getCategorieDao();
		listeDao = MainActivity.daoSession.getListeDao();
		mesureDao = MainActivity.daoSession.getMesureDao();

	}
	

	private void importBdd(){
		dbImp = new MyDatabase(this);
		dbImp.getReadableDatabase();
		dbImp.close();
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

            case R.id.buttonPersonalSearchID:
                intent = new Intent(MainActivity.this, ActivityPersonalSearch.class);
                startActivity(intent);
                break;

            case R.id.buttonAllRecipesID:
                intent = new Intent(MainActivity.this, ActivityAllRecipes.class);
                startActivity(intent);
                break;

            case R.id.buttonPreferencesID:
                intent = new Intent(MainActivity.this, ActivityPreferences.class);
                startActivity(intent);
                break;

            case R.id.buttonShoplistID:
                intent = new Intent(MainActivity.this, ActivityShoplist.class);
                startActivity(intent);
                break;

            case R.id.buttonAddRecipeID:
                intent = new Intent(MainActivity.this, ActivityAddRecipeStep1.class);
                startActivity(intent);
                break;




        }

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		db.close();
		super.onDestroy();
	}

}
