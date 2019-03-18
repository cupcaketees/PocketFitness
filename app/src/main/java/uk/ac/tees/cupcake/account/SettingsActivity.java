package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.ac.tees.cupcake.R;

/**
 * Settings Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
    }

    /**
     * Intent to account settings activity
     */
    public void accountSettingsOnClick(View view){startActivity(new Intent(SettingsActivity.this, AccountSettingsActivity.class));}
}
