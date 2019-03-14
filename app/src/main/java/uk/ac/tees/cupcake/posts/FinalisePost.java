package uk.ac.tees.cupcake.posts;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;

public class FinalisePost extends AppCompatActivity {
    private static final String TAG = "FinalisePost";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference postImages;
    private Bitmap bitmap;
    private String mCurrentUserId;
    ImageView imageView;
    EditText mText;
    Context context;
    String postPictureURL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        context = FinalisePost.this;
        postImages = FirebaseStorage.getInstance().getReference().child("Posts Images");

        mText = findViewById(R.id.finaliseDescription);

        mCurrentUserId = mAuth.getCurrentUser().getUid();

        imageView = findViewById(R.id.imageShare);
        defineImage();
        initialise();

        Log.d(TAG, "onCreate: got the chosen image" + getIntent().getStringExtra("Selected_image"));
    }

    private void initialise() {
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        TextView post = findViewById(R.id.postFinalise);
        post.setOnClickListener(v -> {

//

//
//            if(TextUtils.isEmpty(mText.toString().trim())){
//                Toast.makeText(FinalisePost.this, validateInput(), Toast.LENGTH_SHORT).show();
//                return;
//            }

            StorageReference userFilePath = postImages.child(mCurrentUserId).child("1.jpg");

            Uri uri =  getImageUri(FinalisePost.this, bitmap);

            userFilePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(FinalisePost.this, "image uploaded", Toast.LENGTH_SHORT).show();

                        postPictureURL = task.getResult().getDownloadUrl().toString();
                        savePost();

                    }else{
                        Toast.makeText(FinalisePost.this, "image not uploaded", Toast.LENGTH_SHORT).show();
                        //todo
                    }
                }
            });

        });
    }



    private void savePost() {
        Post post = new Post(mCurrentUserId, postPictureURL, mText.toString(), Post.getCurrentTimeUsingDate());

        firebaseFirestore.collection("Users").document(mCurrentUserId).collection("User Posts").document("post1").set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });




    }




    private void defineImage() {
        Intent intent = getIntent();


        String mAppend = "file:/";
              if(intent.hasExtra("selected_image")) {
            UniversalImageLoader.setImage(intent.getStringExtra("Selected_image"), imageView , null, mAppend);
        } else {
            bitmap = intent.getParcelableExtra("selected_bitmap");
            imageView.setImageBitmap(bitmap);


        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }





}
