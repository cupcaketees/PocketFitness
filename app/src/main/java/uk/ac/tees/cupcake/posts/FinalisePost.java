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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class FinalisePost extends AppCompatActivity {
    private static final String TAG = "FinalisePost";
    private FirebaseFirestore firebaseFirestore;
    private StorageReference postImages;
    private String mCurrentUserId;
    private ImageView imageView;
    private EditText mText;
    private Context context;
    private String postPictureURL;
    private Intent intent;
    private ImageView backArrow;
    private TextView shareButton;
    private ProgressBar shareProgress;

    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        context = FinalisePost.this;
        postImages = FirebaseStorage.getInstance().getReference().child("Posts Images");

        mText = findViewById(R.id.finaliseDescription);
        shareButton = findViewById(R.id.postFinalise);
        backArrow = findViewById(R.id.backArrow);
        intent = getIntent();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        shareProgress = findViewById(R.id.shareProgressBar);


        imageView = findViewById(R.id.imageShare);
        defineImage();
        initialise();

        Log.d(TAG, "onCreate: got the chosen image" + getIntent().getStringExtra("Selected_image"));
    }

    private void initialise() {
        backArrow.setOnClickListener(v -> finish());

        shareButton.setOnClickListener(v -> { setEnabled(false, View.VISIBLE); postImagesToStorage();

        });
    }

    private void postImagesToStorage () {

        StorageReference userFilePath = postImages.child(mCurrentUserId).child(formattedDate() + ".jpg");

        if(!intent.hasExtra("test_post")) {
            if (intent.hasExtra("Selected_image")) {
                Bitmap bitmap_gallery = convertToBitmap(intent.getStringExtra("Selected_image"));
                uri = getImageUri(FinalisePost.this, bitmap_gallery);
            } else {
                Bitmap bitmap_camera = null;
                try {
                    bitmap_camera = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), intent.getParcelableExtra("selected_uri"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uri = getImageUri(FinalisePost.this, bitmap_camera);
            }
        }

        userFilePath.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FinalisePost.this, "image uploaded", Toast.LENGTH_SHORT).show();
                postPictureURL = task.getResult().getDownloadUrl().toString();
                savePost();
            } else {
                Toast.makeText(FinalisePost.this, "image not uploaded", Toast.LENGTH_SHORT).show();
            }
            setEnabled(true, View.INVISIBLE);

        });
    }

    private void savePost() {
        Post post = new Post(mCurrentUserId, postPictureURL, mText.getText().toString(), Post.getCurrentTimeUsingDate());

        firebaseFirestore.collection("Users").document(mCurrentUserId).collection("User Posts").document(formattedDate()).set(post)
                .addOnSuccessListener(aVoid -> IntentUtils.invokeBaseView(FinalisePost.this, MainActivity.class))
                .addOnFailureListener(e -> Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show());
    }

    private void defineImage() {
        String mAppend = "file:/";
        if (intent.hasExtra("Selected_image")) {
            UniversalImageLoader.setImage(intent.getStringExtra("Selected_image"), imageView, null, mAppend);
        } else {
            Bitmap uri = null;
            try {
                uri = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), intent.getParcelableExtra("selected_uri"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(uri);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap convertToBitmap(String imageURL) {
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

    private String formattedDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.UK);
        return dateFormat.format(date);
    }
}