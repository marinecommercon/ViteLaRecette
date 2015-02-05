package com.marine.ViteLaRecette.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.marine.ViteLaRecette.ActivityAddRecipeStep4;
import com.marine.ViteLaRecette.R;

/**
 * Created by Marine on 05/02/2015.
 */
public class AddRecipeStep3Fragment extends Fragment {

    private Button buttonNext;
    private EditText edittextSteps;
    private String steps;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_add_recipe_step3, container, false);
        buttonNext = (Button) rootView.findViewById(R.id.buttonNext);
        edittextSteps = (EditText) rootView.findViewById(R.id.edittextSteps);

        initUI(getActivity());

        return rootView;
    }

    protected void initUI(Activity a) {

        //Init button
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ActivityAddRecipeStep4.class);

                resetPrefs();
                steps = edittextSteps.getText().toString();
                addPreferences();

                startActivity(intent);
            }
        });

    }

    private void addPreferences(){

        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_STEPS", steps);
        editor.commit();
    }

    private void resetPrefs(){
        preferences =  getActivity().getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_STEPS", "");
        editor.commit();

    }
}