package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

public class VerifyEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        setTitle("Verify Email Address");
        mAuth = FirebaseAuth.getInstance();
    }

    /*
     * Sends email instructions to current users registered email address.
     */
    public void verifyEmail(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VerifyEmailActivity.this, "Email instructions have been sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VerifyEmailActivity.this, HomeActivity.class));
                        }else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(VerifyEmailActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
