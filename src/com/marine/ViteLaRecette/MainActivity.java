package com.marine.ViteLaRecette;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.marine.ViteLaRecette.dao.*;
import com.marine.ViteLaRecette.database.MyDatabase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Marine on 11/01/2015.
 */
public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;

    private MyDatabase dbImp;
    static SQLiteDatabase db;
    private DaoMaster daoMaster;
    static DaoSession daoSession;
    static RecetteDao recetteDao;
    static QuantiteDao quantiteDao;
    static IngredientDao ingredientDao;
    static CategorieDao categorieDao;
    static ListeDao listeDao;
    static MesureDao mesureDao;
    private int check;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmain);

        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mMenuTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }



        importBdd();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "cookeasybdd", null);
        db = helper.getWritableDatabase();

        //init Daos
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        recetteDao = MainActivity.daoSession.getRecetteDao();
        quantiteDao = MainActivity.daoSession.getQuantiteDao();
        ingredientDao = MainActivity.daoSession.getIngredientDao();
        categorieDao = MainActivity.daoSession.getCategorieDao();
        listeDao = MainActivity.daoSession.getListeDao();
        mesureDao = MainActivity.daoSession.getMesureDao();



    }

    private void importBdd(){
        dbImp = new MyDatabase(this);
        dbImp.getReadableDatabase();
        dbImp.close();
    }

    private void showToast(){

        Context context = getApplicationContext();

        CharSequence text = "Pour quitter,\nappuyez à nouveau sur le bouton retour";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);

        toast.show();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            mDrawerLayout.closeDrawer(mDrawerList);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectItem(position);
                }
            }, 300);
        }
    }

    private void selectItem(int position) {

        Fragment fragment;
        FragmentManager fragmentManager;
        Bundle args = new Bundle();

        switch (position) {

            case 0:
                fragment = new PersonnalSearchFragment();
                args.putInt(AllRecipesFragment.MENU_POSITION, position);
                fragment.setArguments(args);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

            case 1:
                fragment = new AllRecipesFragment();
                args.putInt(AllRecipesFragment.MENU_POSITION, position);
                fragment.setArguments(args);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }




    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        db.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        check = check + 1;

        if (check == 1) {

            showToast();
            myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    check = 0;
                }
            }, 2000);
        }

        if(check==2){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
