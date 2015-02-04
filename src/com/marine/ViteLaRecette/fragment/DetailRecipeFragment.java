package com.marine.ViteLaRecette.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.ActivityDetailRecipeSteps;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marine on 03/02/2015.
 */

public class DetailRecipeFragment extends ListFragment {

    private Button buttonAddShoplist;
    private Button buttonFavorites;
    private Button buttonGoToSteps;

    public DetailRecipeFragment() {
    }

    private Recette recipe;


    private final String TAG = getClass().getSimpleName().toString();
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private SharedPreferences prefs;
    private int recetteID = 0;

    private ListView listViewIngredients;
    private ArrayAdapter<String> adapterListviewIngredients;
    private ArrayList<String> quantities = new ArrayList();

    private List<Quantite> listOfIngredients;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recetteID = bundle.getInt("ID", 0);
            System.out.println("détail" + recetteID);
        }
        prefs = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        recipe = MainActivity.recetteDao.queryBuilder()
                .where(RecetteDao.Properties.Id.eq(recetteID))
                .unique();

        listOfIngredients = MainActivity.quantiteDao._queryRecette_QuantDeRec(recetteID);
        initQuantity(listOfIngredients, quantities);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_detail_recipe, container, false);

        TextView recipeName = (TextView) rootView.findViewById(R.id.textViewNameID);
        TextView recipeCookingTime = (TextView) rootView.findViewById(R.id.textViewCookingTimeID);
        TextView recipePreparationTime = (TextView) rootView.findViewById(R.id.textViewPreparationTimeID);
        TextView recipeLevel = (TextView) rootView.findViewById(R.id.textViewLevelID);
        TextView recipePrice = (TextView) rootView.findViewById(R.id.textViewPriceID);
        listViewIngredients = (ListView) rootView.findViewById(R.id.listViewIngredientsID);

        recipeName.setText(recipe.getNom());
        recipeCookingTime.setText("Temps de cuisson : " + recipe.getCuisson() + " min");
        recipePreparationTime.setText("Temps de preparation : " + recipe.getPreparation());
        recipeLevel.setText("Difficulte : " + translateToString(recipe.getDifficulte(), "difficulty"));
        recipePrice.setText("Cout : " + translateToString(recipe.getPrix(), "price"));

        buttonAddShoplist = (Button) rootView.findViewById(R.id.buttonAddShoplistID);
        buttonFavorites = (Button) rootView.findViewById(R.id.buttonFavoritesID);
        buttonGoToSteps = (Button)rootView.findViewById(R.id.buttonGoToStepsID);

        adapterListviewIngredients = new ArrayAdapter<String>(getActivity(), R.layout.item_petit, quantities);
        listViewIngredients.setAdapter(adapterListviewIngredients);

        resizeListviewIngredients();

        listViewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                alertDialogClickOnIngredient(arg2, listOfIngredients);
            }
        });

        addButtons();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Détail de la recette");

        return rootView;
    }



    private void addButtons(){

        //Handel errors for buttons
        final AlertDialog alertDialogWrongNumber = new AlertDialog.Builder(getActivity()).create();
        alertDialogWrongNumber.setMessage("Le nombre entré n'est pas correct");
        alertDialogWrongNumber.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });

        final AlertDialog alertDialogTooMany = new AlertDialog.Builder(getActivity()).create();
        alertDialogTooMany.setMessage("Le nombre entré est trop grand");
        alertDialogTooMany.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });

        //Button AddShoplist
        final AlertDialog alertDialogChangeQuantity = new AlertDialog.Builder(getActivity()).create();
        alertDialogChangeQuantity.setMessage("Combien de personnes vont deguster ce plat  ?");

        final EditText numberChoosenByUser = new EditText(getActivity());
        numberChoosenByUser.setInputType(InputType.TYPE_CLASS_NUMBER);

        buttonAddShoplist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialogChangeQuantity.show();
            }
        });

        // Button AddShoplist => Adapt the quantities
        final int listOfIngredientsSize = listOfIngredients.size();
        alertDialogChangeQuantity.setView(numberChoosenByUser);
        alertDialogChangeQuantity.setButton(DialogInterface.BUTTON_POSITIVE, "Ajuster les quantites", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String numberString = numberChoosenByUser.getText().toString().trim();

                if (numberString.equals("") || numberString.equals("0") || numberString.substring(0, 1).equals("0")) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                }
                else if( Integer.parseInt(numberString) > 9999 ){
                    alertDialogTooMany.show();
                    numberChoosenByUser.getText().clear();}

                else {

                    int numberInt = Integer.parseInt(numberString);
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

                if (numberString.equals("") || numberString.equals("0") || numberString.substring(0, 1).equals("0")) {
                    alertDialogWrongNumber.show();
                    numberChoosenByUser.getText().clear();
                }
                else if( Integer.parseInt(numberString) > 9999 ){
                    alertDialogTooMany.show();
                    numberChoosenByUser.getText().clear();}

                else {

                    int numberInt = Integer.parseInt(numberString);
                    quantities.clear();

                    for (int i = 0; i < listOfIngredientsSize; i++) {

                        double adjustedQuantity = Math.floor(((double) (listOfIngredients.get(i).getQuantite() * numberInt / listOfIngredients.get(i).getRecette().getNombre()) * 10)) / 10;
                        String Q = "" + adjustedQuantity;
                        String S = "" + listOfIngredients.get(i).getSuffixe();
                        if (adjustedQuantity == 0.0) {
                            Q = "";
                        }
                        if (S.trim().equals("null")) {
                            S = "";
                        }
                        quantities.add((String) (Q + " " + listOfIngredients.get(i).getMesure().getNom() + listOfIngredients.get(i).getIngredient().getNom() + " " + S));
                        listViewIngredients.setAdapter(adapterListviewIngredients);

                    }

                    //Remember date and time to make the recipe unique
                    Calendar c = Calendar.getInstance();

                    Liste liste = new Liste(null, numberInt, recipe.getId(), c.getTime().toString());
                    MainActivity.listeDao.insert(liste);

                    showToast();

                }
                return;
            }
        });


        // Button Favorites
        changeStateButtonFavorites();
        buttonFavorites.setOnClickListener(new View.OnClickListener() {
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

        buttonGoToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentManager fragmentManager;

                Bundle bundle = new Bundle();
                bundle.putInt("ID", (int) recetteID);

                fragment = new DetailRecipeStepsFragment();
                fragment.setArguments(bundle);

                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });


    }

    public void alertDialogClickOnIngredient(int arg2, List<Quantite> quantite) {

        final int arg = arg2;
        final List<Quantite> quantiteSelected = quantite;

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

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

    private void showToast(){

        Context context = getActivity().getApplicationContext();

        CharSequence text = "La recette a été ajoutée à la liste de courses";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);

        toast.show();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}