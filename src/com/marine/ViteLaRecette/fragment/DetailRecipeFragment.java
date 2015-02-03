package com.marine.ViteLaRecette.fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marine.ViteLaRecette.MainActivity;
import com.marine.ViteLaRecette.R;
import com.marine.ViteLaRecette.dao.Quantite;
import com.marine.ViteLaRecette.dao.Recette;
import com.marine.ViteLaRecette.dao.RecetteDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marine on 03/02/2015.
 */

public class DetailRecipeFragment extends ListFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int id = bundle.getInt("ID", 0);
            System.out.println("" + id);
        }



    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}