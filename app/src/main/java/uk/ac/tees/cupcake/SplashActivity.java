package uk.ac.tees.cupcake;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.login.LoginActivity;

/**
 * Created by HugoT on 13/02/2019.
 * Splash screen Page
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final String TAG = "SplashActivity";

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Checks users current auth state and directs to them to appropriate activity.
        if(currentUser == null){
            // not logged in
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }else{
            FirebaseFirestore.getInstance()
                             .collection("Users")
                             .document(currentUser.getUid())
                             .get()
                             .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                 @Override
                                 public void onSuccess(DocumentSnapshot documentSnapshot) {
                                     if(!documentSnapshot.exists()){
                                         // new account not setup.
                                         startActivity(new Intent(SplashActivity.this, SetupProfileActivity.class));
                                         finish();
                                     }else {
                                         // already setup account.
                                         startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                         finish();
                                     }
                                 }
                             });
        }

        SystemClock.sleep(1000);
        Log.d(TAG, "onCreate: onEnd");
    }
}