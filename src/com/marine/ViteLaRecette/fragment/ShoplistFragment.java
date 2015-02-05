package com.marine.ViteLaRecette.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.adapter.AdapterCourses;
import com.marine.ViteLaRecette.dao.Liste;

import java.util.List;

/**
 * Created by Marine on 05/02/2015.
 */
public class ShoplistFragment extends Fragment {

    //initShoplist
    private List<Liste> recipesItems;
    private AdapterCourses recipesAdapter;

    //setOnclick_Recipes()
    private ListView listviewRecipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_shoplist, container, false);
        listviewRecipes = (ListView) rootView.findViewById(R.id.shoplistRecipes);

        loadRecipes();
        setOnclick_Recipes();

        return rootView;
    }


    private void loadRecipes() {

        recipesItems = MainActivity.listeDao.loadAll();
        recipesAdapter = new AdapterCourses(getActivity(), recipesItems);
        listviewRecipes.setAdapter(recipesAdapter);

    }

    private void setOnclick_Recipes() {

        listviewRecipes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();

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

                                Fragment fragment;
                                FragmentManager fragmentManager;
                                Bundle bundle = new Bundle();

                                bundle.putInt("ID", (int) recipesItems.get(arg2).getRecette().getId().intValue());

                                fragment = new DetailRecipeFragment();
                                fragment.setArguments(bundle);

                                fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
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

                Fragment fragment;
                FragmentManager fragmentManager;
                Bundle bundle = new Bundle();

                bundle.putInt("POS", (int) position);
                bundle.putInt("NB", (int) nb);

                fragment = new ShoplistStepsFragment();
                fragment.setArguments(bundle);

                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });


    }

}