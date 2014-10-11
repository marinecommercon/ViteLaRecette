package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by mcommercon on 09/10/14.
 */
public class ActivityAddRecipeStep3 extends Activity {

    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_step3);
        initUI(this);

    }


    protected void initUI(Activity a) {

        //Init button
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddRecipeStep3.this,
                        ActivityAddRecipeStep4.class);
                startActivity(intent);
            }
        });


    }

}