package uk.ac.tees.cupcake;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class NavigationBarActivity extends AppCompatActivity {

    private NavigationView navView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        navView = findViewById(R.id.nav_view);
        navView
    }

}