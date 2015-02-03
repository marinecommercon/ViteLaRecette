package com.marine.ViteLaRecette;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.marine.ViteLaRecette.adapter.AdapterCourses;
import com.marine.ViteLaRecette.dao.Liste;
import com.marine.ViteLaRecette.dao.Recette;

import java.util.ArrayList;
import java.util.List;

public class ActivityShoplist extends Activity {


    //initShoplist
    private List<Liste> recipesItems;
    private AdapterCourses recipesAdapter;

    //setOnclick_Recipes()
    private ListView listviewRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init UI
        setContentView(R.layout.activity_shoplist);
        listviewRecipes = (ListView) findViewById(R.id.shoplistRecipes);

        loadRecipes();
        setOnclick_Recipes();

    }


    private void loadRecipes() {

        recipesItems = MainActivity.listeDao.loadAll();
        recipesAdapter = new AdapterCourses(this, recipesItems);
        listviewRecipes.setAdapter(recipesAdapter);

    }

    private void setOnclick_Recipes() {


        listviewRecipes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ActivityShoplist.this).create();

                alertDialog.setMessage("Retirer de la liste ?");

                alertDialog.setButton("Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                alertDialog.setButton2("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MainActivity.listeDao.delete(recipesItems.get(arg2));
                        loadRecipes();

                    }
                });
                alertDialog.setButton3("Detail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent1 = new Intent(ActivityShoplist.this, ActivityDetailRecipe.class);
                                intent1.putExtra("ID", recipesItems.get(arg2).getRecette().getId().intValue());
                                startActivity(intent1);
                            }
                        });

                alertDialog.show();

                return true;

            }
        });


        listviewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {

                int position = arg2;
                int nb = recipesItems.get(arg2).getNombre().intValue();

                Intent intent = new Intent(ActivityShoplist.this,ActivityShoplistSteps.class);
                intent.putExtra("POS", (int) position);
                intent.putExtra("NB", (int) nb);

                startActivity(intent);
            }
        });


    }

}
