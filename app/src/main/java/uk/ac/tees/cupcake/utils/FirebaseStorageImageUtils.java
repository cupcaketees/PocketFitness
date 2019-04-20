package uk.ac.tees.cupcake.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class FirebaseStorageImageUtils {
    
    private FirebaseStorageImageUtils() {
    
    }
    
    public static void storeFile(Context context, String id, String directory, Uri fileUri, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        
        firebaseStorage.getReference()
                .child(directory)
                .child(id)
                .putFile(fileUri)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show());
    }
    
}