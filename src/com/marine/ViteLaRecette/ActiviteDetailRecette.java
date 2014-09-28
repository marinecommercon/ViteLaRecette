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

public class ActiviteDetailRecette extends Activity implements OnClickListener {

	private Button buttonAddShoplist;
	private Button buttonFavorites;
	private Recette recette;


	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int iDRecette = this.getIntent().getIntExtra("ID", 0);

		setContentView(R.layout.activity_detail);

		recette = MainActivity.recetteDao.queryBuilder()
				.where(Properties.Id.eq(iDRecette))
				.unique();
		
		final List <Quantite> quantite = MainActivity.quantiteDao._queryRecette_QuantDeRec(iDRecette);
		final ArrayList <String> quantites = new ArrayList();
		final int qSize=quantite.size();

        TextView recipeName = (TextView)findViewById(R.id.textViewNameID);
        TextView recipeCookingTime = (TextView)findViewById(R.id.textViewCookingTimeID);
        TextView recipePreparationTime = (TextView)findViewById(R.id.textViewPreparationTimeID);
        TextView recipeLevel = (TextView)findViewById(R.id.textViewLevelID);
        TextView recipePrice = (TextView)findViewById(R.id.textViewPriceID);
        TextView recipeDescription = (TextView)findViewById(R.id.textViewDescriptionID);

        recipeName.setText(recette.getNom());
        recipeCookingTime.setText("Temps de cuisson : " + recette.getCuisson() + " min");
        recipePreparationTime.setText("Temps de preparation : " + recette.getPreparation());
        recipeLevel.setText("Difficulte : " + recette.getDifficulte());
        recipePrice.setText("Cout : " + recette.getPrix());
        recipeDescription.setText(""+recette.getDescription());


        final ListView listViewIngredients = (ListView)findViewById(R.id.listViewIngredientsID);


  /**      listViewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                      long arg3) {


                  AlertDialog alertDialog = new AlertDialog.Builder(ActiviteDetailRecette.this).create();

                  alertDialog.setMessage("Ingredient : " + quantite.get(arg2).getIngredient().getNom() + "\nCategorie : " + quantite.get(arg2).getIngredient().getCategorie().getNom());

                  alertDialog.setButton("Retour", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          // User clicked OK button

                      }
                  });
                  alertDialog.setButton2("Bannir l'ingr�dient", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          // User clicked OK button
                          quantite.get(arg2).getIngredient().setFavoris(-1);
                          MainActivity.ingredientDao.update(quantite.get(arg2).getIngredient());
                          android.util.Log.e("DetailRecette", "Ok" + quantite.get(arg2).getIngredient().getFavoris());
                      }
                  });
                  alertDialog.setButton3("Restaurer l'ingr�dient", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          // User clicked OK button
                          quantite.get(arg2).getIngredient().setFavoris(0);
                          MainActivity.ingredientDao.update(quantite.get(arg2).getIngredient());
                          android.util.Log.e("DetailRecette", "Ok" + quantite.get(arg2).getIngredient().getFavoris());
                      }
                  });

                  alertDialog.show();


              }

          });




   **/


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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_petit, quantites);
        listViewIngredients.setAdapter(adapter);




        final AlertDialog alertDialogWrongNumber = new AlertDialog.Builder(ActiviteDetailRecette.this).create();
        alertDialogWrongNumber.setMessage("Vous n'avez pas entre un nombre correct !" + "\n" + "La recette n'a pas ete ajustee/ajoutee");
        alertDialogWrongNumber.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });



        final AlertDialog alertDialogChangeQuantity = new AlertDialog.Builder(ActiviteDetailRecette.this).create();
        alertDialogChangeQuantity.setMessage("Combien de personnes vont deguster ce plat  ?");

        final EditText numberChoosenByUser = new EditText(ActiviteDetailRecette.this);
        numberChoosenByUser.setInputType(InputType.TYPE_CLASS_NUMBER);


        alertDialogChangeQuantity.setView(numberChoosenByUser);
        alertDialogChangeQuantity.setButton("Ajuster les quantites", new DialogInterface.OnClickListener() {
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
                    quantites.clear();
                    for (int i = 0; i < qSize; i++) {
                        double Qajustee = Math.floor(((double) (quantite.get(i).getQuantite() * numberInt / quantite.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + Qajustee;
                        String S = "" + quantite.get(i).getSuffixe();
                        if (Qajustee == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantites.add((String) (Q + " " + quantite.get(i).getMesure().getNom() + quantite.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapter);
                    }

                }
                return;
            }
        });

        alertDialogChangeQuantity.setButton2("Ajouter a la \n liste de course", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = numberChoosenByUser.getText().toString().trim();
                String firstNumber = numberChoosenByUser.getText().toString().trim().substring(0,1);

                int a = Integer.parseInt(value);
                int b = Integer.parseInt(firstNumber);

                if (value.equals("") || value.equals("0") || a<=0 || b==0 ) {
                    alertDialogWrongNumber.show();
                }
                else {

                    quantites.clear();
                    for (int i = 0; i < qSize; i++) {
                        double Qajustee = Math.floor(((double) (quantite.get(i).getQuantite() * a / quantite.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + Qajustee;
                        String S = "" + quantite.get(i).getSuffixe();
                        if (Qajustee == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantites.add((String) (Q + " " + quantite.get(i).getMesure().getNom() + quantite.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapter);
                    }
                    Liste liste = new Liste(null, a, recette.getId());
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

                switch (recette.getFavoris()) {
                    case -1:
                        recette.setFavoris(0);
                        MainActivity.recetteDao.update(recette);
                        break;
                    case 0:
                        recette.setFavoris(1);
                        MainActivity.recetteDao.update(recette);
                        break;
                    case 1:
                        recette.setFavoris(-1);
                        MainActivity.recetteDao.update(recette);
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










    private void changeStateButtonFavorites(){
            switch(recette.getFavoris()){
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