package com.marine.ViteLaRecette.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.marine.ViteLaRecette.R;

/**
 * Created by Marine on 05/02/2015.
 */
public class AddRecipeStep1Fragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_add_recipe_step1, container, false);


        return rootView;
    }

}