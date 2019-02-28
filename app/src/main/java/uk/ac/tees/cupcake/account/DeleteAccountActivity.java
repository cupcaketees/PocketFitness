package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.login.LoginActivity;

public class DeleteAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        setTitle("Delete My Account");
        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.delete_account_password_edit_text);
    }

    /*
     * Authenticate current user using password input, on success attempts to delete user account.
     * TODO Include deletion of google accounts.
     */
    public void deleteAccount(View view){

        String userInputPassword = mPasswordEditText.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(TextUtils.isEmpty(userInputPassword)) {
            Toast.makeText(DeleteAccountActivity.this, "You must enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), userInputPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    currentUser.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(DeleteAccountActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                                        finish();
                                    }else{
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(DeleteAccountActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(DeleteAccountActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

