package uk.ac.tees.cupcake.posts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import uk.ac.tees.cupcake.ApplicationConstants;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.maps.UserPostLocationActivity;
import uk.ac.tees.cupcake.utils.FirebaseStorageImageUtils;

public final class CreatePostActivity extends AppCompatActivity {
    
    private FirebaseUser currentUser;
    
    private Uri localImageUri;
    
    private EditText editText;
    private FABProgressCircle fabProgressCircle;
    private FloatingActionButton removeImageButton;
    
    private ImageView addImageButton;
    private ImageView addLocationButton;
    private ImageView imageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        setTitle("Create Post");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        
        if (currentUser == null) {
            throw new RuntimeException();
        }
        
        editText = findViewById(R.id.create_post_edit_text);
        addImageButton = findViewById(R.id.create_post_add_image);
        addLocationButton = findViewById(R.id.create_post_add_location);
        
        removeImageButton = findViewById(R.id.create_post_remove_image_button);
        fabProgressCircle = findViewById(R.id.create_post_fab_progress_circle);
        fabProgressCircle.attachListener(() -> {
            Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
            intent.putExtra("index", 1);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        
        if (resultCode == RESULT_OK) {
            
            if (requestCode == ApplicationConstants.CREATE_POST_ADD_IMAGE_REQUEST_CODE) {
                imageView = findViewById(R.id.create_post_post_image);

                localImageUri = result.getUri();
                imageView.setImageURI(localImageUri);
                
                imageView.setVisibility(View.VISIBLE);
                removeImageButton.setVisibility(View.VISIBLE);
                
                Animation animation = new ScaleAnimation(
                        1.2f, 1f, 1.2f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, .5f,
                        ScaleAnimation.RELATIVE_TO_SELF, .5f);
                animation.setInterpolator(t -> (float) -(Math.pow(Math.E, -t*5) * Math.cos(20 * t)) + 1);
                animation.setDuration(1500);
    
                imageView.startAnimation(animation);
                
            } else if (requestCode == ApplicationConstants.CREATE_POST_ADD_LOCATION_REQUEST_CODE) {
                String location = data.getStringExtra("Location");
                System.out.println(location);
            }
            
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(CreatePostActivity.this, result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Called upon pressing the post button. Uploads image if there is one attached and then
     * persists the post.
     */
    public void postButtonOnClick(View view) {
        fabProgressCircle.show();
    
        Post post = new Post(currentUser.getUid(), editText.getText().toString());
    
        if (localImageUri != null) {
            String uriString = localImageUri.toString();
            String postImageId = uriString.substring(uriString.indexOf("cropped") + "cropped".length(), uriString.length());
            
            FirebaseStorageImageUtils.storeFile(CreatePostActivity.this, postImageId, "post_images", localImageUri,
                    taskSnapshot -> {
                        post.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                        insertPost(post);
                    });
        
        } else {
        
            insertPost(post);
        }
    }
    
    /**
     * Adds a post to firestore database, allows firebase to auto generate an id.
     *
     * @param post the post to persist.
     */
    private void insertPost(Post post) {
        DocumentReference userPostsDocument = FirebaseFirestore
                .getInstance()
                .collection("Users/" + currentUser.getUid() + "/User Posts/")
                .document();
    
        post.setPostId(userPostsDocument.getId());
        
        userPostsDocument
                .set(post)
                .addOnSuccessListener(documentReference -> {
                    Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
                    intent.putExtra("index", 1);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
    /**
     * Called upon clicking the add image button. Takes the user to the image cropping view.
     */
    public void addImageOnClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(CreatePostActivity.this, R.anim.create_post_button_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            
            }
        
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .getIntent(CreatePostActivity.this);
    
                startActivityForResult(intent, ApplicationConstants.CREATE_POST_ADD_IMAGE_REQUEST_CODE);
            }
        
            @Override
            public void onAnimationRepeat(Animation animation) {
            
            }
        });
        addImageButton.startAnimation(animation);
    }
    
    /**
     * Called upon clicking the image removal button. Removes the attached image.
     */
    public void removeImageOnClick(View view) {
        ScaleAnimation anim = new ScaleAnimation(1f,0f,1f,0f,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                removeImageButton.setVisibility(View.GONE);
            }
    
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
            }
    
            @Override
            public void onAnimationRepeat(Animation animation) {
        
            }
        });
        
        imageView.startAnimation(anim);
    }
    
    public void addLocationOnClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(CreatePostActivity.this, R.anim.create_post_button_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            
            }
        
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(CreatePostActivity.this, UserPostLocationActivity.class);
    
                startActivityForResult(intent, ApplicationConstants.CREATE_POST_ADD_LOCATION_REQUEST_CODE);
            }
        
            @Override
            public void onAnimationRepeat(Animation animation) {
            
            }
        });
        addLocationButton.startAnimation(animation);
    }
    
    public void imageOnClick(View view) {
        editText.clearFocus();
        imageView.requestFocusFromTouch();
        zoomImageFromThumb(imageView, findViewById(R.id.create_post_expanded_image), R.id.create_post_view, localImageUri);
    }
    
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator currentAnimator;
    
    /**
     * Amended method from the android docs to expand an {@link ImageView} to span the specified containerId.
     *
     * @param thumbView the {@link ImageView} to expand.
     * @see <a href="https://developer.android.com/training/animation/zoom.html">https://developer.android.com/training/animation/zoom.html</a>
     */
    private void zoomImageFromThumb(final ImageView thumbView, final ImageView expandedImageView, @IdRes final int containerId, Uri imageUri) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        
        if (imageUri == null) {
            thumbView.buildDrawingCache();
            final Bitmap bitmap = thumbView.getDrawingCache();
            expandedImageView.setImageBitmap(bitmap);
            
        } else {
            expandedImageView.setImageURI(imageUri);
        }
        
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(containerId).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        
        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        expandedImageView.setBackgroundColor(0xD0000000);
        
        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
        
        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        
        int shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
        
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(view -> {
            if (currentAnimator != null) {
                currentAnimator.cancel();
            }
    
            expandedImageView.setBackgroundColor(0x00000000);
            
            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y,startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
            set1.setDuration(shortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    currentAnimator = null;
                }
                
                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    currentAnimator = null;
                }
            });
            set1.start();
            currentAnimator = set1;
        });
    }
    
}