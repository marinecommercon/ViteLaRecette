package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcommercon on 09/10/14.
 */
public class ActivityAddRecipeStep2 extends Activity {


    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_step2);
        initUI(this);

    }


    protected void initUI(Activity a) {

        //Init button
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddRecipeStep2.this,
                        ActivityAddRecipeStep3.class);
                startActivity(intent);
            }
        });

        System.out.println(getSharedPreferences("ADD_RECIPE",0).getString("ADD_RECIPE_NAME", null));


    }

}