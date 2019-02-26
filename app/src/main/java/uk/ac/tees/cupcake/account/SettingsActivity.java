package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.ac.tees.cupcake.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    /*
     * Takes user to Account activity
     */
    public void goToAccountActivity(View view){
        Intent intent = new Intent(this, AccountSettingsActivity.class);
        startActivity(intent);
    }
}
