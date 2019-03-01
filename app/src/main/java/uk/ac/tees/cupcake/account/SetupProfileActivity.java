package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/*
 * Setup Profile Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class SetupProfileActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mFirstNameEditText = findViewById(R.id.setup_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.setup_profile_last_name_edit_text);
    }
    /*
     * Sends user to homepage activity if required fields are completed
     */
    public void finishSetup(){

        String userInputFirstName = mFirstNameEditText.getText().toString().trim();
        String userInputLastName = mLastNameEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputFirstName)){
            Toast.makeText(SetupProfileActivity.this,"You must enter your first name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userInputLastName)){
            Toast.makeText(SetupProfileActivity.this,"You must enter your last name", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(SetupProfileActivity.this, HomeActivity.class));
        }
    }
}
