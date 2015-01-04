package com.marine.ViteLaRecette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao.Properties;

public class ActivityDetailRecipe extends Activity {

    private Button buttonAddShoplist;
    private Button buttonFavorites;
    private Button buttonGoToSteps;
    private Recette recipe;


    private final String TAG = getClass().getSimpleName().toString();
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private SharedPreferences prefs;
    private int recetteID = 0;

    private ListView listViewIngredients;
    private ArrayAdapter<String> adapterListviewIngredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences(TAG, MODE_PRIVATE);
        recetteID = this.getIntent().getIntExtra("ID", 0);
        setContentView(R.layout.activity_detail_recipe);

        recipe = MainActivity.recetteDao.queryBuilder()
                .where(Properties.Id.eq(recetteID))
                .unique();

        final List<Quantite> listOfIngredients = MainActivity.quantiteDao._queryRecette_QuantDeRec(recetteID);
        final ArrayList<String> quantities = new ArrayList();
        final int listOfIngredientsSize = listOfIngredients.size();

        //Initialization of UI

        TextView recipeName = (TextView) findViewById(R.id.textViewNameID);
        TextView recipeCookingTime = (TextView) findViewById(R.id.textViewCookingTimeID);
        TextView recipePreparationTime = (TextView) findViewById(R.id.textViewPreparationTimeID);
        TextView recipeLevel = (TextView) findViewById(R.id.textViewLevelID);
        TextView recipePrice = (TextView) findViewById(R.id.textViewPriceID);

        listViewIngredients = (ListView) findViewById(R.id.listViewIngredientsID);

        recipeName.setText(recipe.getNom());
        recipeCookingTime.setText("Temps de cuisson : " + recipe.getCuisson() + " min");
        recipePreparationTime.setText("Temps de preparation : " + recipe.getPreparation());
        recipeLevel.setText("Difficulte : " + translateToString(recipe.getDifficulte(), "difficulty"));
        recipePrice.setText("Cout : " + translateToString(recipe.getPrix(), "price"));


        //Initialization List of ingredients

        listViewIngredients = (ListView) findViewById(R.id.listViewIngredientsID);
        adapterListviewIngredients = new ArrayAdapter<String>(this, R.layout.item_petit, quantities);
        listViewIngredients.setAdapter(adapterListviewIngredients);
        initQuantity(listOfIngredients, quantities);
        resizeListviewIngredients();


        listViewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                alertDialogClickOnIngredient(arg2, listOfIngredients);
            }
        });


        //Handel errors

        final AlertDialog alertDialogWrongNumber = new AlertDialog.Builder(ActivityDetailRecipe.this).create();
        alertDialogWrongNumber.setMessage("Le nombre entre n'est pas correct");
        alertDialogWrongNumber.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });


        //Button AddShoplist

        final AlertDialog alertDialogChangeQuantity = new AlertDialog.Builder(ActivityDetailRecipe.this).create();
        alertDialogChangeQuantity.setMessage("Combien de personnes vont deguster ce plat  ?");

        final EditText numberChoosenByUser = new EditText(ActivityDetailRecipe.this);
        numberChoosenByUser.setInputType(InputType.TYPE_CLASS_NUMBER);

        buttonAddShoplist = (Button) findViewById(R.id.buttonAddShoplistID);
        buttonAddShoplist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialogChangeQuantity.show();
            }
        });


        // Button AddShoplist => Adapt the quantities

        alertDialogChangeQuantity.setView(numberChoosenByUser);
        alertDialogChangeQuantity.setButton(DialogInterface.BUTTON_POSITIVE, "Ajuster les quantites", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String numberString = numberChoosenByUser.getText().toString().trim();
                int numberInt = Integer.parseInt(numberString);
                int FirstNumberInt = -1;

                if (numberString.length() > 0) {
                    String firstNumberString = numberChoosenByUser.getText().toString().trim().substring(0, 1);
                    FirstNumberInt = Integer.parseInt(firstNumberString);
                }

                if (numberString.equals("") || numberString.equals("0") || numberInt <= 0 || FirstNumberInt == 0) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                } else {
                    quantities.clear();
                    for (int i = 0; i < listOfIngredientsSize; i++) {
                        double quantityModified = Math.floor(((double) (listOfIngredients.get(i).getQuantite() * numberInt / listOfIngredients.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + quantityModified;
                        String S = "" + listOfIngredients.get(i).getSuffixe();
                        if (quantityModified == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantities.add((String) (Q + " " + listOfIngredients.get(i).getMesure().getNom() + listOfIngredients.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapterListviewIngredients);
                    }

                }
                return;
            }
        });


        // Button AddShoplist => Adapt and add to the shoplist

        alertDialogChangeQuantity.setButton(DialogInterface.BUTTON_NEGATIVE, "Ajouter a la \n liste de course", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String numberString = numberChoosenByUser.getText().toString().trim();
                int numberInt = Integer.parseInt(numberString);
                int FirstNumberInt = -1;

                if (numberString.length() > 0) {
                    String firstNumberString = numberChoosenByUser.getText().toString().trim().substring(0, 1);
                    FirstNumberInt = Integer.parseInt(firstNumberString);
                }

                if (numberString.equals("") || numberString.equals("0") || numberInt <= 0 || FirstNumberInt == 0) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                } else {
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
                        listViewIngredients.setAdapter(adapterListviewIngredients);

                         }
                    Liste liste = new Liste(null, numberInt, recipe.getId());
                    MainActivity.listeDao.insert(liste);

                }
                return;
            }
        });


        // Button Favorites

        buttonFavorites = (Button) findViewById(R.id.buttonFavoritesID);
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



        buttonGoToSteps = (Button)findViewById(R.id.buttonGoToStepsID);
        buttonGoToSteps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailRecipe.this,
                        ActivityDetailRecipeSteps.class);
                intent.putExtra("ID",recetteID);
                startActivity(intent);
            }
        });


    }


    public void initQuantity(List<Quantite> quantite, ArrayList<String> quantites) {

        for (int i = 0; i < quantite.size(); i++) {
            String Q = "" + quantite.get(i).getQuantite();
            String S = "" + quantite.get(i).getSuffixe();

            if (quantite.get(i).getQuantite() == 0.0) {
                Q = "";
            }
            if (S.trim().equals("null")) {
                S = "";
            }
            quantites.add((String) (Q + " " + quantite.get(i).getMesure().getNom() + quantite.get(i).getIngredient().getNom() + " " + S));
        }
    }

    public void alertDialogClickOnIngredient(int arg2, List<Quantite> quantite) {

        final int arg = arg2;
        final List<Quantite> quantiteSelected = quantite;

        AlertDialog alertDialog = new AlertDialog.Builder(ActivityDetailRecipe.this).create();

        alertDialog.setMessage("Ingredient : " + quantite.get(arg2).getIngredient().getNom() + "\nCategorie : " + quantite.get(arg2).getIngredient().getCategorie().getNom());
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        if(quantiteSelected.get(arg).getIngredient().getFavoris()==0) {

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Bannir l'ingredient", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    quantiteSelected.get(arg).getIngredient().setFavoris(-1);
                    MainActivity.ingredientDao.update(quantiteSelected.get(arg).getIngredient());
                }
            });
        }

        else if(quantiteSelected.get(arg).getIngredient().getFavoris()==-1) {

            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Restaurer l'ingredient", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    quantiteSelected.get(arg).getIngredient().setFavoris(0);
                    MainActivity.ingredientDao.update(quantiteSelected.get(arg).getIngredient());
                }
            });
        }

        alertDialog.show();
    }

    private void changeStateButtonFavorites() {
        switch (recipe.getFavoris()) {
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

    private String translateToString(int rating, String type){

        String label = "";

        if(rating==1 && type.equals("difficulty")){label = "Facile";}
        if(rating==2 && type.equals("difficulty")){label = "Moyen";}
        if(rating==3 && type.equals("difficulty")){label = "Difficile";}
        if(rating==4 && type.equals("difficulty")){label = "Très difficile";}

        if(rating==1 && type.equals("price")){label = "Bon marché";}
        if(rating==2 && type.equals("price")){label = "Moyen";}
        if(rating==3 && type.equals("price")){label = "Très cher";}

        return label;
    }

    private void resizeListviewIngredients(){

        int totalHeight = listViewIngredients.getPaddingTop() + listViewIngredients.getPaddingBottom();
        for (int i = 0; i < adapterListviewIngredients.getCount(); i++) {
            View listItem = adapterListviewIngredients.getView(i, null, listViewIngredients);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewIngredients.getLayoutParams();
        params.height = totalHeight + (listViewIngredients.getDividerHeight() * (adapterListviewIngredients.getCount() - 1));
        listViewIngredients.setLayoutParams(params);

    }

}