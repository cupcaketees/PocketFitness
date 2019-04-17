package uk.ac.tees.cupcake.posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.xml.transform.Result;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.maps.UserPostLocationActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/*
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class FinalisePost extends AppCompatActivity {
    private static final String TAG = "FinalisePost";
    private FirebaseFirestore firebaseFirestore;
    private String mCurrentUserId;
    private ImageView imageView;
    private EditText mText;
    private String postPictureURL;
    private ImageView backArrow;
    private TextView shareButton;
    private ProgressBar shareProgress;
    private Intent imageIntent;
    private Uri uri;
    private TextView textView;
  
    private final String FIRST_NAME_KEY = "firstName";
    private final String LAST_NAME_KEY = "lastName";
    private final String PROFILE_PHOTO_KEY = "profilePictureUrl";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mText = findViewById(R.id.finaliseDescription);
        shareButton = findViewById(R.id.postFinalise);
        backArrow = findViewById(R.id.backArrow);
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        shareProgress = findViewById(R.id.shareProgressBar);
        imageView = findViewById(R.id.imageShare);
        imageIntent = getIntent();
        textView = findViewById(R.id.textLocation);
        defineImage();
        initialise();

        Log.d(TAG, "onCreate: got the chosen image" + getIntent().getStringExtra("Selected_image"));
    }

    /**
     * Initialise the buttons
     */
    private void initialise() {
        backArrow.setOnClickListener(v -> finish());
        shareButton.setOnClickListener(v -> { setEnabled(false, View.VISIBLE); postImagesToStorage();
        });


        Button button = findViewById(R.id.select_location);
        Intent intent = new Intent(this, UserPostLocationActivity.class);

        button.setOnClickListener(v -> startActivityForResult(intent, 1));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String result = data.getStringExtra("Location");
            textView.setText(result);
            Log.d(TAG, "onActivityResult: " + result);
        } else {
            Toast.makeText(this, "No Location Selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Stores image in cloud storage.
     * Depending on if its a gallery/camera or text post different functions are ran.
     * It'll then save the image to the Cloud storage with the date as its name. It also returns a URL for the Realtime database
     */
    private void postImagesToStorage () {

        StorageReference userFilePath = FirebaseStorage.getInstance().getReference().child("Posts Images").child(mCurrentUserId).child(formattedDate() + ".jpg");
        if(!imageIntent.hasExtra("test_post")) {
            if (imageIntent.hasExtra("Selected_image")) {
                Bitmap bitmap_gallery = convertToBitmap(imageIntent.getStringExtra("Selected_image"));
                uri = getImageUri(FinalisePost.this, bitmap_gallery);
            } else {
                uri = getImageUri(FinalisePost.this, getBitmapCamera());
            }
        }

        userFilePath.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postPictureURL = task.getResult().getDownloadUrl().toString();
                savePost();
            } else {
                Toast.makeText(FinalisePost.this, "image not uploaded", Toast.LENGTH_SHORT).show();
            }
            setEnabled(true, View.INVISIBLE);
        });
    }

    /**
     * This saves the post to a collection called User Post which each user has.
     * If successful returns to {@link MainActivity}
     */
    private void savePost() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(mCurrentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        String firstName = documentSnapshot.getString(FIRST_NAME_KEY);
                        String lastName = documentSnapshot.getString(LAST_NAME_KEY);
                        String profilePictureUrl = documentSnapshot.getString(PROFILE_PHOTO_KEY);

                        String postId = formattedDate();

                        Post post = new Post(mCurrentUserId, postPictureURL, mText.getText().toString(), firstName, lastName, profilePictureUrl, postId);

                        firebaseFirestore.collection("Users")
                                         .document(mCurrentUserId)
                                         .collection("User Posts")
                                         .document(postId)
                                         .set(post)
                                         .addOnSuccessListener(aVoid -> IntentUtils.invokeBaseView(FinalisePost.this, MainActivity.class))
                                         .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    /**
     * This sets the image on the page depending on where it came from
     */
    private void defineImage() {
        String mAppend = "file:/";
        if (getIntent().hasExtra("Selected_image")) {
            UniversalImageLoader.setImage(getIntent().getStringExtra("Selected_image"), imageView, null, mAppend);
        } else {
            Bitmap uri = null;
            try {
                uri = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), getIntent().getParcelableExtra("selected_uri"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(uri);
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap convertToBitmap(String imageURL) {
        File imageFile = new File(imageURL);
        FileInputStream fis = null;
        Bitmap bitmap = null;

        try {
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "convertToBitmap:  FileNotFoundException " + e.getMessage());
        } finally {
            try {
                Objects.requireNonNull(fis).close();
            } catch (IOException e) {
                Log.e(TAG, "convertToBitmap:  FileNotFoundException " + e.getMessage());
            }
        }
        return bitmap;
    }

    private void setEnabled(boolean b, int v) {
        backArrow.setEnabled(b);
        shareButton.setClickable(b);
        mText.setClickable(b);
        shareProgress.setVisibility(v);
    }

    private Bitmap getBitmapCamera () {
        Bitmap bitmap_camera = null;
        try {
            bitmap_camera = MediaStore.Images.Media
                    .getBitmap(this.getContentResolver(), imageIntent.getParcelableExtra("selected_uri"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap_camera;
    }

    private String formattedDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.UK);
        return dateFormat.format(date);
    }
}