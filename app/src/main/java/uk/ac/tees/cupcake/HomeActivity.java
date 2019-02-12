package uk.ac.tees.cupcake;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.ac.tees.cupcake.Utils.SectionsPagerAdapter;


public class HomeActivity extends AppCompatActivity
       {

    Toolbar toolbar;
    DrawerLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        initialiseView();

    }

    /**
     * Initialises Toolbar  and Drawer
     * Sets the Toolbar for the View
     */
    public void initialiseView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }




}
