package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao.Properties;

public class ActivityDetailRecipe extends Activity implements OnClickListener {

	private Button buttonAddShoplist;
	private Button buttonFavorites;
	private Recette recipe;


	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int iDRecette = this.getIntent().getIntExtra("ID", 0);

		setContentView(R.layout.activity_detail_recipe);

		recipe = MainActivity.recetteDao.queryBuilder()
				.where(Properties.Id.eq(iDRecette))
				.unique();

		final List <Quantite> listOfIngredients = MainActivity.quantiteDao._queryRecette_QuantDeRec(iDRecette);
		final ArrayList <String> quantities = new ArrayList();
		final int listOfIngredientsSize=listOfIngredients.size();

        TextView recipeName = (TextView)findViewById(R.id.textViewNameID);
        TextView recipeCookingTime = (TextView)findViewById(R.id.textViewCookingTimeID);
        TextView recipePreparationTime = (TextView)findViewById(R.id.textViewPreparationTimeID);
        TextView recipeLevel = (TextView)findViewById(R.id.textViewLevelID);
        TextView recipePrice = (TextView)findViewById(R.id.textViewPriceID);
        TextView recipeDescription = (TextView)findViewById(R.id.textViewDescriptionID);

        recipeName.setText(recipe.getNom());
        recipeCookingTime.setText("Temps de cuisson : " + recipe.getCuisson() + " min");
        recipePreparationTime.setText("Temps de preparation : " + recipe.getPreparation());
        recipeLevel.setText("Difficulte : " + recipe.getDifficulte());
        recipePrice.setText("Cout : " + recipe.getPrix());
        recipeDescription.setText(""+ recipe.getDescription());



        //List of ingredients, UI and alertDialog
        final ListView listViewIngredients = (ListView)findViewById(R.id.listViewIngredientsID);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_petit, quantities);
        listViewIngredients.setAdapter(adapter);

        initQuantity(listOfIngredients, quantities);

        listViewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                      long arg3) {
                  alertDialogClickOnIngredient(arg2, listOfIngredients);
              }
          });

        final AlertDialog alertDialogWrongNumber = new AlertDialog.Builder(ActivityDetailRecipe.this).create();
        alertDialogWrongNumber.setMessage("Le nombre entre n'est pas correct");
        alertDialogWrongNumber.setButton(DialogInterface.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });



        //Button AddShoplist
        final AlertDialog alertDialogChangeQuantity = new AlertDialog.Builder(ActivityDetailRecipe.this).create();
        alertDialogChangeQuantity.setMessage("Combien de personnes vont deguster ce plat  ?");

        final EditText numberChoosenByUser = new EditText(ActivityDetailRecipe.this);
        numberChoosenByUser.setInputType(InputType.TYPE_CLASS_NUMBER);

        alertDialogChangeQuantity.setView(numberChoosenByUser);
        alertDialogChangeQuantity.setButton(DialogInterface.BUTTON_POSITIVE,"Ajuster les quantites", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String numberString = numberChoosenByUser.getText().toString().trim();
                int numberInt = Integer.parseInt(numberString);
                int FirstNumberInt=-1;

                if(numberString.length()>0) {
                    String firstNumberString = numberChoosenByUser.getText().toString().trim().substring(0, 1);
                    FirstNumberInt = Integer.parseInt(firstNumberString);
                }

                if (numberString.equals("") || numberString.equals("0") || numberInt<=0 || FirstNumberInt==0 ) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                }
                else {
                    quantities.clear();
                    for (int i = 0; i < listOfIngredientsSize; i++) {
                        double Qajustee = Math.floor(((double) (listOfIngredients.get(i).getQuantite() * numberInt / listOfIngredients.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + Qajustee;
                        String S = "" + listOfIngredients.get(i).getSuffixe();
                        if (Qajustee == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantities.add((String) (Q + " " + listOfIngredients.get(i).getMesure().getNom() + listOfIngredients.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapter);
                    }

                }
                return;
            }
        });

        alertDialogChangeQuantity.setButton(DialogInterface.BUTTON_NEGATIVE,"Ajouter a la \n liste de course", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String numberString = numberChoosenByUser.getText().toString().trim();
                int numberInt = Integer.parseInt(numberString);
                int FirstNumberInt=-1;

                if(numberString.length()>0) {
                    String firstNumberString = numberChoosenByUser.getText().toString().trim().substring(0, 1);
                    FirstNumberInt = Integer.parseInt(firstNumberString);
                }

                if (numberString.equals("") || numberString.equals("0") || numberInt<=0 || FirstNumberInt==0 ) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                }
                else {
                    quantities.clear();
                    for (int i = 0; i < listOfIngredientsSize; i++) {
                        double ajustedQuantity = Math.floor(((double) (listOfIngredients.get(i).getQuantite() * numberInt / listOfIngredients.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + ajustedQuantity;
                        String S = "" + listOfIngredients.get(i).getSuffixe();
                        if (ajustedQuantity == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantities.add((String) (Q + " " + listOfIngredients.get(i).getMesure().getNom() + listOfIngredients.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapter);
                    }
                    Liste liste = new Liste(null, numberInt, recipe.getId());
                    MainActivity.listeDao.insert(liste);

                }
                return;
            }
        });




        buttonFavorites = (Button)findViewById(R.id.buttonFavoritesID);
        changeStateButtonFavorites();
        buttonFavorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (recipe.getFavoris()) {
                    case -1:
                        recipe.setFavoris(0);
                        MainActivity.recetteDao.update(recipe);
                        break;
                    case 0:
                        recipe.setFavoris(1);
                        MainActivity.recetteDao.update(recipe);
                        break;
                    case 1:
                        recipe.setFavoris(-1);
                        MainActivity.recetteDao.update(recipe);
                        break;
                }
                changeStateButtonFavorites();
            }
        });



        buttonAddShoplist = (Button)findViewById(R.id.buttonAddShoplistID);
        buttonAddShoplist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialogChangeQuantity.show();
            }
        });


    }



    public void initQuantity(List <Quantite> quantite, ArrayList <String> quantites){

        for(int i = 0; i<quantite.size() ;i++){
            String Q=""+quantite.get(i).getQuantite();
            String S=""+quantite.get(i).getSuffixe();

            if(quantite.get(i).getQuantite()==0.0){
                Q="";
            }
            if(S.trim().equals("null")){
                S="";
            }
            quantites.add((String)(Q+" "+quantite.get(i).getMesure().getNom()+quantite.get(i).getIngredient().getNom()+" "+S));
        }
    }

    public void alertDialogClickOnIngredient(int arg2, List <Quantite> quantite){

        final int arg = arg2;
        final List <Quantite> quantiteSelected = quantite;

        AlertDialog alertDialog = new AlertDialog.Builder(ActivityDetailRecipe.this).create();

        alertDialog.setMessage("Ingredient : " + quantite.get(arg2).getIngredient().getNom() + "\nCategorie : " + quantite.get(arg2).getIngredient().getCategorie().getNom());
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Bannir l'ingredient", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                quantiteSelected.get(arg).getIngredient().setFavoris(-1);
                MainActivity.ingredientDao.update(quantiteSelected.get(arg).getIngredient());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Restaurer l'ingredient", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                quantiteSelected.get(arg).getIngredient().setFavoris(0);
                MainActivity.ingredientDao.update(quantiteSelected.get(arg).getIngredient());
            }
        });
        alertDialog.show();
    }

    private void changeStateButtonFavorites(){
            switch(recipe.getFavoris()){
                case -1:
                    buttonFavorites.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
                case 0:
                    buttonFavorites.setBackgroundColor(getResources().getColor(R.color.blue));
                    break;
                case 1:
                    buttonFavorites.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
            }
        }

    @Override
    public void onClick(View arg0) {
    }


}