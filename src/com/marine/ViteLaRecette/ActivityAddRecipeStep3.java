package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.marine.ViteLaRecette.dao.Quantite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcommercon on 09/10/14.
 */
public class ActivityAddRecipeStep3 extends Activity {

    private Button buttonNext;
    private EditText edittextSteps;
    private String steps;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_step3);

        initUI(this);

    }


    protected void initUI(Activity a) {

        edittextSteps = (EditText) findViewById(R.id.edittextSteps);

        //Init button
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddRecipeStep3.this,ActivityAddRecipeStep4.class);

                resetPrefs();
                steps = edittextSteps.getText().toString();
                addPreferences();

                startActivity(intent);
            }
        });

    }

    private void addPreferences(){

        preferences =  getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_STEPS", steps);
        editor.commit();
    }

    private void resetPrefs(){
        preferences =  getApplicationContext().getSharedPreferences("ADD_RECIPE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADD_RECIPE_STEPS", "");
        editor.commit();

    }



}