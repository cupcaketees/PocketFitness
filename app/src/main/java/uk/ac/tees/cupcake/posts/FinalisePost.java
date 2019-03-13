package uk.ac.tees.cupcake.posts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;



import uk.ac.tees.cupcake.R;

public class FinalisePost extends AppCompatActivity {
    private static final String TAG = "FinalisePost";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_post);



        defineImage();
        initialise();

        Log.d(TAG, "onCreate: got the chosen image" + getIntent().getStringExtra("Selected_image"));
    }

    private void initialise() {
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        TextView post = findViewById(R.id.postFinalise);
        post.setOnClickListener(v -> {
            //upload image to account

        });
    }




    private void defineImage() {
        Intent intent = getIntent();
        ImageView imageView = findViewById(R.id.imageShare);
        String mAppend = "file:/";
        Bitmap bitmap;
        if(intent.hasExtra("selected_image")) {
            UniversalImageLoader.setImage(intent.getStringExtra("Selected_image"), imageView , null, mAppend);
        } else {
            bitmap = intent.getParcelableExtra("selected_bitmap");
            imageView.setImageBitmap(bitmap);

        }

    }



}
