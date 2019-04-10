package uk.ac.tees.cupcake.posts;



import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 * This is used to for static images.
 * for non static images use {@link SquareImagePostView}
 */
public class UniversalImageLoader {

    public static void setImage(String URL, ImageView selectedImage, final ProgressBar mBar, String appendUri){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(appendUri + URL, selectedImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mBar != null){
                    mBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mBar != null){
                    mBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mBar != null){
                    mBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mBar != null){
                    mBar.setVisibility(View.GONE);
                }
            }
        });
    }
}

